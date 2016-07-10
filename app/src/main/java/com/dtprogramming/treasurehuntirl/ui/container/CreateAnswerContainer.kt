package com.dtprogramming.treasurehuntirl.ui.container

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.ui.activities.ContainerActivity
import com.dtprogramming.treasurehuntirl.ui.views.AdjustableValueView
import kotlinx.android.synthetic.main.container_create_answer.view.*

/**
 * Created by ryantaylor on 7/9/16.
 */
class CreateAnswerContainer : BasicContainer() {

    companion object {
        val URI: String = CreateAnswerContainer::class.java.simpleName
    }

    private lateinit var adjustableArea: AdjustableValueView
    var area = 100

    override fun inflate(containerActivity: ContainerActivity, parent: ViewGroup, extras: Bundle): Container {
        super.inflate(containerActivity, parent, extras)
        inflateView(R.layout.container_create_answer)

        adjustableArea = parent.container_create_answer_adjustable_zone

        parent.container_create_answer_save.setOnClickListener { containerActivity.finishCurrentContainer() }
        parent.container_create_answer_cancel.setOnClickListener { containerActivity.finishCurrentContainer() }

        adjustableArea.setOnLeftDrawableClickListener {

            if (area > 1) {
                area --

                adjustableArea.mText = area.toString()
            }
        }

        adjustableArea.container_create_answer_adjustable_zone.setOnRightDrawableClickListener {

            if (area < 9999) {
                area ++

                adjustableArea.mText = area.toString()
            }
        }

        adjustableArea.mText = area.toString()

        return this
    }

    override fun onFinish() {

    }
}