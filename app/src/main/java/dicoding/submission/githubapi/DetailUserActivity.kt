package dicoding.submission.githubapi

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import dicoding.submission.githubapi.adapter.ViewPagerAdapter
import dicoding.submission.githubapi.fragments.DetailUserFragment
import dicoding.submission.githubapi.fragments.FollowersListFragment
import dicoding.submission.githubapi.fragments.FollowingListFragment
import dicoding.submission.githubapi.models.User
import kotlinx.android.synthetic.main.activity_detailuser.*

class DetailUserActivity : AppCompatActivity() {

    companion object {
        val TAG = "DetailUserActivity"
        val USER = "user"
    }

    private lateinit var user: User
    var username: String? = null
    private lateinit var followingListFragment: FollowingListFragment
    private lateinit var followersListFragment: FollowersListFragment
    private lateinit var detailUserFragment: DetailUserFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailuser)

        val bundle = intent.extras
        user = bundle?.getParcelable(USER)!!
        username = user.login
        Log.d(TAG, "onCreate: " + user.login)

        toolbar.title = getString(R.string.title_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        detailUserFragment = DetailUserFragment.newInstance(user)
        followersListFragment = FollowersListFragment.newInstance(username.toString())
        followingListFragment = FollowingListFragment.newInstance(username.toString())

        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.addFragment(detailUserFragment, getString(R.string.title_detailuserfragment))
        viewPagerAdapter.addFragment(followersListFragment, getString(R.string.title_listfollower))
        viewPagerAdapter.addFragment(followingListFragment, getString(R.string.title_listfollowing))

        viewpager.adapter = viewPagerAdapter
        viewpager.offscreenPageLimit = viewPagerAdapter.count

        tablayout.setupWithViewPager(viewpager)
        var i = 0
        while (i < viewPagerAdapter.count) {
            tablayout.getTabAt(i)?.text = viewPagerAdapter.gettitle(i)
            i++
        }
    }

}