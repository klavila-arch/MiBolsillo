
package com.ebc.mibolsillo.ui.movimientos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

private val Azul = Color(0xFF2358C8)

@Composable
fun FormularioMovimiento(
    alCerrar: () -> Unit,
    alGuardar: (
        tipo: String,
        monto: Double,
        categoria: String,
        descripcion: String
    ) -> Unit
) {
    var tipo by remember { mutableStateOf("Gasto") }
    var monto by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("Alimentación") }
    var menuAbierto by remember { mutableStateOf(false) }

    val categorias = if (tipo == "Gasto") {
        listOf(
            "Alimentación",
            "Transporte",
            "Vivienda",
            "Entretenimiento",
            "Salud",
            "Otros"
        )
    } else {
        listOf(
            "Sueldo",
            "Ventas",
            "Regalos",
            "Otros"
        )
    }

    val montoNumerico = monto.toDoubleOrNull()
    val montoValido = montoNumerico != null &&
            montoNumerico > 0.0 &&
            montoNumerico.isFinite()

    AlertDialog(
        onDismissRequest = alCerrar,
        title = {
            Text(
                text = "Registrar movimiento",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Selecciona el tipo de movimiento")

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = tipo == "Gasto",
                        onClick = {
                            tipo = "Gasto"
                            categoria = "Alimentación"
                        },
                        label = { Text("Gasto") }
                    )

                    FilterChip(
                        selected = tipo == "Ingreso",
                        onClick = {
                            tipo = "Ingreso"
                            categoria = "Sueldo"
                        },
                        label = { Text("Ingreso") }
                    )
                }

                OutlinedTextField(
                    value = monto,
                    onValueChange = { monto = it },
                    label = { Text("Monto (MXN)") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Box {
                    OutlinedButton(
                        onClick = { menuAbierto = true }
                    ) {
                        Text("Categoría: $categoria")
                    }

                    DropdownMenu(
                        expanded = menuAbierto,
                        onDismissRequest = {
                            menuAbierto = false
                        }
                    ) {
                        categorias.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion) },
                                onClick = {
                                    categoria = opcion
                                    menuAbierto = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = {
                        Text("Descripción (opcional)")
                    },
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val cantidad = montoNumerico
                    if (cantidad != null && montoValido) {
                        alGuardar(
                            tipo,
                            cantidad,
                            categoria,
                            descripcion.trim()
                        )
                    }
                },
                enabled = montoValido
            ) {
                Text("Guardar", color = Azul)
            }
        },
        dismissButton = {
            TextButton(onClick = alCerrar) {
                Text("Cancelar")
            }
        }
    )
}
