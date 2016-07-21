package com.dtprogramming.treasurehuntirl.ui.container

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ViewGroup
import android.widget.EditText
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.database.connections.impl.ClueConnectionImpl
import com.dtprogramming.treasurehuntirl.presenters.CreateCluePresenter
import com.dtprogramming.treasurehuntirl.presenters.PresenterManager
import com.dtprogramming.treasurehuntirl.ui.activities.ContainerActivity
import com.dtprogramming.treasurehuntirl.ui.views.CreateClueView
import com.dtprogramming.treasurehuntirl.util.PARENT_UUID
import kotlinx.android.synthetic.main.container_create_clue.view.*

/**
 * Created by ryantaylor on 6/20/16.
 */
class CreateClueContainer() : BasicContainer(), CreateClueView {

    companion object {
        val URI: String = CreateClueContainer::class.java.simpleName
    }

    override var rootViewId = R.layout.container_create_clue

    private lateinit var createCluePresenter: CreateCluePresenter

    private lateinit var clueText: EditText

    init {
        createCluePresenter = if (PresenterManager.hasPresenter(CreateCluePresenter.TAG))
            PresenterManager.getPresenter(CreateCluePresenter.TAG) as CreateCluePresenter
        else
            PresenterManager.addPresenter(CreateCluePresenter.TAG, CreateCluePresenter(ClueConnectionImpl())) as CreateCluePresenter
    }

    override fun inflate(containerActivity: ContainerActivity, parent: ViewGroup, extras: Bundle): Container {
        super.inflate(containerActivity, parent, extras)
        containerActivity.setToolBarTitle(containerActivity.stringFrom(R.string.clue_action_bar_title))

        clueText = parent.create_clue__container_clue_text

        if (extras.containsKey(PARENT_UUID))
            createCluePresenter.load(this, extras.getString(PARENT_UUID))
        else
            createCluePresenter.reload(this)

        parent.create_clue_container_save.setOnClickListener {
            createCluePresenter.save()
        }

        clueText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                createCluePresenter.onTextChanged(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

        })

        return this
    }

    override fun onPause() {
        Log.i("CreateClueContainer", "onPause()")
        super.onPause()

        createCluePresenter.unsubscribe()
    }

    override fun onReload(parent: ViewGroup) {
        super.onReload(parent)

        createCluePresenter.reload(this)
    }

    override fun onFinish() {
        Log.i("CreateClueContainer", "onFinish()")
        super.onFinish()

        createCluePresenter.finish()
    }

    override fun setClueText(text: String) {
        clueText.setText(text)
    }

    override fun close() {
        containerActivity.finishCurrentContainer()
    }
}