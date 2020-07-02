package dicoding.submission.githubapi.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import dicoding.submission.githubapi.R
import dicoding.submission.githubapi.models.DetailUser
import dicoding.submission.githubapi.network.NetState
import dicoding.submission.githubapi.network.NetStatus
import dicoding.submission.githubapi.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.fragment_detailuser.*

class DetailUserFragment : Fragment() {

    companion object {
        val TAG = "DetailUserFragment"
        val USERNAME = "username"

        fun newInstance(username: String): DetailUserFragment {
            val instance = DetailUserFragment()
            val args = Bundle()
            args.putString(USERNAME, username)
            instance.arguments = args
            return instance
        }
    }

    var username: String? = null
    private lateinit var detailUser: DetailUser
    private lateinit var userViewModel: UserViewModel
    private lateinit var netState: NetState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        username = arguments?.getString(USERNAME)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detailuser, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        userViewModel.userDetailLiveData.observe(this, Observer {
            detailUser = it
            update_ui()
        })
        userViewModel.networkState.observe(this, Observer {
            netState = it
            update_loading()
        })

        userViewModel.detailUser(username.toString())
    }

    private fun update_loading() {
        progress_bar.visibility = if (netState == NetState.LOADING) View.VISIBLE else View.GONE
        if (netState == NetState.ERROR || netState.status == NetStatus.FAILED) {
            txt_error.text = netState.message
            view_error.visibility = View.VISIBLE
            iv_avatar.visibility = View.GONE
            view_detail.visibility = View.GONE
        } else {
            view_detail.visibility = if (netState == NetState.LOADING) View.GONE else View.VISIBLE
            iv_avatar.visibility = View.VISIBLE
            view_error.visibility = View.GONE
        }
    }

    private fun update_ui() {
        Glide.with(this)
            .load(detailUser.avatarUrl)
            .into(iv_avatar)

        txt_repo.text = detailUser.publicRepos.toString()
        txt_following.text = detailUser.following.toString()
        txt_follower.text = detailUser.followers.toString()

        txt_fullname.text = detailUser.name
        txt_username.text = detailUser.login
        txt_company.text = detailUser.company
        txt_location.text = detailUser.location
    }
}