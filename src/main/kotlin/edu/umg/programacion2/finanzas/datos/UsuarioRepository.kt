package edu.umg.programacion2.finanzas.datos

import edu.umg.programacion2.finanzas.modelo.Usuario

/**
 * CONTRATO de almacenamiento de usuarios.
 * - Consola / pruebas: UsuarioRepositoryEnMemoria (este proyecto).
 * - Android: una clase con Room que implementa esta MISMA interfaz.
 * El servicio no sabe ni le importa donde se guardan los datos.
 */
interface UsuarioRepository {
    /** Inserta si id == 0, actualiza si no. Devuelve el id. */
    suspend fun guardar(usuario: Usuario): Long
    suspend fun eliminar(id: Long)
    suspend fun buscarPorId(id: Long): Usuario?
    suspend fun listar(): List<Usuario>
}
