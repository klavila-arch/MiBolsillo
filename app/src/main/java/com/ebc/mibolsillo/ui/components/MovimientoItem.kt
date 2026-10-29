
package com.ebc.mibolsillo.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MovimientoItem(
    concepto: String,
    categoria: String,
    cantidad: String,
    color: Color,
    alEliminar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = concepto,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )

                Text(
                    text = categoria,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Text(
                text = cantidad,
                color = color,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )

            TextButton(
                onClick = alEliminar,
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                Text(
                    text = "Eliminar",
                    color = Color(0xFFC74444),
                    fontSize = 12.sp
                )
            }
        }
    }
}
