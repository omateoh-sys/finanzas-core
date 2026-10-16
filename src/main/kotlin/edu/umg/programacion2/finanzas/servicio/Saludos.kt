package edu.umg.programacion2.finanzas.servicio

/** Mensaje de bienvenida segun la hora del dia. */
object Saludos {
    /** @param hora 0..23 */
    fun saludo(nombre: String, hora: Int): String {
        val parte = when (hora) {
            in 5..11 -> "Buenos días"
            in 12..18 -> "Buenas tardes"
            else -> "Buenas noches"
        }
        return "$parte, $nombre"
    }
}
