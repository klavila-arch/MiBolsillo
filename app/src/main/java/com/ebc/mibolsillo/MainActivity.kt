
package com.ebc.mibolsillo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ebc.mibolsillo.ui.theme.MiBolsilloTheme
import com.ebc.mibolsillo.ui.components.TarjetaResumen
import com.ebc.mibolsillo.ui.components.MovimientoItem
import com.ebc.mibolsillo.ui.movimientos.FormularioMovimiento
import com.ebc.mibolsillo.ui.inicio.InicioViewModel
import java.text.NumberFormat
import java.util.Locale

private val Azul = Color(0xFF2358C8)
private val Fondo = Color(0xFFF5F7FB)
private val Verde = Color(0xFF16865D)
private val Rojo = Color(0xFFC74444)

fun formatoMoneda(cantidad: Double): String {
    return NumberFormat.getCurrencyInstance(
        Locale.forLanguageTag("es-MX")
    ).format(cantidad)
}

class MainActivity : ComponentActivity() {

    private val inicioViewModel: InicioViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MiBolsilloTheme {
                PantallaInicio(inicioViewModel)
            }
        }
    }
}

@Composable
fun PantallaInicio(
    viewModel: InicioViewModel
) {
    val uiState = viewModel.uiState

    var mostrarFormulario by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
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

        Button(
            onClick = {
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

        Text(
            text = "Movimientos recientes",
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF202635)
        )

        if (uiState.movimientos.isEmpty()) {
            Text(
                text = "Todavía no tienes movimientos registrados.",
                color = Color.DarkGray,
                fontSize = 14.sp
            )
        } else {
            uiState.movimientos
                .asReversed()
                .forEach { movimiento ->

                    val esIngreso = movimiento.tipo == "Ingreso"

                    val concepto = movimiento.descripcion
                        .ifBlank { movimiento.categoria }

                    val signo = if (esIngreso) "+" else "-"

                    MovimientoItem(
                        concepto = concepto,
                        categoria = movimiento.categoria,
                        cantidad = signo + formatoMoneda(
                            movimiento.monto
                        ),
                        color = if (esIngreso) Verde else Rojo
                    )
                }
        }
    }

    if (mostrarFormulario) {
        FormularioMovimiento(
            alCerrar = {
                mostrarFormulario = false
            },
            alGuardar = { tipo, monto, categoria, descripcion ->
                viewModel.agregarMovimiento(
                    tipo = tipo,
                    monto = monto,
                    categoria = categoria,
                    descripcion = descripcion
                )

                mostrarFormulario = false
            }
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun PantallaInicioPreview() {
    MiBolsilloTheme {
        PantallaInicio(
            viewModel = InicioViewModel()
        )
    }
}

