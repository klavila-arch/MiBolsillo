package com.ebc.mibolsillo.data

data class ResumenFinanciero(
    val ingresos: Double,
    val gastos: Double,
    val saldoDisponible: Double
)

fun calcularResumenFinanciero(
    movimientos: List<MovimientoFinanciero>
): ResumenFinanciero {

    val ingresos = movimientos
        .filter { it.tipo == "Ingreso" }
        .sumOf { it.monto }

    val gastos = movimientos
        .filter { it.tipo == "Gasto" }
        .sumOf { it.monto }

    return ResumenFinanciero(
        ingresos = ingresos,
        gastos = gastos,
        saldoDisponible = ingresos - gastos
    )
}
fun esMontoValido(monto: Double): Boolean {
    return monto > 0.0 && monto.isFinite()
}

