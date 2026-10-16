package edu.umg.programacion2.finanzas

import edu.umg.programacion2.finanzas.datos.MovimientoRepositoryEnMemoria
import edu.umg.programacion2.finanzas.datos.UsuarioRepositoryEnMemoria
import edu.umg.programacion2.finanzas.modelo.Categoria
import edu.umg.programacion2.finanzas.modelo.Movimiento
import edu.umg.programacion2.finanzas.modelo.Resultado
import edu.umg.programacion2.finanzas.modelo.Usuario
import edu.umg.programacion2.finanzas.servicio.ConversorMoneda
import edu.umg.programacion2.finanzas.servicio.Saludos
import edu.umg.programacion2.finanzas.servicio.ServicioFinanzas
import edu.umg.programacion2.finanzas.util.Dinero
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth

/**
 * Demo de consola: prueba la libreria SIN Android.  ->  mvn compile exec:java
 * "suspend fun main" permite llamar funciones suspend sin librerias extra.
 */
suspend fun main() {
    // IMPORTANTE: en Android sera la misma linea, pero con los repositorios de Room.
    val servicio = ServicioFinanzas(UsuarioRepositoryEnMemoria(), MovimientoRepositoryEnMemoria())

    val ana = (servicio.guardarUsuario(Usuario(nombre = "Ana")) as Resultado.Exito).valor
    val luis = (servicio.guardarUsuario(Usuario(nombre = "Luis", avatar = 2)) as Resultado.Exito).valor

    println(Saludos.saludo("Ana", LocalTime.now().hour))

    val hoy = LocalDate.now()
    val pruebas = listOf(
        Movimiento(usuarioId = ana, categoria = Categoria.SALARIO, descripcion = "Salario quincena", montoCentavos = Dinero.aCentavos("3,500.00")!!, fecha = hoy),
        Movimiento(usuarioId = ana, categoria = Categoria.ALIMENTACION, descripcion = "Súper", montoCentavos = Dinero.aCentavos("845.75")!!, fecha = hoy),
        Movimiento(usuarioId = ana, categoria = Categoria.TRANSPORTE, descripcion = "Gasolina", montoCentavos = 30000, fecha = hoy),
        Movimiento(usuarioId = luis, categoria = Categoria.SERVICIOS, descripcion = "Luz", montoCentavos = 42050, fecha = hoy),
        Movimiento(usuarioId = ana, categoria = Categoria.SALUD, descripcion = "   ", montoCentavos = 1000, fecha = hoy)   // invalido
    )
    for (m in pruebas) {
        when (val r = servicio.guardarMovimiento(m)) {
            is Resultado.Exito -> println("✔ Guardado #${r.valor}: ${m.descripcion} ${Dinero.formatear(m.montoCentavos)}")
            is Resultado.Error -> println("✘ Error: ${r.mensaje}")
        }
    }

    val mes = YearMonth.now()
    val r = servicio.resumenDelMes(ana, mes)
    println("\n--- Resumen de Ana ($mes) ---")
    println("Ingresos: ${Dinero.formatear(r.totalIngresos)}")
    println("Gastos:   ${Dinero.formatear(r.totalGastos)}  (${r.porcentajeGastado}% de lo ganado)")
    println("Balance:  ${Dinero.formatear(r.balance)}")
    r.gastosPorCategoria.forEach { (cat, total) -> println("  · $cat: ${Dinero.formatear(total)}") }

    val familia = servicio.resumenFamiliarDelMes(mes)
    println("\nGastos de toda la familia: ${Dinero.formatear(familia.totalGastos)}")

    val conversor = ConversorMoneda(BigDecimal("7.66"))   // tasa de ejemplo; en Android vendra de Banguat
    println("\nBalance en dólares: ${Dinero.formatear(conversor.quetzalesADolares(r.balance), "$")}")
    println("US$ 100.00 = ${Dinero.formatear(conversor.dolaresAQuetzales(10000))}")
}
