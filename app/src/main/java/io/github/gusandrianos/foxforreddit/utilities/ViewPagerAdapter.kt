package io.github.gusandrianos.foxforreddit.utilities

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(val fragments: ArrayList<Fragment>,
                       private val tabTitles: ArrayList<String>,
                       fragment: Fragment)
    : FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    override fun getItemCount(): Int {
        return fragments.size
    }

    fun getFragmentTitle(position: Int): String {
        return tabTitles[position]
    }
}