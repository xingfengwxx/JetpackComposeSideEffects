package com.wangxingxing.jetpackcomposesideeffects.samples

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

/**
 * author : 王星星
 * date : 2021/9/24 16:09
 * email : 1099420259@qq.com
 * description : snapshotFlow：将 Compose 的 State 转换为 Flow
 */

private const val TAG = "SnapshotFlowSample"

@Composable
fun SnapshotFlowSample() {
    val listState = rememberLazyListState()
    LazyColumn(state = listState) {
        items(1000) { index ->  
            Text(text = "Item $index")
        }
    }
    // 当在 snapshotFlow 块中读取的 State 对象之一发生变化时，
    // 如果新值与之前发出的值不相等，Flow 会向其收集器发出新值（此行为类似于 Flow.distinctUntilChanged 的行为）。
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .map { index -> index > 20 } // 收集大于20的索引
            .distinctUntilChanged() // 不用重复发送
            .filter { it }
            .collect {
                Log.d(TAG, "SnapshotFlowSample: $it")
            }
    }
}