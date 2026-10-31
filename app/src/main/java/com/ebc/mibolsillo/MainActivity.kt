package com.ebc.mibolsillo

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ebc.mibolsillo.data.MovimientoFinanciero
import com.ebc.mibolsillo.ui.components.MovimientoItem
import com.ebc.mibolsillo.ui.components.TarjetaResumen
import com.ebc.mibolsillo.ui.inicio.InicioViewModel
import com.ebc.mibolsillo.ui.movimientos.FormularioMovimiento
import com.ebc.mibolsillo.ui.theme.MiBolsilloTheme
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

// Colores de MiBolsillo
private val Azul = Color(0xFF2358C8)
private val Fondo = Color(0xFFF5F7FB)
private val Verde = Color(0xFF16865D)
private val Rojo = Color(0xFFC74444)

// Formato de moneda mexicana
fun formatoMoneda(cantidad: Double): String {
    return NumberFormat.getCurrencyInstance(
        Locale.forLanguageTag("es-MX")
    ).format(cantidad)
}

// Actividad principal
class MainActivity : ComponentActivity() {

    private val inicioViewModel: InicioViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MiBolsilloTheme {
                PantallaInicio(
                    viewModel = inicioViewModel
                )
            }
        }
    }
}

// Pantalla principal
@Composable
fun PantallaInicio(
    viewModel: InicioViewModel
) {
    val uiState = viewModel.uiState

    // Control del formulario
    var mostrarFormulario by remember {
        mutableStateOf(false)
    }

    // Movimiento seleccionado para editar
    var movimientoAEditar by remember {
        mutableStateOf<MovimientoFinanciero?>(null)
    }

    // Movimiento seleccionado para eliminar
    var movimientoAEliminar by remember {
        mutableStateOf<Long?>(null)
    }

    // Fecha actual
    val calendarioActual = remember {
        Calendar.getInstance()
    }

    // Mes y año seleccionados
    var mesSeleccionado by remember {
        mutableIntStateOf(
            calendarioActual.get(Calendar.MONTH)
        )
    }

    var anioSeleccionado by remember {
        mutableIntStateOf(
            calendarioActual.get(Calendar.YEAR)
        )
    }

    // Permite consultar todos los movimientos
    var mostrarTodos by remember {
        mutableStateOf(false)
    }

    // Control de los menús
    var menuMesAbierto by remember {
        mutableStateOf(false)
    }

    var menuAnioAbierto by remember {
        mutableStateOf(false)
    }

    val nombresMeses = listOf(
        "Enero",
        "Febrero",
        "Marzo",
        "Abril",
        "Mayo",
        "Junio",
        "Julio",
        "Agosto",
        "Septiembre",
        "Octubre",
        "Noviembre",
        "Diciembre"
    )

    // Años disponibles según los movimientos registrados
    val aniosDisponibles = remember(
        uiState.movimientos,
        anioSeleccionado
    ) {
        (
                uiState.movimientos.map { movimiento ->
                    Calendar.getInstance().apply {
                        timeInMillis = movimiento.fecha
                    }.get(Calendar.YEAR)
                } + anioSeleccionado
                ).distinct().sortedDescending()
    }

    // Filtrar movimientos por mes y año
    val movimientosFiltrados = remember(
        uiState.movimientos,
        mesSeleccionado,
        anioSeleccionado,
        mostrarTodos
    ) {
        uiState.movimientos
            .filter { movimiento ->

                if (mostrarTodos) {
                    true
                } else {
                    val calendarioMovimiento =
                        Calendar.getInstance().apply {
                            timeInMillis = movimiento.fecha
                        }

                    val mesMovimiento =
                        calendarioMovimiento.get(Calendar.MONTH)

                    val anioMovimiento =
                        calendarioMovimiento.get(Calendar.YEAR)

                    mesMovimiento == mesSeleccionado &&
                            anioMovimiento == anioSeleccionado
                }
            }
            .sortedByDescending { it.fecha }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        // Título
        Text(
            text = "MiBolsillo",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Azul
        )

        Text(
            text = "Organiza tus finanzas personales",
            fontSize = 15.sp,
            color = Color.DarkGray
        )

        // Tarjeta de saldo disponible
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Azul
            ),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Saldo disponible",
                    color = Color.White,
                    fontSize = 16.sp
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = formatoMoneda(
                        uiState.saldoDisponible
                    ),
                    color = Color.White,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Pesos mexicanos (MXN)",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
            }
        }

        // Resumen de ingresos y gastos
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            TarjetaResumen(
                titulo = "Ingresos",
                cantidad = formatoMoneda(uiState.ingresos),
                color = Verde,
                modifier = Modifier.weight(1f)
            )

            TarjetaResumen(
                titulo = "Gastos",
                cantidad = formatoMoneda(uiState.gastos),
                color = Rojo,
                modifier = Modifier.weight(1f)
            )
        }

        // Botón para registrar movimientos
        Button(
            onClick = {
                movimientoAEditar = null
                mostrarFormulario = true
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Azul
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "+ Registrar movimiento",
                modifier = Modifier.padding(8.dp)
            )
        }

        // Historial
        Text(
            text = "Movimientos recientes",
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF202635)
        )

        // Selector de periodo
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    text = "Filtrar por fecha",
                    fontWeight = FontWeight.SemiBold,
                    color = Azul
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    // Selector de mes
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedButton(
                            onClick = {
                                menuMesAbierto = true
                            },
                            enabled = !mostrarTodos,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = nombresMeses[mesSeleccionado],
                                maxLines = 1
                            )
                        }

                        DropdownMenu(
                            expanded = menuMesAbierto,
                            onDismissRequest = {
                                menuMesAbierto = false
                            }
                        ) {
                            nombresMeses.forEachIndexed {
                                    indice, nombre ->

                                DropdownMenuItem(
                                    text = {
                                        Text(nombre)
                                    },
                                    onClick = {
                                        mesSeleccionado = indice
                                        menuMesAbierto = false
                                    }
                                )
                            }
                        }
                    }

                    // Selector de año
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedButton(
                            onClick = {
                                menuAnioAbierto = true
                            },
                            enabled = !mostrarTodos,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = anioSeleccionado.toString()
                            )
                        }

                        DropdownMenu(
                            expanded = menuAnioAbierto,
                            onDismissRequest = {
                                menuAnioAbierto = false
                            }
                        ) {
                            aniosDisponibles.forEach { anio ->

                                DropdownMenuItem(
                                    text = {
                                        Text(anio.toString())
                                    },
                                    onClick = {
                                        anioSeleccionado = anio
                                        menuAnioAbierto = false
                                    }
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Mostrar todos",
                        color = Color.DarkGray
                    )

                    Switch(
                        checked = mostrarTodos,
                        onCheckedChange = {
                            mostrarTodos = it
                        }
                    )
                }

                Text(
                    text = if (mostrarTodos) {
                        "Mostrando todos los movimientos"
                    } else {
                        "Periodo: ${nombresMeses[mesSeleccionado]} de $anioSeleccionado"
                    },
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        // Lista de movimientos filtrados
        if (movimientosFiltrados.isEmpty()) {

            Text(
                text = if (uiState.movimientos.isEmpty()) {
                    "Todavía no tienes movimientos registrados."
                } else {
                    "No hay movimientos registrados en este periodo."
                },
                color = Color.DarkGray,
                fontSize = 14.sp
            )

        } else {

            movimientosFiltrados.forEach { movimiento ->

                val esIngreso =
                    movimiento.tipo == "Ingreso"

                val concepto = movimiento.descripcion
                    .ifBlank { movimiento.categoria }

                val signo =
                    if (esIngreso) "+" else "-"

                MovimientoItem(
                    concepto = concepto,
                    categoria = movimiento.categoria,
                    cantidad = signo + formatoMoneda(
                        movimiento.monto
                    ),
                    color = if (esIngreso) Verde else Rojo,
                    fecha = movimiento.fecha,
                    alEditar = {
                        movimientoAEditar = movimiento
                    },
                    alEliminar = {
                        movimientoAEliminar = movimiento.id
                    }
                )
            }
        }
    }

    // Formulario para registrar movimientos
    if (mostrarFormulario) {

        FormularioMovimiento(
            alCerrar = {
                mostrarFormulario = false
            },
            alGuardar = {
                    tipo,
                    monto,
                    categoria,
                    descripcion,
                    fecha ->

                viewModel.agregarMovimiento(
                    tipo = tipo,
                    monto = monto,
                    categoria = categoria,
                    descripcion = descripcion,
                    fecha = fecha
                )

                mostrarFormulario = false
            }
        )
    }

    // Formulario para editar un movimiento existente
    movimientoAEditar?.let { movimiento ->
        FormularioMovimiento(
            movimientoEditar = movimiento,
            alCerrar = {
                movimientoAEditar = null
            },
            alGuardar = { tipo, monto, categoria, descripcion, fecha ->
                viewModel.editarMovimiento(
                    movimiento.copy(
                        tipo = tipo,
                        monto = monto,
                        categoria = categoria,
                        descripcion = descripcion,
                        fecha = fecha
                    )
                )
                movimientoAEditar = null
            }
        )
    }

    // Confirmación para eliminar movimientos
    if (movimientoAEliminar != null) {

        AlertDialog(
            onDismissRequest = {
                movimientoAEliminar = null
            },
            title = {
                Text(
                    text = "Eliminar movimiento",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "¿Estás segura de que deseas eliminar este movimiento? Esta acción no se puede deshacer."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        movimientoAEliminar?.let { id ->
                            viewModel.eliminarMovimiento(id)
                        }

                        movimientoAEliminar = null
                    }
                ) {
                    Text(
                        text = "Eliminar",
                        color = Rojo
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        movimientoAEliminar = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

// Vista previa
@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun PantallaInicioPreview() {

    MiBolsilloTheme {

        val contexto = LocalContext.current

        val aplicacion =
            contexto.applicationContext as Application

        PantallaInicio(
            viewModel = InicioViewModel(aplicacion)
        )
    }
}
