package com.ebc.mibolsillo.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

private val Context.movimientosDataStore by preferencesDataStore(
    name = "movimientos"
)

class MovimientoStorage(private val context: Context) {

    private val movimientosKey =
        stringPreferencesKey("lista_movimientos")

    val movimientos: Flow<List<MovimientoFinanciero>> =
        context.movimientosDataStore.data.map { preferencias ->

            val json = preferencias[movimientosKey] ?: "[]"

            try {
                val arreglo = JSONArray(json)

                List(arreglo.length()) { indice ->
                    val objeto = arreglo.getJSONObject(indice)
                    val id = objeto.getLong("id")

                    MovimientoFinanciero(
                        id = id,
                        tipo = objeto.getString("tipo"),
                        monto = objeto.getDouble("monto"),
                        categoria = objeto.getString("categoria"),
                        descripcion = objeto.getString("descripcion"),
                        fecha = objeto.optLong("fecha", id)
                    )
                }
            } catch (_: org.json.JSONException) {
                emptyList()
            }
        }

    suspend fun guardarMovimientos(
        lista: List<MovimientoFinanciero>
    ) {
        val arreglo = JSONArray()

        lista.forEach { movimiento ->
            val objeto = JSONObject().apply {
                put("id", movimiento.id)
                put("tipo", movimiento.tipo)
                put("monto", movimiento.monto)
                put("categoria", movimiento.categoria)
                put("descripcion", movimiento.descripcion)
                put("fecha", movimiento.fecha)
            }

            arreglo.put(objeto)
        }

        context.movimientosDataStore.edit { preferencias ->
            preferencias[movimientosKey] = arreglo.toString()
        }
    }
}