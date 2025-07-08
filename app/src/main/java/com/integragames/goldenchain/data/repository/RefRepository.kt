package com.integragames.goldenchain.data.repository

import android.content.Context
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener


class UserRepository(private val context: Context) {

    fun getReferrer(callback: (String) -> Unit) {
        try {
            val referrerClient: InstallReferrerClient =
                InstallReferrerClient.newBuilder(context).build()
            referrerClient.startConnection(object : InstallReferrerStateListener {

                override fun onInstallReferrerSetupFinished(responseCode: Int) {
                    when (responseCode) {
                        InstallReferrerClient.InstallReferrerResponse.OK -> {
                            val res = referrerClient.installReferrer.installReferrer
                            callback(res)
                        }

                        else -> {
                            callback("null")
                        }
                    }
                    try {
                        referrerClient.endConnection()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onInstallReferrerServiceDisconnected() {
                    callback("null")
                }
            })
        } catch (e: Exception) {
            callback("null")
        }
    }

}