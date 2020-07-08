package dicoding.submission.githubapi.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dicoding.submission.githubapi.FavoritActivity
import dicoding.submission.githubapi.R
import dicoding.submission.githubapi.models.DetailUser
import dicoding.submission.githubapi.models.NetState
import dicoding.submission.githubapi.models.NetStatus
import dicoding.submission.githubapi.models.User
import dicoding.submission.githubapi.viewmodels.UserFavViewModel
import dicoding.submission.githubapi.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.fragment_detailuser.*

class DetailUserFragment : Fragment() {

    companion object {
        val TAG = "DetailUserFragment"
        val USER = "user"

        fun newInstance(user: User): DetailUserFragment {
            val instance = DetailUserFragment()
            instance.arguments = Bundle().apply {
                putParcelable(USER, user)
            }
            return instance
        }
    }

//    private lateinit var userfav: User
//    private lateinit var user: User

    var user: User? = null
    var userfav: User? = null

    private lateinit var detailUser: DetailUser
    private lateinit var userViewModel: UserViewModel
    private lateinit var userFavViewModel: UserFavViewModel
    private lateinit var netState: NetState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = arguments?.getParcelable(USER)!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detailuser, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        userFavViewModel = ViewModelProviders.of(this).get(UserFavViewModel::class.java)

        userViewModel.detailUser(user?.login.toString())
        userFavViewModel.search_username(user?.id!!)

        userViewModel.userDetailLiveData.observe(this, Observer {
            detailUser = it
            update_ui()
        })
        userViewModel.networkState.observe(this, Observer {
            netState = it
            update_loading()
        })
        userFavViewModel.getAllUserFavs().observe(this, Observer {
            val users = it
            if (users.size > 0) {
                users.forEach {
                    if (it.id == user!!.id) {
                        userfav = it
                        return@forEach
                    }
                }
            }
            update_fav()
        })
        fab_favorite.setOnClickListener {
            if (userfav?.id != user?.id) {
                user?.let { it1 -> userFavViewModel.insert(it1) }
                userfav = user
                Snackbar.make(it, "berhasil ditambahkan ke daftar favorit", Snackbar.LENGTH_SHORT)
                    .setAction("Lihat") {
                        goto_favuser()
                    }.show()
            } else {
                user?.let { it1 -> userFavViewModel.delete(it1) }
                userfav = null
                Snackbar.make(it, "berhasil dihapus dari daftar favorit", Snackbar.LENGTH_SHORT)
                    .setAction("Lihat") {
                        goto_favuser()
                    }.show()
            }
            userFavViewModel.search_username(user?.id!!)
            update_fav()
        }
    }

    private fun goto_favuser() {
        val fav_intent = Intent(activity, FavoritActivity::class.java)
        startActivity(fav_intent)
        activity?.finish()
    }

    private fun update_fav() {
        val isFavorit = userfav?.id == user?.id
        Log.d(TAG, "onViewCreated: " + isFavorit)
        if (isFavorit) {
            fab_favorite.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            fab_favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    private fun update_loading() {
        progress_bar.visibility = if (netState == NetState.LOADING) View.VISIBLE else View.GONE
        if (netState == NetState.ERROR || netState.status == NetStatus.FAILED) {
            txt_error.text = netState.message
            view_error.visibility = View.VISIBLE
            iv_avatar.visibility = View.GONE
            view_detail.visibility = View.GONE
            fab_favorite.visibility = View.GONE
        } else {
            view_detail.visibility = if (netState == NetState.LOADING) View.GONE else View.VISIBLE
            iv_avatar.visibility = View.VISIBLE
            view_error.visibility = View.GONE
            fab_favorite.visibility = if (netState == NetState.LOADING) View.GONE else View.VISIBLE
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