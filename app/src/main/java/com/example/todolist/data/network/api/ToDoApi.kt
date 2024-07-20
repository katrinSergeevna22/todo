package com.example.todolist.data.network.api

import com.example.todolist.data.network.dto.request.TodoItemListRequestForJson
import com.example.todolist.data.network.dto.request.TodoItemRequestForJson
import com.example.todolist.data.network.dto.response.TodoItemListResponseForJson
import com.example.todolist.data.network.dto.response.TodoItemResponseForJson
import dagger.Binds
import retrofit2.http.*
import java.util.*

interface ToDoApi {
    @GET("list")
    suspend fun getTasks(
        @Header("Authorization") token: String
    ): TodoItemListResponseForJson

    @PATCH("list")
    suspend fun patchTasks(
        @Header("Authorization") token: String,
        @Header("X-Last-Known-Revision") header: Int,
        @Body body: TodoItemListRequestForJson
    ): TodoItemListResponseForJson

    @GET("list/{id}")
    suspend fun getTask(
        @Header("Authorization") token: String,
        @Path("id") id: UUID
    ): TodoItemResponseForJson

    @POST("list")
    suspend fun postTask(
        @Header("Authorization") token: String,
        @Header("X-Last-Known-Revision") header: Int,
        @Body body: TodoItemRequestForJson
    ): TodoItemResponseForJson

    @PUT("list/{id}")
    suspend fun putTask(
        @Header("Authorization") token: String,
        @Header("X-Last-Known-Revision") header: Int,
        @Path("id") id: UUID,
        @Body body: TodoItemRequestForJson
    ): TodoItemResponseForJson

    @DELETE("list/{id}")
    suspend fun deleteTask(
        @Header("Authorization") token: String,
        @Header("X-Last-Known-Revision") header: Int,
        @Path("id") id: UUID
    ): TodoItemResponseForJson
}