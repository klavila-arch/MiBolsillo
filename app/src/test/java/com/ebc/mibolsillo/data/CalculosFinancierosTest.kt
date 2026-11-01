
package com.ebc.mibolsillo.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CalculosFinancierosTest {

    // Crear movimientos ficticios para las pruebas
    private fun movimiento(
        id: Long,
        tipo: String,
        monto: Double
    ): MovimientoFinanciero {
        return MovimientoFinanciero(
            id = id,
            tipo = tipo,
            monto = monto,
            categoria = "Prueba",
            descripcion = "Movimiento de prueba",
            fecha = 1L
        )
    }

    // PRUEBA 1: Calcular ingresos correctamente
    @Test
    fun calcularIngresosCorrectamente() {
        val movimientos = listOf(
            movimiento(1L, "Ingreso", 8000.0),
            movimiento(2L, "Ingreso", 3000.0)
        )

        val resumen = calcularResumenFinanciero(movimientos)

        assertEquals(11000.0, resumen.ingresos, 0.001)
    }

    // PRUEBA 2: Calcular gastos correctamente
    @Test
    fun calcularGastosCorrectamente() {
        val movimientos = listOf(
            movimiento(1L, "Gasto", 1000.0),
            movimiento(2L, "Gasto", 500.0)
        )

        val resumen = calcularResumenFinanciero(movimientos)

        assertEquals(1500.0, resumen.gastos, 0.001)
    }

    // PRUEBA 3: Calcular saldo disponible
    @Test
    fun calcularSaldoDisponible() {
        val movimientos = listOf(
            movimiento(1L, "Ingreso", 11000.0),
            movimiento(2L, "Gasto", 1500.0)
        )

        val resumen = calcularResumenFinanciero(movimientos)

        assertEquals(9500.0, resumen.saldoDisponible, 0.001)
    }

    // PRUEBA 4: Recalcular saldo al modificar un gasto
    @Test
    fun actualizarSaldoAlModificarGasto() {
        val movimientos = listOf(
            movimiento(1L, "Ingreso", 11000.0),
            movimiento(2L, "Gasto", 1500.0)
        )

        val movimientosEditados = movimientos.map {
            if (it.id == 2L) {
                it.copy(monto = 1000.0)
            } else {
                it
            }
        }

        val resumen = calcularResumenFinanciero(movimientosEditados)

        assertEquals(11000.0, resumen.ingresos, 0.001)
        assertEquals(1000.0, resumen.gastos, 0.001)
        assertEquals(10000.0, resumen.saldoDisponible, 0.001)
    }

    // PRUEBA 5: Calcular saldo sin movimientos
    @Test
    fun calcularSaldoSinMovimientos() {
        val resumen = calcularResumenFinanciero(emptyList())

        assertEquals(0.0, resumen.ingresos, 0.001)
        assertEquals(0.0, resumen.gastos, 0.001)
        assertEquals(0.0, resumen.saldoDisponible, 0.001)
    }

    // PRUEBA 6: Recalcular saldo después de eliminar un gasto
    @Test
    fun calcularSaldoDespuesDeEliminarGasto() {
        val movimientos = listOf(
            movimiento(1L, "Ingreso", 11000.0),
            movimiento(2L, "Gasto", 1500.0)
        )

        val movimientosRestantes = movimientos.filter {
            it.id != 2L
        }

        val resumen = calcularResumenFinanciero(movimientosRestantes)

        assertEquals(11000.0, resumen.ingresos, 0.001)
        assertEquals(0.0, resumen.gastos, 0.001)
        assertEquals(11000.0, resumen.saldoDisponible, 0.001)
    }

    // PRUEBA 7: Aceptar un monto positivo
    @Test
    fun aceptarMontoPositivo() {
        assertTrue(esMontoValido(500.0))
    }

    // PRUEBA 8: Rechazar un monto negativo
    @Test
    fun rechazarMontoNegativo() {
        assertFalse(esMontoValido(-500.0))
    }

    // PRUEBA 9: Rechazar un monto igual a cero
    @Test
    fun rechazarMontoCero() {
        assertFalse(esMontoValido(0.0))
    }

    // PRUEBA 10: Rechazar un valor NaN
    @Test
    fun rechazarMontoNaN() {
        assertFalse(esMontoValido(Double.NaN))
    }

    // PRUEBA 11: Rechazar valores infinitos
    @Test
    fun rechazarMontoInfinito() {
        assertFalse(esMontoValido(Double.POSITIVE_INFINITY))
        assertFalse(esMontoValido(Double.NEGATIVE_INFINITY))
    }
}
