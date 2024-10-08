package com.example.lab10_tarea.data

import retrofit2.Response
import retrofit2.http.*

interface AdaptadorApiService {

    @GET("api/registros")
    suspend fun obtenerRegistros(): List<RegistroFuncionamiento>

    @GET("api/registros/{id}")
    suspend fun obtenerRegistro(@Path("id") id: Int): Response<RegistroFuncionamiento>

    @Headers("Content-Type: application/json")
    @POST("api/registros")
    suspend fun crearRegistro(@Body registro: RegistroFuncionamiento): Response<RegistroFuncionamiento>

    @PUT("api/registros/{id}")
    suspend fun actualizarRegistro(@Path("id") id: Int, @Body registro: RegistroFuncionamiento): Response<RegistroFuncionamiento>

    @DELETE("api/registros/{id}")
    suspend fun eliminarRegistro(@Path("id") id: Int): Response<Void>

    @GET("api/configuraciones")
    suspend fun obtenerConfiguraciones(): List<ConfiguracionElectrodomestico>

    @Headers("Content-Type: application/json")
    @POST("api/configuraciones")
    suspend fun crearConfiguracion(@Body configuracion: ConfiguracionElectrodomestico): Response<ConfiguracionElectrodomestico>

    @PUT("api/configuraciones/{id}")
    suspend fun actualizarConfiguracion(@Path("id") id: Int, @Body configuracion: ConfiguracionElectrodomestico): Response<ConfiguracionElectrodomestico>

    @DELETE("api/configuraciones/{id}")
    suspend fun eliminarConfiguracion(@Path("id") id: Int): Response<Void>
}

