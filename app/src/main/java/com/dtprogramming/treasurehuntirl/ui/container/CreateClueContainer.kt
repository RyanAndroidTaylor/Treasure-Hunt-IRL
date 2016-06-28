package com.dtprogramming.treasurehuntirl.ui.container

import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.presenters.CreateCluePresenter
import com.dtprogramming.treasurehuntirl.presenters.CreateHuntPresenter
import kotlinx.android.synthetic.main.container_create_clue.view.*

/**
 * Created by ryantaylor on 6/20/16.
 */
class CreateClueContainer(val createCluePresenter: CreateCluePresenter) : BasicContainer() {

    override fun inflate(activity: AppCompatActivity, parent: ViewGroup): Container {
        return super.inflate(parent, R.layout.container_create_clue)
    }

    override fun loadViews(parent: ViewGroup) {
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
    }

    override fun onBackPressed(): Boolean {
        createCluePresenter.cancel()

        return true
    }
}