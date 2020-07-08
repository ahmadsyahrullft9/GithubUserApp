package dicoding.submission.githubapi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import dicoding.submission.githubapi.models.CrudState
import dicoding.submission.githubapi.models.User
import dicoding.submission.githubapi.viewmodels.UserFavViewModel
import dicoding.submission.githubuserapp.adapter.UserAdapter
import kotlinx.android.synthetic.main.activity_favorit.*

class FavoritActivity : AppCompatActivity() {

    private lateinit var userAdapter: UserAdapter
    private lateinit var userFavViewModel: UserFavViewModel

    private var userList = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorit)

        toolbar.title = getString(R.string.title_favorit)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        userAdapter = UserAdapter(this, userList, object : UserAdapter.Listener {
            override fun callback(user: User, crudState: CrudState) {
                Log.d(MainActivity.TAG, user.login.toString())
                if (crudState == CrudState.READ) goto_detailuser(user)
            }
        })

        recyclerview.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        recyclerview.layoutManager = layoutManager
        recyclerview.adapter = userAdapter

        userFavViewModel = ViewModelProviders.of(this).get(UserFavViewModel::class.java)

        userFavViewModel.getAllUserFavs().observe(this, Observer {
            userList = it as ArrayList<User>
            update_ui()
        })
    }

    private fun update_ui() {
        userAdapter.setData(userList)
        view_error.visibility = if (userList.size < 1) View.VISIBLE else View.GONE
        txt_error.text = if (userList.size < 1) getString(R.string.err_empty) else ""
    }

    private fun goto_detailuser(user: User) {
        val detail_intent = Intent(this@FavoritActivity, DetailUserActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable(DetailUserActivity.USER, user)
        detail_intent.putExtras(bundle)
        startActivity(detail_intent)
    }
}