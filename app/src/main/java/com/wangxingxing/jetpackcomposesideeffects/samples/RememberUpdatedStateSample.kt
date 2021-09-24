package com.wangxingxing.jetpackcomposesideeffects.samples

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import kotlinx.coroutines.delay

/**
 * author : 王星星
 * date : 2021/9/24 15:34
 * email : 1099420259@qq.com
 * description : rememberUpdatedState：在效应中引用某个值，该效应在值改变时不应重启
 */

private const val TAG = "RememberUpdatedStateSam"

@Composable
fun LandingScreen(onTimeOut: () -> Unit) {
    // 保存一个 mutableStateOf，并且在每次重组时，调用 rememberUpdatedState 更新它为最新的值。
    // 当 LandingScreen 发生重组时，这里将始终引用最新的 onTimeOut 函数
    // rememberUpdatedState 应该在当组合期间计算的参数或值由耗时的 lambda 或对象表达式引用时使用。
    // 因为这样才有时间去更新值。
    val currentOnTimeout by rememberUpdatedState(onTimeOut)
    // val currentOnTimeout by remember { mutableStateOf(onTimeOut) }
    // 使用 LaunchedEffect 时，当 key 发生改变会导致协程重新启动，如果不希望协程重启，可以将 key 设置为常量。
    // 但由此引发的一个问题是，不重启如何保证数据的更新？
    // 这里也就是要将 currentOnTimeout 换成其它函数怎么办？
    LaunchedEffect(true) {
        Log.d(TAG, "LandingScreen: ")
        // 这里要保证协程不能重启，重启会导致计时重新开始
        repeat(10) {
            delay(1000L)
            Log.d(TAG, "LandingScreen: delay ${it + 1} s")
        }
        currentOnTimeout()
    }
}

@Composable
fun RememberUpdatedStateSample() {
    val onTimeOut1: () -> Unit = { Log.d(TAG, "RememberUpdatedStateSample: landing timeout 1.")}
    val onTimeOut2: () -> Unit = { Log.d(TAG, "RememberUpdatedStateSample: landing timeout 2.")}
    // 选择使用哪一个 onTimeOut 函数，默认值为 onTimeOut1
    val changeOnTimeOutState = remember { mutableStateOf(onTimeOut1) }

    Column {
        // 点击按钮，选择 onTimeOut 函数
        Button(onClick = {
            if (changeOnTimeOutState.value == onTimeOut1) {
                changeOnTimeOutState.value = onTimeOut2
            } else {
                changeOnTimeOutState.value = onTimeOut1
            }
        }) {
            Text(text = "choose onTimeOut${if (changeOnTimeOutState.value == onTimeOut1) 1 else 2}")
        }

        LandingScreen(changeOnTimeOutState.value)
    }
}