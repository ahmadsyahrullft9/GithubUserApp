package dicoding.submission.githubapi.reminder

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import dicoding.submission.githubapi.MainActivity
import dicoding.submission.githubapi.R
import java.time.LocalTime
import java.util.*

class ReminderReceiver : BroadcastReceiver() {

    var time_reminder: String
    var notifiTag: String
    var notifiId: Int
    var channelId: String
    var channerName: String
    var notifTittle: String
    var notifMessage: String

    init {
        time_reminder = Config.TIME_REMINDER
        notifiTag = Config.NOTIFICATION_TAG
        notifiId = Config.NOTIFICATION_ID
        channelId = Config.CHANNEL_ID
        channerName = Config.CHANNER_NAME
        notifTittle = Config.REMINDER_TITLE
        notifMessage = Config.REMINDER_MESSAGE
    }

    fun isRunning(context: Context): Boolean {
        val pendingIntent = Intent(context, ReminderReceiver::class.java)
        return PendingIntent.getBroadcast(context, notifiId, pendingIntent, PendingIntent.FLAG_NO_CREATE) != null
    }

    fun setAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)
        val timeInMillis: Long = getTimeInMilis(time_reminder)
        val pendingIntent = PendingIntent.getBroadcast(context, notifiId, intent, 0)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
    }

    fun destroy(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, notifiId, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
    }

    private fun getTimeInMilis(timeStr: String): Long {
        //https://www.ict.social/kotlin/oop/date-and-time-in-kotlin-parsing-and-comparing
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val time = LocalTime.parse(timeStr)
            val cal: Calendar = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, time.hour)
            cal.set(Calendar.MINUTE, time.minute)
            cal.set(Calendar.SECOND, time.second)
            Log.d(notifiTag, "setAlarm: " + time.hour + ":" + time.minute + ":" + time.second)
            return cal.timeInMillis
        } else {
            val cal: Calendar = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeStr.subSequence(0, 2).toString()))
            cal.set(Calendar.MINUTE, Integer.parseInt(timeStr.subSequence(4, 6).toString()))
            cal.set(Calendar.SECOND, Integer.parseInt(timeStr.subSequence(8, 10).toString()))
            return cal.timeInMillis
        }
    }

    @SuppressLint("ServiceCast")
    override fun onReceive(p0: Context?, p1: Intent?) {
        val intent = Intent(p0, MainActivity::class.java)
        val appIntent =
            PendingIntent.getActivity(p0, notifiId, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val nmc = p0?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notifBuilder: NotificationCompat.Builder = NotificationCompat.Builder(p0, channelId)
            .setContentText(notifMessage)
            .setContentTitle(notifTittle)
            .setAutoCancel(true)
            .setWhen(getTimeInMilis(time_reminder))
            .setContentIntent(appIntent)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
            .setSmallIcon(R.mipmap.ic_launcher)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channerName, NotificationManager.IMPORTANCE_DEFAULT)
            notifBuilder.setChannelId(channelId)
            nmc.createNotificationChannel(channel)
        }

        nmc.notify(notifiTag, notifiId, notifBuilder.build())
    }
}