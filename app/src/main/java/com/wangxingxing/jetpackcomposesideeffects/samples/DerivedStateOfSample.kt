package com.wangxingxing.jetpackcomposesideeffects.samples

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * author : 王星星
 * date : 2021/9/24 14:16
 * email : 1099420259@qq.com
 * description :
 */

// 扩展函数，检查字符串是否包含指定集合中的任一字符串
fun String.containsWord(list: List<String>): Boolean {
    for (item in list) {
        if (this.contains(item)) return true
    }
    return false
}

@Composable
fun ItemText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        textAlign = TextAlign.Center,
        modifier = modifier
            .height(30.dp)
            .fillMaxWidth()
    )
}

// derivedStateOf：将一个或多个状态对象转换为其他状态
// 使用此函数可确保仅当计算中使用的状态之一发生变化时才会进行计算。
@Composable
fun TodoList(
    highPrioritywords: List<String> = listOf("Review", "Unblock", "Compose")
) {
    val todoTasks = remember { mutableStateListOf<String>() }
    // 由于执行过滤以计算 highPriorityTasks 的成本很高，因此应仅在任何列表更改时执行，而不是在每次重组时执行。
    val highPriorityTasks = remember(todoTasks, highPrioritywords) {
        derivedStateOf {
            todoTasks.filter { it.containsWord(highPrioritywords) }
        }
    }
    var (text, setText) = remember { mutableStateOf("") }
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(value = text, onValueChange = setText)
            Button(
                onClick = { todoTasks.add(text) }
            ) {
                Text(text = "Add")
            }
        }

        LazyColumn {
            // 高优先级任务
            items(highPriorityTasks.value) {
                ItemText(
                    text = it,
                    modifier = Modifier.background(Color.LightGray)
                )
            }
            // 所有任务
            items(todoTasks) {
                ItemText(text = it)
            }
        }
    }
}

@Composable
fun DerivedStateOfSample() {
    TodoList()
}
