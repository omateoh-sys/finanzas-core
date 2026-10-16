package edu.umg.programacion2.finanzas.modelo

/**
 * Categorias fijas de ingresos y gastos.
 *
 * IMPORTANTE: Android guarda en la base de datos el NOMBRE de la constante (ej. "ALIMENTACION").
 * Si renombras una constante, los datos viejos ya no se podran leer. Agregar nuevas si se puede.
 */
enum class Categoria(val tipo: TipoMovimiento, private val etiqueta: String) {
    // ---- Ingresos ----
    SALARIO(TipoMovimiento.INGRESO, "Salario"),
    NEGOCIO(TipoMovimiento.INGRESO, "Negocio / ventas"),
    REMESA(TipoMovimiento.INGRESO, "Remesas"),
    BONO(TipoMovimiento.INGRESO, "Bono 14 / Aguinaldo"),
    REGALO_RECIBIDO(TipoMovimiento.INGRESO, "Regalos recibidos"),
    OTRO_INGRESO(TipoMovimiento.INGRESO, "Otros ingresos"),

    // ---- Gastos cotidianos ----
    ALIMENTACION(TipoMovimiento.GASTO, "Alimentación"),
    TRANSPORTE(TipoMovimiento.GASTO, "Transporte"),
    SERVICIOS(TipoMovimiento.GASTO, "Servicios (luz, agua, internet)"),
    VIVIENDA(TipoMovimiento.GASTO, "Vivienda / alquiler"),
    SALUD(TipoMovimiento.GASTO, "Salud y medicinas"),
    EDUCACION(TipoMovimiento.GASTO, "Educación"),
    ROPA(TipoMovimiento.GASTO, "Ropa y calzado"),
    ENTRETENIMIENTO(TipoMovimiento.GASTO, "Entretenimiento"),
    DIEZMOS(TipoMovimiento.GASTO, "Diezmos y ofrendas"),
    DEUDAS(TipoMovimiento.GASTO, "Deudas y préstamos"),
    OTRO_GASTO(TipoMovimiento.GASTO, "Otros gastos");

    /** Lo que ve el usuario (Spinner, listas). */
    override fun toString(): String = etiqueta

    companion object {
        /** Solo las categorias de un tipo: para llenar el Spinner segun Ingreso/Gasto. */
        fun delTipo(tipo: TipoMovimiento): List<Categoria> = entries.filter { it.tipo == tipo }
    }
}
