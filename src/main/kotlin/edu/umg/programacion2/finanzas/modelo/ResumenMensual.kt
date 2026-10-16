package edu.umg.programacion2.finanzas.modelo

import java.time.YearMonth

/** Datos listos para el Dashboard: tarjetas de totales y grafica por categoria. */
data class ResumenMensual(
    val mes: YearMonth,
    val totalIngresos: Long,
    val totalGastos: Long,
    val gastosPorCategoria: Map<Categoria, Long>,
    val cantidadMovimientos: Int
) {
    /** Lo que queda del mes (puede ser negativo). */
    val balance: Long get() = totalIngresos - totalGastos

    /** % de los ingresos que ya se gasto (0..100+). 0 si no hay ingresos. */
    val porcentajeGastado: Int
        get() = if (totalIngresos <= 0) 0 else ((totalGastos * 100) / totalIngresos).toInt()
}
