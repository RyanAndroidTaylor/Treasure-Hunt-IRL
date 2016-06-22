package com.dtprogramming.treasurehuntirl.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.presenters.CreateHuntPresenter
import com.dtprogramming.treasurehuntirl.presenters.PresenterManager
import com.dtprogramming.treasurehuntirl.ui.container.Container
import com.dtprogramming.treasurehuntirl.ui.container.CreateClueContainer
import com.dtprogramming.treasurehuntirl.ui.container.CreateHuntContainer
import com.dtprogramming.treasurehuntirl.ui.views.CreateHuntView
import kotlinx.android.synthetic.main.activity_create_hunt.*
import java.util.*

/**
 * Created by ryantaylor on 6/15/16.
 */
class CreateHuntActivity : BaseActivity(), CreateHuntView {

    private lateinit var createHuntPresenter: CreateHuntPresenter
    private lateinit var container: Container

    companion object {
        val HUNT_UUID = "HuntUuid"

        fun getIntent(context: Context): Intent {
            return getIntent(context, UUID.randomUUID().toString().replace("-", ""))
        }

        fun getIntent(context: Context, treasureHuntId: String): Intent {
            val intent = Intent(context, CreateHuntActivity::class.java)

            intent.putExtra(HUNT_UUID, treasureHuntId)

            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_hunt)

        toolbar?.title = resources.getString(R.string.create_hunt_activity_title)

        createHuntPresenter = if (PresenterManager.hasPresenter(CreateHuntPresenter.TAG))
            PresenterManager.getPresenter(CreateHuntPresenter.TAG) as CreateHuntPresenter
        else
            PresenterManager.addPresenter(CreateHuntPresenter.TAG, CreateHuntPresenter()) as CreateHuntPresenter

        if (savedInstanceState != null) {
            createHuntPresenter.load(this)
        } else {
            createHuntPresenter.load(intent.getStringExtra(HUNT_UUID), this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (isFinishing)
            createHuntPresenter.finish()
    }

    override fun loadCreateClueContainer() {
        container = CreateClueContainer(createHuntPresenter)

        container.inflate(activity_create_hunt_container)
    }

    override fun loadCreateHuntContainer(clues: List<String>) {
        container = CreateHuntContainer(createHuntPresenter, clues)

        container.inflate(activity_create_hunt_container)
    }

    override fun updateClueList(clues: List<String>) {
        if (container is CreateHuntContainer)
            (container as CreateHuntContainer).updateClueList(clues)
    }

    override fun onBackPressed() {
        if (!container.onBackPressed())
            super.onBackPressed()
    }
}