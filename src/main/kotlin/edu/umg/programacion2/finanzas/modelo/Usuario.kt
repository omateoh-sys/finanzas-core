package edu.umg.programacion2.finanzas.modelo

/**
 * Un perfil de la familia (sin PIN: perfiles locales tipo "Netflix").
 *
 * @param id 0 = todavia no se ha guardado; la base de datos asigna el id real.
 * @param avatar indice del avatar/color elegido (la app decide como dibujarlo).
 */
data class Usuario(
    val id: Long = 0,
    val nombre: String,
    val avatar: Int = 0
)
