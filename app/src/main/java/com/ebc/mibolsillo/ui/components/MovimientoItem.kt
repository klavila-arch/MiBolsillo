
package com.ebc.mibolsillo.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val Azul = Color(0xFF2358C8)
private val Rojo = Color(0xFFC74444)

private fun formatearFecha(fecha: Long): String {
    val formato = SimpleDateFormat(
        "dd/MM/yyyy",
        Locale.forLanguageTag("es-MX")
    )
    return formato.format(Date(fecha))
}

@Composable
fun MovimientoItem(
    concepto: String,
    categoria: String,
    cantidad: String,
    color: Color,
    fecha: Long,
    alEditar: () -> Unit,
    alEliminar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = concepto,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = Color(0xFF202635)
                )

                Text(
                    text = categoria,
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Text(
                    text = formatearFecha(fecha),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = cantidad,
                    color = color,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                Row(
                    horizontalArrangement =
                        Arrangement.spacedBy(0.dp)
                ) {
                    TextButton(
                        onClick = alEditar,
                        contentPadding =
                            androidx.compose.foundation.layout.PaddingValues(
                                horizontal = 8.dp
                            )
                    ) {
                        Text(
                            text = "Editar",
                            color = Azul,
                            fontSize = 12.sp
                        )
                    }

                    TextButton(
                        onClick = alEliminar,
                        contentPadding =
                            androidx.compose.foundation.layout.PaddingValues(
                                horizontal = 8.dp
                            )
                    ) {
                        Text(
                            text = "Eliminar",
                            color = Rojo,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
