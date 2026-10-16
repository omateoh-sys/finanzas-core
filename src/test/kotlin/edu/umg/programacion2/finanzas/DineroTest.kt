package edu.umg.programacion2.finanzas

import edu.umg.programacion2.finanzas.servicio.ConversorMoneda
import edu.umg.programacion2.finanzas.util.Dinero
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DineroTest {
    @Test fun convierteTextoValidoACentavos() {
        assertEquals(4550L, Dinero.aCentavos("45.50"))
        assertEquals(4550L, Dinero.aCentavos("45.5"))
        assertEquals(123450L, Dinero.aCentavos("1,234.50"))
        assertEquals(10000L, Dinero.aCentavos(" Q 100 "))
    }

    @Test fun rechazaTextoInvalidoSinLanzarExcepcion() {
        listOf("", "abc", "-5", "1.234", "12..5", "1e5").forEach { assertNull(Dinero.aCentavos(it), it) }
    }

    @Test fun formateaConSeparadorDeMiles() {
        assertEquals("Q 1,234.50", Dinero.formatear(123450))
        assertEquals("-Q 5.00", Dinero.formatear(-500))
        assertEquals("$ 0.07", Dinero.formatear(7, "$"))
    }

    @Test fun convierteMonedas() {
        val c = ConversorMoneda(BigDecimal("7.66"))
        assertEquals(76600L, c.dolaresAQuetzales(10000))   // $100 -> Q766
        assertEquals(10000L, c.quetzalesADolares(76600))   // Q766 -> $100
    }
}
