package dicoding.submission.githubapi.consumerapp

import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import dicoding.submission.githubapi.consumerapp.models.CrudState
import dicoding.submission.githubapi.consumerapp.models.User
import dicoding.submission.githubapi.consumerapp.providers.Config
import dicoding.submission.githubuserapp.adapter.UserAdapter
import kotlinx.android.synthetic.main.activity_main_cons.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class MainConsActivity : AppCompatActivity() {

    companion object {
        val TAG = "MainActivity"
    }

    private var userList = ArrayList<User>()
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_cons)

        toolbar.title = getString(R.string.title_main)
        setSupportActionBar(toolbar)

        userAdapter = UserAdapter(this, userList, object : UserAdapter.Listener {
            override fun callback(user: User, crudState: CrudState) {
                Log.d(TAG, user.login.toString())
                //if (crudState == CrudState.READ) goto_detailuser(user)
            }
        })

        recyclerview.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        recyclerview.layoutManager = layoutManager
        recyclerview.adapter = userAdapter

        poppulate_data()
    }

    private fun poppulate_data() {
        val handlerThread = HandlerThread("observer")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        try {
            contentResolver.registerContentObserver(Config.USER_CONTENT_URI, true, object :
                ContentObserver(handler) {
                override fun onChange(self: Boolean) {
                    start_load_user()
                }
            })

            start_load_user()
        } catch (e: SecurityException) {
            e.printStackTrace()
            update_ui()
            txt_error.text = getString(R.string.err_content_provider)
        }

    }

    private fun start_load_user() {
        GlobalScope.launch(Dispatchers.Main) {
            val users = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(Config.USER_CONTENT_URI,
                    null, null, null, null)
                cursorToArrayList(cursor)
            }
            userList = users.await()
            update_ui()
        }
    }

    private fun update_ui() {
        userAdapter.setData(userList)
        view_error.visibility = if (userList.size < 1) View.VISIBLE else View.GONE
        txt_error.text = if (userList.size < 1) getString(R.string.err_empty) else ""
    }

    private fun cursorToArrayList(cursor: Cursor?): ArrayList<User> {
        val users: ArrayList<User> = ArrayList()
        if (cursor != null && cursor.count > 0) {
            cursor.apply {
                while (moveToNext()) {
                    val user = User()
                    user.id =
                        cursor.getString(cursor.getColumnIndexOrThrow(Config.USER_COLUMN_ID))
                            .toInt()
                    user.login =
                        cursor.getString(cursor.getColumnIndexOrThrow(Config.USER_COLUMN_LOGIN))
                    user.htmlUrl =
                        cursor.getString(cursor.getColumnIndexOrThrow(Config.USER_COLUMN_HTMLURL))
                    user.avatarUrl =
                        cursor.getString(cursor.getColumnIndexOrThrow(Config.USER_COLUMN_AVATARURL))
                    users.add(user)
                }
            }
        } else {
            Log.d(TAG, "cursorToArrayList: cursor is null")
        }
        return users
    }
}
