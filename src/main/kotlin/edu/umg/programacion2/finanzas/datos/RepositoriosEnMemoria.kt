package edu.umg.programacion2.finanzas.datos

import edu.umg.programacion2.finanzas.modelo.Movimiento
import edu.umg.programacion2.finanzas.modelo.Usuario
import java.time.LocalDate

/** Version en memoria para la consola y las pruebas. Se pierde al cerrar el programa. */
class UsuarioRepositoryEnMemoria : UsuarioRepository {
    private val datos = linkedMapOf<Long, Usuario>()
    private var siguienteId = 1L

    override suspend fun guardar(usuario: Usuario): Long {
        val id = if (usuario.id == 0L) siguienteId++ else usuario.id
        datos[id] = usuario.copy(id = id)
        return id
    }
    override suspend fun eliminar(id: Long) { datos.remove(id) }
    override suspend fun buscarPorId(id: Long): Usuario? = datos[id]
    override suspend fun listar(): List<Usuario> = datos.values.sortedBy { it.nombre.lowercase() }
}

class MovimientoRepositoryEnMemoria : MovimientoRepository {
    private val datos = linkedMapOf<Long, Movimiento>()
    private var siguienteId = 1L

    override suspend fun guardar(movimiento: Movimiento): Long {
        val id = if (movimiento.id == 0L) siguienteId++ else movimiento.id
        datos[id] = movimiento.copy(id = id)
        return id
    }
    override suspend fun eliminar(id: Long) { datos.remove(id) }
    override suspend fun buscarPorId(id: Long): Movimiento? = datos[id]
    override suspend fun listarPorUsuario(usuarioId: Long): List<Movimiento> =
        datos.values.filter { it.usuarioId == usuarioId }

    override suspend fun listarPorRango(usuarioId: Long, desde: LocalDate, hasta: LocalDate) =
        listarTodosPorRango(desde, hasta).filter { it.usuarioId == usuarioId }

    override suspend fun listarTodosPorRango(desde: LocalDate, hasta: LocalDate) =
        datos.values.filter { !it.fecha.isBefore(desde) && !it.fecha.isAfter(hasta) }

    override suspend fun eliminarDeUsuario(usuarioId: Long) {
        datos.values.removeAll { it.usuarioId == usuarioId }
    }
}
