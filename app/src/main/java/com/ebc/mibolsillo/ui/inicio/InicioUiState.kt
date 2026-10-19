
package com.ebc.mibolsillo.ui.inicio

import com.ebc.mibolsillo.data.MovimientoFinanciero

data class InicioUiState(
    val ingresos: Double = 0.0,
    val gastos: Double = 0.0,
    val saldoDisponible: Double = 0.0,
    val movimientos: List<MovimientoFinanciero> = emptyList()
)
