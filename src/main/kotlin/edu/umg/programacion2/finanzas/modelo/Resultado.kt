package edu.umg.programacion2.finanzas.modelo

/**
 * Respuesta de las operaciones que pueden fallar por datos invalidos.
 * La pantalla hace: when (r) { is Exito -> ...; is Error -> mostrar r.mensaje }
 */
sealed class Resultado<out T> {
    data class Exito<T>(val valor: T) : Resultado<T>()
    data class Error(val mensaje: String) : Resultado<Nothing>()
}
