package com.rice.base

import android.annotation.SuppressLint
import android.app.*
import android.app.Notification.FLAG_AUTO_CANCEL
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log

import com.orhanobut.logger.Logger
import com.rice.tool.TimeUtils

import org.java_websocket.client.WebSocketClient
import org.java_websocket.drafts.Draft_6455
import org.java_websocket.handshake.ServerHandshake

import java.net.URI
import java.net.URISyntaxException
import java.util.*
import kotlin.collections.ArrayList

/**
 * 作者：张雪涛
 * 时间：2019/1/27 0027:9:26
 * 邮箱：1574507001@qq.com
 * 说明：Socket服务Base
 *      不要直接继承这个类，此类扩展性太差，建议复制一份然后重写TODO。
 */
@SuppressLint("Registered")
class BaseSocketService : Service() {

    var context: Context? = null
    var isFirst = true
    var lastTime: Long = 0//最后一条消息发送的时间
    lateinit var thisBinder: SocketBinder

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
//            if (!isFirst) {
//                isFirst = intent.getBooleanExtra("isFirst", false)
//            }
            connetToServer()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun stopService(name: Intent): Boolean {
        Log.d("---MessageService---", "stopService")
        return super.stopService(name)
    }

    override fun onDestroy() {
        Log.d("---MessageService---", "onDestroy")
        super.onDestroy()
    }

    var timer = Timer()
    var timerTask = object : TimerTask() {
        override fun run() {
            if (TimeUtils.getNowTimestamp() - lastTime > 30000) {//距离最后一条消息发送超过3秒
                try {
                    webSocketClient?.send("保持连接")
                    Logger.d("保持连接")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    @SuppressLint("LongLogTag")
    private fun connetToServer() {
        try {
            webSocketClient = object : WebSocketClient(URI(""), object : Draft_6455() {}, null, 10000) {
                override fun onOpen(handshakedata: ServerHandshake) {
                    Logger.d("---opOpen---" + handshakedata.httpStatusMessage)
                    if (webSocketClient != null && webSocketClient!!.isOpen) {
                        if (!isFirst) {
                            //TODO:首次连接时进行的操作
                        }
                        try {
                            send("")
                        } catch (e: Exception) {
                            e.printStackTrace()//如果服务器主动断开连接此处会报错
                        }
                        lastTime = TimeUtils.getNowTimestamp()//记录最后一条消息发送的时间
                    }
                }

                override fun onMessage(message: String) {
                    Logger.d("---onMessage---$message")
                    if (isFirst) {
                        //定时发送消息保持连接
                        try {
                            timer.schedule(timerTask, 30000, 0)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    isFirst = false
                    //TODO:收到消息进行的操作
                }

                override fun onClose(code: Int, reason: String, remote: Boolean) {
                    webSocketClient = null
                    timer.cancel()
                    isFirst = true
                    connetToServer()
                    Logger.d(
                            "---onClose---code:  " + code.toString() +
                                    "  reason:  " + reason
                    )
                }

                override fun onClosing(code: Int, reason: String?, remote: Boolean) {
                    webSocketClient = null
                    Logger.d(
                            "---onClosing---code:  " + code.toString() +
                                    "  reason:  " + reason
                    )
                    super.onClosing(code, reason, remote)
                }

                override fun onError(e: Exception) {
                    Logger.e("---onError---", e)
                    e.printStackTrace()
                    webSocketClient = null
                }
            }
        } catch (e: URISyntaxException) {
            Logger.e("---URISyntaxException---", e)
            e.printStackTrace()
        }
        webSocketClient!!.connect()
    }

    override fun onBind(intent: Intent): IBinder? {
        return thisBinder
    }

    interface OnRepackListener {
        fun onRepack(messageJson: String)
    }

    inner class SocketBinder : Binder() {

        val messageService = this@BaseSocketService

    }

    companion object {
        val ACTION = "com.rice.base.BaseSocketService"//TODO:替换为正式项目包名
        private var webSocketClient: WebSocketClient? = null
        var isFirst = true//首次连接
    }

}
