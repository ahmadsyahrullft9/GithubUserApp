package dicoding.submission.githubapi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import dicoding.submission.githubapi.models.CrudState
import dicoding.submission.githubapi.models.User
import dicoding.submission.githubapi.models.NetState
import dicoding.submission.githubapi.models.NetStatus
import dicoding.submission.githubapi.viewmodels.UserViewModel
import dicoding.submission.githubuserapp.adapter.UserAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        val TAG = "MainActivity"
    }

    private lateinit var userAdapter: UserAdapter
    private lateinit var userViewModel: UserViewModel

    var searchView: SearchView? = null

    private var userList = ArrayList<User>()
    private lateinit var netState: NetState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.title = getString(R.string.title_main)
        setSupportActionBar(toolbar)

        userAdapter = UserAdapter(this, userList, object : UserAdapter.Listener {
            override fun callback(user: User, crudState: CrudState) {
                Log.d(TAG, user.login.toString())
                if (crudState == CrudState.READ) goto_detailuser(user)
            }
        })

        recyclerview.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        recyclerview.layoutManager = layoutManager
        recyclerview.adapter = userAdapter

        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

        //data from network
        userViewModel.userListLiveData.observe(this, Observer {
            userList = it as ArrayList<User>
            userAdapter.setData(userList)
        })

        //menampilkan loading, error, dll
        userViewModel.networkState.observe(this, Observer {
            netState = it
            update_loading()
        })

        userViewModel.getUser()
    }

    private fun update_loading() {
        progress_bar.visibility = if (netState == NetState.LOADING) View.VISIBLE else View.GONE
        if (netState == NetState.ERROR || netState.status == NetStatus.FAILED) {
            txt_error.text = netState.message
            view_error.visibility = View.VISIBLE
        } else {
            view_error.visibility = View.GONE
        }
    }

    private fun goto_detailuser(user:User) {
        val detail_intent = Intent(this@MainActivity, DetailUserActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable(DetailUserActivity.USER, user)
        detail_intent.putExtras(bundle)
        startActivity(detail_intent)
    }

    private fun goto_favuser() {
        val fav_intent = Intent(this@MainActivity, FavoritActivity::class.java)
        startActivity(fav_intent)
    }

    private fun goto_setting() {
        val set_intent = Intent(this@MainActivity, SettingReminderActivity::class.java)
        startActivity(set_intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)

        searchView = menu!!.findItem(R.id.search_menu_item).actionView as SearchView
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                userViewModel.searchUser(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        searchView!!.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                userViewModel.getUser()
                return false
            }

        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.fav_menu_item) {
            goto_favuser()
            return true
        }else if(item.itemId == R.id.set_menu_item){
            goto_setting()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (!searchView!!.isIconified)
            searchView!!.isIconified = true
        else
            super.onBackPressed()
    }
}
