package com.example.todolist.divKit

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import com.example.todolist.R
import com.yandex.div.core.DivActionHandler
import com.yandex.div.core.DivViewFacade
import com.yandex.div.json.expressions.ExpressionResolver
import com.yandex.div2.DivAction
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class SampleDivActionHandler(private val onBack: () -> Unit) : DivActionHandler() {
    override fun handleAction(
        action: DivAction,
        view: DivViewFacade
    ): Boolean {
        val url =
            action.url?.evaluate(view.expressionResolver) ?: return super.handleAction(action, view)

        return if (url.scheme == SCHEME_SAMPLE && handleSampleAction(url, view.view.context)) {
            true
        } else {
            super.handleAction(action, view)
        }
    }

    private fun handleSampleAction(action: Uri, context: Context): Boolean {
        return when (action.host) {
            "exit" -> {
                Toast.makeText(context, context.getString(R.string.returnToApp), Toast.LENGTH_SHORT)
                    .show()
                onBack()
                true
            }

            else -> false
        }
    }

    companion object {
        const val SCHEME_SAMPLE = "sample-action"
    }
}
