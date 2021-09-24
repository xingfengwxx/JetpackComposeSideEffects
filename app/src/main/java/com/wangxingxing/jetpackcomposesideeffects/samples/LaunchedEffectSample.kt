package com.wangxingxing.jetpackcomposesideeffects.samples

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * author : 王星星
 * date : 2021/9/24 14:51
 * email : 1099420259@qq.com
 * description : LaunchedEffect：在某个可组合项的作用域内运行挂起函数
 */

@Composable
fun ScaffoldSample(
    state: MutableState<Boolean>,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    // 当 state.value 为 true 时协程启动，为 false 时协程取消。
    // 当协程取消后，SnackBar 会自动取消
    if (state.value) {
        LaunchedEffect(scaffoldState.snackbarHostState) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = "Error message",
                actionLabel = "Retry message"
            )
        }
    }

    // 当 LaunchedEffect 中 key 发生改变时，重启协程
    /*LaunchedEffect(state.value) {
        scaffoldState.snackbarHostState.showSnackbar(
            message = "Error message",
            actionLabel = "Retry message"
        )
    }*/

    Scaffold(
        scaffoldState = scaffoldState,
        // 标题栏区域
        topBar = {
            TopAppBar(
                title = { Text(text = "脚手架示例") }
            )
        },
        // 屏幕内容区域
        content = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        state.value = !state.value
                    }
                ) {
                    Text(text = "Error occurs ${state.value}")
                }
            }
        }
    )
}

@Composable
fun LaunchedEffectSample() {
    val state = remember { mutableStateOf(false)}
    ScaffoldSample(state)
}