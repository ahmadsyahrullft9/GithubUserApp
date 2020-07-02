package dicoding.submission.githubapi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dicoding.submission.githubapi.adapter.ViewPagerAdapter
import dicoding.submission.githubapi.fragments.DetailUserFragment
import dicoding.submission.githubapi.fragments.FollowersListFragment
import dicoding.submission.githubapi.fragments.FollowingListFragment
import kotlinx.android.synthetic.main.activity_detailuser.*

class DetailUserActivity : AppCompatActivity() {

    companion object {
        val TAG = "DetailUserActivity"
        val USERNAME = "username"
    }

    var username: String? = null
    private lateinit var followingListFragment: FollowingListFragment
    private lateinit var followersListFragment: FollowersListFragment
    private lateinit var detailUserFragment: DetailUserFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailuser)

        val bundle = intent.extras
        username = bundle?.getString(USERNAME)

        toolbar.title = getString(R.string.title_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        detailUserFragment = DetailUserFragment.newInstance(username.toString())
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