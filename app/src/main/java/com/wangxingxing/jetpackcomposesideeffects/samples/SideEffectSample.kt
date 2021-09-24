package com.wangxingxing.jetpackcomposesideeffects.samples

import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * author : 王星星
 * date : 2021/9/24 15:52
 * email : 1099420259@qq.com
 * description : SideEffect：将 Compose 状态发布为非 Compose 代码
 */

private const val TAG = "SideEffectSample"

@Composable
fun BackHandler(
    backDispatcher: OnBackPressedDispatcher,
    enabled: Boolean = true, // 控制返回事件是否被拦截
    onBack: () -> Unit
) {
    val currentOnBack by rememberUpdatedState(onBack)
    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentOnBack() // enabled 为 true 时，回调才会执行
            }
        }
    }

    // 如需与非 Compose 管理的对象共享 Compose 状态，请使用 SideEffect 可组合项，
    // 因为每次成功重组时都会调用该可组合项。
    SideEffect {
        Log.d(TAG, "SideEffect enabled:$enabled")
        backCallback.isEnabled = enabled
    }

    DisposableEffect(backDispatcher) {
        backDispatcher.addCallback(backCallback)
        onDispose {
            Log.d(TAG, "BackHandler: onDispose")
            backCallback.remove()
        }
    }
}

@Composable
fun SideEffectSample(backDispatcher: OnBackPressedDispatcher) {
    var addBackCallback by remember { mutableStateOf(false) }
    // 控制返回事件是否被拦截
    var interceptBackEvent by remember { mutableStateOf(false) }

    Column {
        Row {
            Switch(
                checked = addBackCallback,
                onCheckedChange = {
                    addBackCallback = !addBackCallback
                }
            )
            Text(text = if (addBackCallback) "Add Back Callback" else "Not Add Back Callback")
        }

        Spacer(modifier = Modifier.height(50.dp))

        Row {
            Switch(
                checked = interceptBackEvent,
                onCheckedChange = {
                    interceptBackEvent = !interceptBackEvent
                }
            )
            Text(text = if (interceptBackEvent) "Intercept BackEvent" else "Not Intercept BackEvent")
        }
    }

    // addBackCallback 为 false时，BackHandler 被移除
    if (addBackCallback) {
        BackHandler(backDispatcher, interceptBackEvent) {
            Log.d(TAG, "SideEffectSample: onBack")
        }
    }
}