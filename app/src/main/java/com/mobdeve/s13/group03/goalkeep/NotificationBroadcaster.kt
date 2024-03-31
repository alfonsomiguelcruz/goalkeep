package com.mobdeve.s13.group03.goalkeep

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationBroadcaster : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val builder = NotificationCompat.Builder(context, "notifId")
            .setSmallIcon(R.drawable.goalkeep_icon)
            .setContentTitle("GoalKeep")
            .setContentText(intent.getStringExtra("NOTIF_MSG"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notifManager = NotificationManagerCompat.from(context)

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notifManager.notify(1, builder.build())
    }
}