package com.dtprogramming.treasurehuntirl.ui.container

import android.os.Bundle
import android.view.ViewGroup
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.ui.activities.ContainerActivity
import kotlinx.android.synthetic.main.container_create_answer.view.*

/**
 * Created by ryantaylor on 7/9/16.
 */
class CreateAnswerContainer : BasicContainer() {

    companion object {
        val URI: String = CreateAnswerContainer::class.java.simpleName
    }

    override fun inflate(containerActivity: ContainerActivity, parent: ViewGroup, extras: Bundle): Container {
        super.inflate(containerActivity, parent, extras)
        inflateView(R.layout.container_create_answer)

        parent.container_create_answer_save.setOnClickListener { containerActivity.finishCurrentContainer() }
        parent.container_create_answer_cancel.setOnClickListener { containerActivity.finishCurrentContainer() }

        return this
    }

    override fun onFinish() {

    }
}