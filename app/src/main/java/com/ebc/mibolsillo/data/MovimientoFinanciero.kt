
package com.ebc.mibolsillo.data

data class MovimientoFinanciero(
    val id: Long,
    val tipo: String,
    val monto: Double,
    val categoria: String,
    val descripcion: String
)
