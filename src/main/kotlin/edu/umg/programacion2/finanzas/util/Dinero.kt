package edu.umg.programacion2.finanzas.util

import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

/** Convierte texto <-> centavos y da formato "Q 1,234.50". */
object Dinero {

    /** Q 10,000,000.00 como tope razonable para una app familiar. */
    const val MAXIMO_CENTAVOS: Long = 1_000_000_000L

    // Solo digitos, con hasta 2 decimales: "45", "45.5", "45.50". Sin signo negativo.
    private val FORMATO_VALIDO = Regex("""^\d{1,8}(\.\d{1,2})?$""")

    /**
     * "1,234.50" -> 123450. Devuelve null si el texto no es un monto valido
     * (vacio, letras, negativo, mas de 2 decimales...). Nunca lanza excepcion.
     */
    fun aCentavos(texto: String): Long? {
        val limpio = texto.trim()
            .removePrefix("Q").removePrefix("$")
            .replace(",", "")
            .trim()
        if (!FORMATO_VALIDO.matches(limpio)) return null
        return BigDecimal(limpio).movePointRight(2).longValueExact()
    }

    /** 123450 -> "Q 1,234.50" ; -500 -> "-Q 5.00" */
    fun formatear(centavos: Long, simbolo: String = "Q"): String {
        val signo = if (centavos < 0) "-" else ""
        val valor = BigDecimal.valueOf(Math.abs(centavos), 2)
        // Locale.US: punto decimal y coma de miles, como se usa en Guatemala.
        val formato = DecimalFormat("#,##0.00", DecimalFormatSymbols(Locale.US))
        return "$signo$simbolo ${formato.format(valor)}"
    }

    /** 123450 -> "1234.50" (para precargar un campo de texto al editar). */
    fun aTextoEditable(centavos: Long): String = BigDecimal.valueOf(centavos, 2).toPlainString()
}
