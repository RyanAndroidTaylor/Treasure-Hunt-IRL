package com.dtprogramming.treasurehuntirl.ui.view_pager

import java.util.ArrayList

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.dtprogramming.treasurehuntirl.ui.fragments.TabFragment

class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    private val mFragments = ArrayList<TabFragment>()

    override fun getItem(position: Int): Fragment {
        return mFragments[position]
    }

    override fun getCount(): Int {
        return mFragments.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mFragments[position].title
    }

    fun addFragment(fragment: TabFragment) {
        mFragments.add(fragment)
    }
}
