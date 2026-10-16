package edu.umg.programacion2.finanzas.util

import edu.umg.programacion2.finanzas.modelo.Movimiento
import java.time.LocalDate

/**
 * TODAS las reglas de datos viven aqui (no en las pantallas).
 * Cada funcion devuelve el mensaje de error para el usuario, o null si todo esta bien.
 */
object Validador {
    const val MAX_NOMBRE = 30
    const val MAX_DESCRIPCION = 60
    const val MAX_NOTA = 200

    /** Quita espacios de mas y caracteres de control (seguridad basica de entrada). */
    fun limpiarTexto(texto: String): String =
        texto.filter { !it.isISOControl() }.trim().replace(Regex("\\s+"), " ")

    fun validarNombreUsuario(nombre: String): String? {
        val limpio = limpiarTexto(nombre)
        return when {
            limpio.isEmpty() -> "Escribe un nombre"
            limpio.length > MAX_NOMBRE -> "El nombre puede tener máximo $MAX_NOMBRE letras"
            else -> null
        }
    }

    fun validarMovimiento(m: Movimiento, hoy: LocalDate = LocalDate.now()): String? = when {
        m.usuarioId <= 0 -> "Selecciona un usuario"
        m.descripcion.isBlank() -> "La descripción no puede estar vacía"
        m.descripcion.length > MAX_DESCRIPCION -> "La descripción puede tener máximo $MAX_DESCRIPCION caracteres"
        m.montoCentavos <= 0 -> "El monto debe ser mayor a cero"
        m.montoCentavos > Dinero.MAXIMO_CENTAVOS -> "El monto es demasiado grande"
        m.fecha.isAfter(hoy) -> "La fecha no puede ser futura"
        m.nota.length > MAX_NOTA -> "La nota puede tener máximo $MAX_NOTA caracteres"
        else -> null
    }
}
