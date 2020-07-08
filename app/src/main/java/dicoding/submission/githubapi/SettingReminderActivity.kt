package dicoding.submission.githubapi

import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dicoding.submission.githubapi.reminder.ReminderReceiver
import kotlinx.android.synthetic.main.activity_settingreminder.*

class SettingReminderActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

    private lateinit var reminderReceiver: ReminderReceiver
    var isReminderRunning: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settingreminder)

        toolbar.setTitle(getString(R.string.title_setting))
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        reminderReceiver = ReminderReceiver()
        switch_reminder.setOnCheckedChangeListener(this)

        update_ui()
    }

    private fun update_ui() {
        isReminderRunning = reminderReceiver.isRunning(this)
        switch_reminder.isChecked = isReminderRunning
    }

    override fun onCheckedChanged(p0: CompoundButton?, check: Boolean) {
        if (check) {
            if (!reminderReceiver.isRunning(this@SettingReminderActivity)) {
                reminderReceiver.setAlarm(this@SettingReminderActivity)
                Toast.makeText(this@SettingReminderActivity, "alarm reminder aktif", Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            if (reminderReceiver.isRunning(this@SettingReminderActivity)) {
                reminderReceiver.destroy(this)
                Toast.makeText(this@SettingReminderActivity, "alarm reminder nonaktif", Toast.LENGTH_LONG)
                    .show()
            }
        }
        update_ui()
    }
}