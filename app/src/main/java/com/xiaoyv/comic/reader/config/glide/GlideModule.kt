package com.xiaoyv.comic.reader.config.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.xiaoyv.comic.reader.data.entity.FileEntity
import java.io.InputStream

@GlideModule
class GlideModule : AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.append(FileEntity::class.java, InputStream::class.java, FileEntityLoader.Factory())
    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {

    }
}
