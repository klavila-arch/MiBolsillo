
package com.ebc.mibolsillo.ui.inicio

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ebc.mibolsillo.data.MovimientoFinanciero

class InicioViewModel : ViewModel() {

    var uiState by mutableStateOf(InicioUiState())
        private set

    fun agregarMovimiento(
        tipo: String,
        monto: Double,
        categoria: String,
        descripcion: String
    ) {
        if (monto <= 0.0 || !monto.isFinite()) return
        if (tipo != "Ingreso" && tipo != "Gasto") return

        val nuevoMovimiento = MovimientoFinanciero(
            id = System.currentTimeMillis(),
            tipo = tipo,
            monto = monto,
            categoria = categoria,
            descripcion = descripcion
        )

        val nuevaLista = uiState.movimientos + nuevoMovimiento

        val totalIngresos = nuevaLista
            .filter { it.tipo == "Ingreso" }
            .sumOf { it.monto }

        val totalGastos = nuevaLista
            .filter { it.tipo == "Gasto" }
            .sumOf { it.monto }

        uiState = InicioUiState(
            ingresos = totalIngresos,
            gastos = totalGastos,
            saldoDisponible = totalIngresos - totalGastos,
            movimientos = nuevaLista
        )
    }
}
