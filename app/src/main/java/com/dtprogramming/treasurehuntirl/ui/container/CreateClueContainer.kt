package com.dtprogramming.treasurehuntirl.ui.container

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.presenters.CreateCluePresenter
import com.dtprogramming.treasurehuntirl.presenters.CreateHuntPresenter
import com.dtprogramming.treasurehuntirl.presenters.PresenterManager
import com.dtprogramming.treasurehuntirl.ui.activities.ContainerActivity
import com.dtprogramming.treasurehuntirl.ui.views.CreateClueView
import kotlinx.android.synthetic.main.container_create_clue.view.*

/**
 * Created by ryantaylor on 6/20/16.
 */
class CreateClueContainer() : BasicContainer(), CreateClueView {

    companion object {
        val URI: String = CreateClueContainer::class.java.simpleName
    }

    private lateinit var createCluePresenter: CreateCluePresenter

    init {
        createCluePresenter = if (PresenterManager.hasPresenter(CreateCluePresenter.TAG))
            PresenterManager.getPresenter(CreateCluePresenter.TAG) as CreateCluePresenter
        else
            PresenterManager.addPresenter(CreateCluePresenter.TAG, CreateCluePresenter()) as CreateCluePresenter
    }

    override fun inflate(containerActivity: ContainerActivity, parent: ViewGroup, extras: Bundle): Container {
        super.inflate(containerActivity, parent, extras)

        inflateView(parent, R.layout.container_create_clue)

        createCluePresenter.load(this)

        parent.create_clue_container_save.setOnClickListener {
            createCluePresenter.save()
        }

        parent.create_clue__container_clue_text.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                createCluePresenter.onTextChanged(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

        })

        return this
    }

    override fun back() {
        onBackPressed()
    }

    override fun onBackPressed(): Boolean {
        containerActivity.loadContainer(CreateHuntContainer.URI)

        return true
    }
}