package edu.umg.programacion2.finanzas.modelo

/** Un movimiento de dinero solo puede ser una entrada o una salida. */
enum class TipoMovimiento(private val etiqueta: String) {
    INGRESO("Ingreso"),
    GASTO("Gasto");

    override fun toString(): String = etiqueta
}
