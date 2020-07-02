package dicoding.submission.githubapi.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import dicoding.submission.githubapi.R
import dicoding.submission.githubapi.models.User
import dicoding.submission.githubapi.network.NetState
import dicoding.submission.githubapi.network.NetStatus
import dicoding.submission.githubapi.viewmodels.UserViewModel
import dicoding.submission.githubuserapp.adapter.UserAdapter
import kotlinx.android.synthetic.main.fragment_followers.*

class FollowersListFragment : Fragment() {

    companion object {
        val TAG = "FollowersListFragment"
        val USERNAME = "username"

        fun newInstance(username: String): FollowersListFragment {
            val instance = FollowersListFragment()
            val args = Bundle()
            args.putString(USERNAME, username)
            instance.arguments = args
            return instance
        }
    }

    var username: String? = null
    private var userList = ArrayList<User>()
    private lateinit var userViewModel: UserViewModel
    private lateinit var userAdapter: UserAdapter
    private lateinit var netState: NetState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        username = arguments?.getString(DetailUserFragment.USERNAME)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userAdapter = UserAdapter(activity, userList, object : UserAdapter.Listener {
            override fun detail_user(user: User) {
                Log.d(TAG, user.login.toString())
                //ke halaman detail user
                //goto_detailuser(user.login.toString())
            }
        })

        recyclerview.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        recyclerview.layoutManager = layoutManager
        recyclerview.adapter = userAdapter

        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        userViewModel.userListLiveData.observe(this, Observer {
            userList = it as ArrayList<User>
            userAdapter.setData(userList)
        })
        userViewModel.networkState.observe(this, Observer {
            netState = it
            update_loading()
        })

        userViewModel.getFollowers(username.toString())
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
}