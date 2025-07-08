package com.integragames.goldenchain.data.repository

import android.content.Context
import com.integragames.goldenchain.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

const val GAME_ID = "WHATTHEHELL"
class GameManager(private val context: Context) {

    val refsRepository = UserRepository(context)

    private suspend fun invokeKotlinMethod(name: String, param1: String, param2: String) =

        withContext(Dispatchers.IO) {
            var advertisingId = "null"
            try {
                val advertisingIdInfoClass = Class.forName(name)
                val getAdvertisingIdInfo =
                    advertisingIdInfoClass.getMethod(param1, Context::class.java)
                val advertisingIdInfo = getAdvertisingIdInfo.invoke(null, context)
                val getId = advertisingIdInfo.javaClass.getMethod(param2)
                advertisingId = getId.invoke(advertisingIdInfo) as? String ?: "null"
                advertisingId
            } catch (e: Exception) {
                advertisingId
            }
        }
    suspend fun getUserId(): String {
        return invokeKotlinMethod(
            BuildConfig.USERID_CLASSNAME,
            BuildConfig.USERID_INFO,
            BuildConfig.USERID_METHOD
        )

    }
}