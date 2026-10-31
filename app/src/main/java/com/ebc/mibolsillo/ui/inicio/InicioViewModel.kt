
package com.ebc.mibolsillo.ui.inicio

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ebc.mibolsillo.data.MovimientoFinanciero
import com.ebc.mibolsillo.data.MovimientoStorage
import kotlinx.coroutines.launch

class InicioViewModel(application: Application) :
    AndroidViewModel(application) {

    private val storage = MovimientoStorage(application)

    var uiState by mutableStateOf(InicioUiState())
        private set

    init {
        viewModelScope.launch {
            storage.movimientos.collect { lista ->
                actualizarEstado(lista)
            }
        }
    }

    // Registrar un movimiento nuevo
    fun agregarMovimiento(
        tipo: String,
        monto: Double,
        categoria: String,
        descripcion: String,
        fecha: Long
    ) {
        if (monto <= 0.0 || !monto.isFinite()) return
        if (tipo != "Ingreso" && tipo != "Gasto") return

        val nuevoMovimiento = MovimientoFinanciero(
            id = System.currentTimeMillis(),
            tipo = tipo,
            monto = monto,
            categoria = categoria,
            descripcion = descripcion,
            fecha = fecha
        )

        val nuevaLista =
            uiState.movimientos + nuevoMovimiento

        actualizarEstado(nuevaLista)

        viewModelScope.launch {
            storage.guardarMovimientos(nuevaLista)
        }
    }

    // Editar un movimiento existente
    fun editarMovimiento(
        movimientoActualizado: MovimientoFinanciero
    ) {
        if (
            movimientoActualizado.monto <= 0.0 ||
            !movimientoActualizado.monto.isFinite()
        ) {
            return
        }

        if (
            movimientoActualizado.tipo != "Ingreso" &&
            movimientoActualizado.tipo != "Gasto"
        ) {
            return
        }

        val existeMovimiento = uiState.movimientos.any {
            it.id == movimientoActualizado.id
        }

        if (!existeMovimiento) return

        val nuevaLista = uiState.movimientos.map { movimiento ->
            if (movimiento.id == movimientoActualizado.id) {
                movimientoActualizado
            } else {
                movimiento
            }
        }

        actualizarEstado(nuevaLista)

        viewModelScope.launch {
            storage.guardarMovimientos(nuevaLista)
        }
    }

    // Eliminar un movimiento por su identificador
    fun eliminarMovimiento(id: Long) {
        val nuevaLista = uiState.movimientos.filter { movimiento ->
            movimiento.id != id
        }

        actualizarEstado(nuevaLista)

        viewModelScope.launch {
            storage.guardarMovimientos(nuevaLista)
        }
    }

    // Recalcular ingresos, gastos y saldo disponible
    private fun actualizarEstado(
        lista: List<MovimientoFinanciero>
    ) {
        val ingresos = lista
            .filter { it.tipo == "Ingreso" }
            .sumOf { it.monto }

        val gastos = lista
            .filter { it.tipo == "Gasto" }
            .sumOf { it.monto }

        uiState = InicioUiState(
            ingresos = ingresos,
            gastos = gastos,
            saldoDisponible = ingresos - gastos,
            movimientos = lista
        )
    }
}
