package edu.umg.programacion2.finanzas.modelo

import java.time.LocalDate

/**
 * Un ingreso o un gasto de un usuario.
 *
 * BUENA PRACTICA: el dinero se guarda en CENTAVOS (Long), nunca en Double.
 * 0.1 + 0.2 en Double da 0.30000000000000004; con centavos: 10 + 20 = 30, exacto.
 *
 * El tipo (ingreso/gasto) NO se guarda aparte: sale de la categoria. Asi es imposible
 * tener un "INGRESO" con categoria "ALIMENTACION".
 */
data class Movimiento(
    val id: Long = 0,
    val usuarioId: Long,
    val categoria: Categoria,
    val descripcion: String,
    val montoCentavos: Long,
    val fecha: LocalDate,
    val nota: String = ""
) {
    val tipo: TipoMovimiento get() = categoria.tipo
    val esIngreso: Boolean get() = tipo == TipoMovimiento.INGRESO
}
