package dicoding.submission.githubapi.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class TimeChangeReceiver : BroadcastReceiver() {

    val TAG = "TimeChangeReceiver"

    override fun onReceive(p0: Context?, p1: Intent?) {
        val action: String = p1?.action.toString()
        if (action == Intent.ACTION_TIME_CHANGED ||
            action == Intent.ACTION_TIMEZONE_CHANGED ||
            action == Intent.ACTION_DATE_CHANGED) {
            val reminderReceiver = ReminderReceiver()
            //Toast.makeText(p0, "OnTimeChange", Toast.LENGTH_LONG).show()
            //reminderReceiver.print_logtime(System.currentTimeMillis(), "onChangeTime")

            if (reminderReceiver.isRunning(p0!!)) {
                reminderReceiver.destroy(p0)
                reminderReceiver.setAlarm(p0)
            }
        }
    }

}