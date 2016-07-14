package com.dtprogramming.treasurehuntirl.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.ui.container.CreateHuntContainer
import com.dtprogramming.treasurehuntirl.util.HUNT_UUID
import com.dtprogramming.treasurehuntirl.util.NEW
import kotlinx.android.synthetic.main.activity_create_hunt.*

/**
 * Created by ryantaylor on 6/15/16.
 */
class CreateHuntActivity : ContainerActivity() {

    override lateinit var parent: ViewGroup

    companion object {
        fun getCreateNewIntent(context: Context): Intent {
            val intent = Intent(context, CreateHuntActivity::class.java)

            intent.putExtra(NEW, true)

            return intent
        }

        fun getLoadIntent(context: Context, treasureHuntId: String): Intent {
            val intent = Intent(context, CreateHuntActivity::class.java)

            intent.putExtra(HUNT_UUID, treasureHuntId)

            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_hunt)

        parent = activity_create_hunt_container

        if (savedInstanceState != null && savedInstanceState.containsKey(CURRENT_URI)) {
            loadContainer(savedInstanceState.getString(CURRENT_URI))
        }
        else {
            val extras = Bundle()

            if (intent.hasExtra(HUNT_UUID))
                extras.putString(HUNT_UUID, intent.getStringExtra(HUNT_UUID))
            if (intent.hasExtra(NEW))
                extras.putBoolean(NEW, intent.getBooleanExtra(NEW, true))

            loadContainer(CreateHuntContainer.URI, extras)
        }

        toolbar?.title = stringFrom(R.string.treasure_hunt_action_bar_title)
    }

    override fun setToolBarTitle(title: String) {
        toolbar?.title = title
    }
}