package edu.umg.programacion2.finanzas.servicio

import edu.umg.programacion2.finanzas.datos.MovimientoRepository
import edu.umg.programacion2.finanzas.datos.UsuarioRepository
import edu.umg.programacion2.finanzas.modelo.Movimiento
import edu.umg.programacion2.finanzas.modelo.ResumenMensual
import edu.umg.programacion2.finanzas.modelo.Resultado
import edu.umg.programacion2.finanzas.modelo.TipoMovimiento
import edu.umg.programacion2.finanzas.modelo.Usuario
import edu.umg.programacion2.finanzas.util.Validador
import java.time.LocalDate
import java.time.YearMonth

/**
 * El "cerebro" de la app. Recibe los repositorios por el constructor
 * (inyeccion de dependencias): en consola van los de memoria, en Android los de Room.
 */
class ServicioFinanzas(
    private val usuarios: UsuarioRepository,
    private val movimientos: MovimientoRepository
) {

    // ------------------------- Usuarios -------------------------

    suspend fun listarUsuarios(): List<Usuario> = usuarios.listar()

    suspend fun buscarUsuario(id: Long): Usuario? = usuarios.buscarPorId(id)

    /** Crea o edita un perfil. No permite dos perfiles con el mismo nombre. */
    suspend fun guardarUsuario(usuario: Usuario): Resultado<Long> {
        Validador.validarNombreUsuario(usuario.nombre)?.let { return Resultado.Error(it) }
        val nombre = Validador.limpiarTexto(usuario.nombre)

        val repetido = usuarios.listar().any {
            it.id != usuario.id && it.nombre.equals(nombre, ignoreCase = true)
        }
        if (repetido) return Resultado.Error("Ya existe un perfil llamado $nombre")

        return Resultado.Exito(usuarios.guardar(usuario.copy(nombre = nombre)))
    }

    /** Borra el perfil Y todos sus movimientos. */
    suspend fun eliminarUsuario(id: Long) {
        movimientos.eliminarDeUsuario(id)
        usuarios.eliminar(id)
    }

    // ----------------------- Movimientos ------------------------

    suspend fun guardarMovimiento(movimiento: Movimiento, hoy: LocalDate = LocalDate.now()): Resultado<Long> {
        val limpio = movimiento.copy(
            descripcion = Validador.limpiarTexto(movimiento.descripcion),
            nota = Validador.limpiarTexto(movimiento.nota)
        )
        Validador.validarMovimiento(limpio, hoy)?.let { return Resultado.Error(it) }
        if (usuarios.buscarPorId(limpio.usuarioId) == null) {
            return Resultado.Error("El usuario ya no existe")
        }
        return Resultado.Exito(movimientos.guardar(limpio))
    }

    suspend fun buscarMovimiento(id: Long): Movimiento? = movimientos.buscarPorId(id)

    suspend fun eliminarMovimiento(id: Long) = movimientos.eliminar(id)

    /** Movimientos del mes, del mas reciente al mas antiguo (para el historial). */
    suspend fun movimientosDelMes(usuarioId: Long, mes: YearMonth): List<Movimiento> =
        movimientos.listarPorRango(usuarioId, mes.atDay(1), mes.atEndOfMonth())
            .sortedWith(compareByDescending<Movimiento> { it.fecha }.thenByDescending { it.id })

    // ------------------------- Reportes -------------------------

    suspend fun resumenDelMes(usuarioId: Long, mes: YearMonth): ResumenMensual =
        resumir(mes, movimientos.listarPorRango(usuarioId, mes.atDay(1), mes.atEndOfMonth()))

    /** Lo mismo pero sumando a TODA la familia. */
    suspend fun resumenFamiliarDelMes(mes: YearMonth): ResumenMensual =
        resumir(mes, movimientos.listarTodosPorRango(mes.atDay(1), mes.atEndOfMonth()))

    /** Saldo acumulado de toda la historia del usuario (ingresos - gastos). */
    suspend fun saldoActual(usuarioId: Long): Long =
        movimientos.listarPorUsuario(usuarioId).sumOf { if (it.esIngreso) it.montoCentavos else -it.montoCentavos }

    private fun resumir(mes: YearMonth, lista: List<Movimiento>): ResumenMensual {
        val gastos = lista.filter { it.tipo == TipoMovimiento.GASTO }
        return ResumenMensual(
            mes = mes,
            totalIngresos = lista.filter { it.esIngreso }.sumOf { it.montoCentavos },
            totalGastos = gastos.sumOf { it.montoCentavos },
            gastosPorCategoria = gastos.groupBy { it.categoria }
                .mapValues { (_, l) -> l.sumOf { it.montoCentavos } }
                .toList().sortedByDescending { it.second }.toMap(),   // mayor gasto primero
            cantidadMovimientos = lista.size
        )
    }
}
