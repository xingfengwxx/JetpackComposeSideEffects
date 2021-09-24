package com.wangxingxing.jetpackcomposesideeffects.samples

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * author : 王星星
 * date : 2021/9/24 11:52
 * email : 1099420259@qq.com
 * description : 使用 Scaffold 实现包含抽屉布局的UI
 *               rememberCoroutineScope：获取组合感知作用域，以便在可组合项外启动协程
 */

@Composable
fun ScaffoldSample() {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "抽屉组件中的内容")
            }
        },

        // 标题栏区域
        topBar = {
            TopAppBar(
                title = { Text(text = "脚手架示例") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                scaffoldState.drawerState.open()
                            }
                        }
                    ) {
                        Icon(imageVector = Icons.Filled.Menu, contentDescription = null)
                    }
                }
            )
        },

        //屏幕内容区域
        content = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "屏幕内容区域")
            }
        },

        //悬浮按钮
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("悬浮按钮") },
                onClick = {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar("点击了悬浮按钮")
                    }
                }
            )
        },

        floatingActionButtonPosition = FabPosition.End,
        snackbarHost = {
            SnackbarHost(it) { data ->
                Snackbar(
                    snackbarData = data,
                    backgroundColor = MaterialTheme.colors.surface,
                    contentColor = MaterialTheme.colors.onSurface,
                    shape = CutCornerShape(10.dp)
                )
            }
        }
    )
}