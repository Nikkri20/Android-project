package com.example.android_project.receiver

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import com.example.android_project.ui.screens.setAlarm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d("BootReceiver", "Устройство перезагружено. Проверяем разрешения.")

            if (context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                Log.e("BootReceiver", "Разрешение на уведомления отсутствует.")
                return
            }

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val reminderTime = "08:00" // Замените на сохраненное значение
                    val profileName = "Default Profile" // Замените на сохраненное значение
                    setAlarm(context, reminderTime, profileName)
                    Log.d("BootReceiver", "Будильник восстановлен для $profileName")
                } catch (e: Exception) {
                    Log.e("BootReceiver", "Ошибка восстановления будильника: ${e.message}")
                }
            }
        }
    }
}
