package com.example.todolist.presentation.ui.compose.divKit

import android.annotation.SuppressLint
import android.content.Context
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.LinearLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.example.todolist.divKit.AssetReader
import com.example.todolist.divKit.Div2ViewFactory
import com.example.todolist.divKit.PicassoDivImageLoader
import com.example.todolist.divKit.SampleDivActionHandler
import com.yandex.div.core.Div2Context
import com.yandex.div.core.DivConfiguration
import dagger.hilt.android.qualifiers.ApplicationContext

@SuppressLint("StaticFieldLeak")
lateinit var divView: View

@Composable
fun ViewScreen(
    onBack: () -> Unit
) {

    AndroidView(factory = { context ->
        LinearLayout(context).apply {
            divView = createDivView(context, onBack)
            addView(divView)
        }
    })

}


fun delView() {
    divView?.let {
        (divView.parent as? LinearLayout)?.removeView(divView)
    }
}

fun createDivView(@ApplicationContext context: Context, onBack: () -> Unit): View {
    val assetReader = AssetReader(context)
    val divJson = assetReader.read("sample.json")
    val templatesJson = divJson.optJSONObject("templates")
    val cardJson = divJson.getJSONObject("card")

    val divContext = Div2Context(
        baseContext = context as ContextThemeWrapper,
        configuration = createDivConfiguration(context, onBack),
    )

    val divView = Div2ViewFactory(divContext, templatesJson).createView(cardJson)

    return divView
}

private fun createDivConfiguration(
    @ApplicationContext context: Context,
    onBack: () -> Unit
): DivConfiguration {
    return DivConfiguration.Builder(PicassoDivImageLoader(context))
        .actionHandler(SampleDivActionHandler(onBack))
        .visualErrorsEnabled(true)
        .build()
}