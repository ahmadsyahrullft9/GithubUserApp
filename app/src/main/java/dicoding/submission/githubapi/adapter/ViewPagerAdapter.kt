package dicoding.submission.githubapi.adapter


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.*

class ViewPagerAdapter(manager: FragmentManager) :
    FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    internal var fragments = ArrayList<Fragment>()
    var tittle_fragment = ArrayList<String>()
        internal set

    fun addFragment(fragments: Fragment, title: String) {
        this.fragments.add(fragments)
        this.tittle_fragment.add(title)
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    fun gettitle(posisi: Int): String {
        return tittle_fragment[posisi]
    }
}
