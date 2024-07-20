package com.example.todolist.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ISettingRepository {

    fun getRevision(): Int

    fun setRevision(revision: Int)

    fun getUsername(): String

    fun getToken(): String

    fun getNotificationEnabled(): Boolean

    fun getFlowNotificationEnabled(): StateFlow<Boolean>

    fun setNotificationEnabled(mode: Boolean)
}