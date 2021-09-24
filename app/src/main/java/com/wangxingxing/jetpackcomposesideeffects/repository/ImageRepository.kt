package com.wangxingxing.jetpackcomposesideeffects.repository

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.request.FutureTarget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.Exception

/**
 * author : 王星星
 * date : 2021/9/24 15:04
 * email : 1099420259@qq.com
 * description :
 */

class ImageRepository(
    private val context: Context
) {
    // 通过 Glide 下载一个图片在本地，然后将图片转换为Bitmap，再转换为 ImageBitmap（可以用于compose中）
    suspend fun load(url: String): Image? {
        return withContext(Dispatchers.IO) {
            try {
                delay(1000) // 故意延迟
                val future: FutureTarget<File> = Glide
                    .with(context)
                    .load(url)
                    .downloadOnly(200, 200)
                val imageBitmap =
                    BitmapFactory.decodeFile(future.get().absolutePath).asImageBitmap()
                Image(imageBitmap)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}

data class Image(val imageBitmap: ImageBitmap)

sealed class Result<T>() {
    object Loading: Result<Image>()
    object Error: Result<Image>()
    data class Success(val image: Image) : Result<Image>()
}