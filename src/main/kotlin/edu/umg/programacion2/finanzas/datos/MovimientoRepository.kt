package edu.umg.programacion2.finanzas.datos

import edu.umg.programacion2.finanzas.modelo.Movimiento
import java.time.LocalDate

/** CONTRATO de almacenamiento de ingresos y gastos (ver UsuarioRepository). */
interface MovimientoRepository {
    /** Inserta si id == 0, actualiza si no. Devuelve el id. */
    suspend fun guardar(movimiento: Movimiento): Long
    suspend fun eliminar(id: Long)
    suspend fun buscarPorId(id: Long): Movimiento?
    suspend fun listarPorUsuario(usuarioId: Long): List<Movimiento>
    /** Movimientos con fecha entre [desde] y [hasta], ambas incluidas. */
    suspend fun listarPorRango(usuarioId: Long, desde: LocalDate, hasta: LocalDate): List<Movimiento>
    /** Igual que listarPorRango pero de TODA la familia. */
    suspend fun listarTodosPorRango(desde: LocalDate, hasta: LocalDate): List<Movimiento>
    suspend fun eliminarDeUsuario(usuarioId: Long)
}
