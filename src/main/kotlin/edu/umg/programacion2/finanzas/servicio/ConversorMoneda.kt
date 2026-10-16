package edu.umg.programacion2.finanzas.servicio

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Conversion Quetzales <-> Dolares con la tasa del dia (Banguat).
 * Trabaja en centavos y con BigDecimal para no perder precision.
 *
 * @param quetzalesPorDolar ej. BigDecimal("7.66") => 1 USD = Q 7.66
 */
class ConversorMoneda(val quetzalesPorDolar: BigDecimal) {

    init {
        require(quetzalesPorDolar > BigDecimal.ZERO) { "La tasa de cambio debe ser mayor a cero" }
    }

    /** Centavos de quetzal -> centavos de dolar. */
    fun quetzalesADolares(centavosQ: Long): Long =
        BigDecimal.valueOf(centavosQ).divide(quetzalesPorDolar, 0, RoundingMode.HALF_EVEN).longValueExact()

    /** Centavos de dolar -> centavos de quetzal. */
    fun dolaresAQuetzales(centavosUsd: Long): Long =
        BigDecimal.valueOf(centavosUsd).multiply(quetzalesPorDolar).setScale(0, RoundingMode.HALF_EVEN).longValueExact()
}
