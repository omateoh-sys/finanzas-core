package edu.umg.programacion2.finanzas

import edu.umg.programacion2.finanzas.datos.MovimientoRepositoryEnMemoria
import edu.umg.programacion2.finanzas.datos.UsuarioRepositoryEnMemoria
import edu.umg.programacion2.finanzas.modelo.Categoria
import edu.umg.programacion2.finanzas.modelo.Movimiento
import edu.umg.programacion2.finanzas.modelo.Resultado
import edu.umg.programacion2.finanzas.modelo.Usuario
import edu.umg.programacion2.finanzas.servicio.ServicioFinanzas
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.YearMonth
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ServicioFinanzasTest {
    private val hoy = LocalDate.of(2026, 10, 15)
    private fun nuevoServicio() = ServicioFinanzas(UsuarioRepositoryEnMemoria(), MovimientoRepositoryEnMemoria())

    private suspend fun ServicioFinanzas.crear(nombre: String) =
        (guardarUsuario(Usuario(nombre = nombre)) as Resultado.Exito).valor

    private fun mov(u: Long, c: Categoria, monto: Long, desc: String = "x", fecha: LocalDate = hoy) =
        Movimiento(usuarioId = u, categoria = c, descripcion = desc, montoCentavos = monto, fecha = fecha)

    @Test fun noPermiteNombresRepetidosNiVacios() = runBlocking {
        val s = nuevoServicio()
        s.crear("Ana")
        assertIs<Resultado.Error>(s.guardarUsuario(Usuario(nombre = "  ana ")))
        assertIs<Resultado.Error>(s.guardarUsuario(Usuario(nombre = "   ")))
        Unit
    }

    @Test fun validaMovimientos() = runBlocking {
        val s = nuevoServicio()
        val ana = s.crear("Ana")
        assertIs<Resultado.Error>(s.guardarMovimiento(mov(ana, Categoria.SALUD, 100, desc = "  "), hoy))
        assertIs<Resultado.Error>(s.guardarMovimiento(mov(ana, Categoria.SALUD, 0), hoy))
        assertIs<Resultado.Error>(s.guardarMovimiento(mov(ana, Categoria.SALUD, 100, fecha = hoy.plusDays(1)), hoy))
        assertIs<Resultado.Error>(s.guardarMovimiento(mov(999, Categoria.SALUD, 100), hoy))
        assertIs<Resultado.Exito<Long>>(s.guardarMovimiento(mov(ana, Categoria.SALUD, 100), hoy))
        Unit
    }

    @Test fun resumenSeparaUsuariosYMeses() = runBlocking {
        val s = nuevoServicio()
        val ana = s.crear("Ana"); val luis = s.crear("Luis")
        s.guardarMovimiento(mov(ana, Categoria.SALARIO, 300000), hoy)
        s.guardarMovimiento(mov(ana, Categoria.ALIMENTACION, 50000), hoy)
        s.guardarMovimiento(mov(ana, Categoria.TRANSPORTE, 25000), hoy)
        s.guardarMovimiento(mov(ana, Categoria.ALIMENTACION, 99900, fecha = hoy.minusMonths(1)), hoy)  // otro mes
        s.guardarMovimiento(mov(luis, Categoria.SERVICIOS, 40000), hoy)

        val r = s.resumenDelMes(ana, YearMonth.from(hoy))
        assertEquals(300000, r.totalIngresos)
        assertEquals(75000, r.totalGastos)
        assertEquals(225000, r.balance)
        assertEquals(25, r.porcentajeGastado)
        assertEquals(Categoria.ALIMENTACION, r.gastosPorCategoria.keys.first())

        assertEquals(115000, s.resumenFamiliarDelMes(YearMonth.from(hoy)).totalGastos)
        assertEquals(300000 - 75000 - 99900L, s.saldoActual(ana))
    }

    @Test fun eliminarUsuarioBorraSusMovimientos() = runBlocking {
        val s = nuevoServicio()
        val ana = s.crear("Ana")
        s.guardarMovimiento(mov(ana, Categoria.ALIMENTACION, 100), hoy)
        s.eliminarUsuario(ana)
        assertTrue(s.listarUsuarios().isEmpty())
        assertEquals(0, s.resumenFamiliarDelMes(YearMonth.from(hoy)).cantidadMovimientos)
    }
}
