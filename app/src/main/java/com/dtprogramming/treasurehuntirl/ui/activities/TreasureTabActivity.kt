package com.dtprogramming.treasurehuntirl.ui.activities

import android.os.Bundle
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.ui.fragments.CreateHuntTabFragment
import com.dtprogramming.treasurehuntirl.ui.fragments.CurrentHuntsFragment
import com.dtprogramming.treasurehuntirl.ui.fragments.TreasureHuntListFragment
import com.dtprogramming.treasurehuntirl.ui.view_pager.ViewPagerAdapter
import kotlinx.android.synthetic.main.app_bar_treasure_tab.*
import kotlinx.android.synthetic.main.content_treasure_tab.*

class TreasureTabActivity : NavDrawActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treasure_tab)
        setupDrawer()

        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.addFragment(TreasureHuntListFragment())
        viewPagerAdapter.addFragment(CurrentHuntsFragment())
        viewPagerAdapter.addFragment(CreateHuntTabFragment())

        treasure_tab_activity_view_pager.adapter = viewPagerAdapter

        treasure_tab_activity_sliding_tab.setDistributeEvenly(true)
        treasure_tab_activity_sliding_tab.setCustomTabView(R.layout.sliding_tab_layout, R.id.sliding_tab_title)
        treasure_tab_activity_sliding_tab.setCustomTabColorizer {
            // We support version 16 and this is not deprecated tell 23
            @Suppress("DEPRECATION")
            resources.getColor(R.color.colorAccent)
        }

        treasure_tab_activity_sliding_tab.setViewPager(treasure_tab_activity_view_pager)

        toolbar?.title = resources.getString(R.string.app_name)
    }
}
