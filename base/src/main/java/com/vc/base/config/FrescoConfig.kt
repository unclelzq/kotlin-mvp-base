package com.vc.base.config

import android.app.Application
import android.util.Log
import com.facebook.cache.disk.DiskCacheConfig
import com.facebook.common.internal.Supplier
import com.facebook.common.util.ByteConstants
import com.facebook.imagepipeline.cache.MemoryCacheParams
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.imagepipeline.decoder.ProgressiveJpegConfig
import com.facebook.imagepipeline.image.ImmutableQualityInfo
import com.facebook.imagepipeline.image.QualityInfo


class FrescoConfig private constructor() {

    companion object {
        private val LOG_TAG = FrescoConfig::class.java.simpleName

        private val MAX_HEAP_SIZE = Runtime.getRuntime().maxMemory()//分配的可用内存
        private val MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 8//使用的缓存大小
        private const val MAX_SMALL_DISK_VERY_LOW_CACHE_SIZE = 5 * ByteConstants.MB//小图极低磁盘空间缓存的最大值（特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）
        private const val MAX_SMALL_DISK_LOW_CACHE_SIZE = 15 * ByteConstants.MB//小图低磁盘空间缓存的最大值（特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）
        private const val MAX_SMALL_DISK_CACHE_SIZE = 30 * ByteConstants.MB//小图磁盘缓存的最大值（特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）
        private const val MAX_DISK_CACHE_VERY_LOW_SIZE = 30 * ByteConstants.MB//默认图极低磁盘空间缓存的最大值
        private const val MAX_DISK_CACHE_LOW_SIZE = 50 * ByteConstants.MB //默认图低磁盘空间缓存的最大值
        private const val MAX_DISK_CACHE_SIZE = 150 * ByteConstants.MB //默认图磁盘缓存的最大值

        private const val IMAGE_PIPELINE_SMALL_CACHE_DIR = "Small_Pic"//小图所放路径的文件夹名
        private const val IMAGE_PIPELINE_CACHE_DIR = "Pic"//默认图所放路径的文件夹名

        val instance: FrescoConfig by lazy {
            InnerInstance.frescoConfig
        }
    }

    private object InnerInstance {
        val frescoConfig = FrescoConfig()
    }

    private var imagePipelineConfig: ImagePipelineConfig? = null

    private fun configureCaches(context: Application): ImagePipelineConfig? {
        if (imagePipelineConfig != null)
            return imagePipelineConfig
        val cachePath = context.externalCacheDir

        val cacheParams = Supplier {
            val cacheParams = MemoryCacheParams(
                    MAX_MEMORY_CACHE_SIZE.toInt(),// 内存缓存中总图片的最大大小,以字节为单位。
                    Int.MAX_VALUE, // 内存缓存中图片的最大数量。
                    MAX_MEMORY_CACHE_SIZE.toInt(),// 内存缓存中准备清除但尚未被删除的总图片的最大大小,以字节为单位。
                    Int.MAX_VALUE, // 内存缓存中准备清除的总图片的最大数量。
                    MAX_MEMORY_CACHE_SIZE.toInt()// 内存缓存中单个图片的最大大小。
            )
            cacheParams
        }

        val smallPicCache = DiskCacheConfig.newBuilder(context)
                .setBaseDirectoryPath(cachePath)//缓存图片基路径
                .setBaseDirectoryName(IMAGE_PIPELINE_SMALL_CACHE_DIR)//文件夹名
                .setCacheErrorLogger { _, _, message, throwable ->
                    Log.i(LOG_TAG, "*******图片错误 samllPicCache $message 异常 $throwable")
                }
//                .setCacheEventListener(object :CacheEventListener{})//缓存事件侦听器。
                .setMaxCacheSize(MAX_SMALL_DISK_CACHE_SIZE.toLong())//默认缓存的最大大小。
                .setMaxCacheSizeOnLowDiskSpace(MAX_SMALL_DISK_LOW_CACHE_SIZE.toLong())//缓存的最大大小,使用设备时低磁盘空间。
                .setMaxCacheSizeOnVeryLowDiskSpace(MAX_SMALL_DISK_VERY_LOW_CACHE_SIZE.toLong())//缓存的最大大小,当设备极低磁盘空间
                .build()

        val picCache = DiskCacheConfig.newBuilder(context)
                .setBaseDirectoryPath(cachePath)//缓存图片基路径
                .setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR)//文件夹名
                .setCacheErrorLogger { _, _, message, throwable ->
                    Log.i(LOG_TAG, "*******图片错误 picCache $message 异常 $throwable")
                }
//                .setCacheEventListener(object :CacheEventListener{})//缓存事件侦听器。
                .setMaxCacheSize(MAX_DISK_CACHE_SIZE.toLong())//默认缓存的最大大小。
                .setMaxCacheSizeOnLowDiskSpace(MAX_DISK_CACHE_LOW_SIZE.toLong())//缓存的最大大小,使用设备时低磁盘空间。
                .setMaxCacheSizeOnVeryLowDiskSpace(MAX_DISK_CACHE_VERY_LOW_SIZE.toLong())//缓存的最大大小,当设备极低磁盘空间
                .build()

        //渐变配置
        val progressiveJpegConfig = object : ProgressiveJpegConfig {
            override fun getNextScanNumberToDecode(scanNumber: Int): Int {
                return scanNumber + 2
            }

            override fun getQualityInfo(scanNumber: Int): QualityInfo {
                val isGoodEnough = scanNumber >= 5
                return ImmutableQualityInfo.of(scanNumber, isGoodEnough, false)
            }
        }

        val configBuilder = ImagePipelineConfig.newBuilder(context)
//            .setAnimatedImageFactory(AnimatedImageFactory animatedImageFactory)//图片加载动画
                .setBitmapMemoryCacheParamsSupplier(cacheParams)//内存缓存配置（一级缓存，已解码的图片）
                .setMainDiskCacheConfig(picCache)//磁盘缓存配置（总，三级缓存）
//            .setNetworkFetchProducer(networkFetchProducer)//自定的网络层配置：如OkHttp，Volley
                .setProgressiveJpegConfig(progressiveJpegConfig)//渐进式JPEG图
                .setSmallImageDiskCacheConfig(smallPicCache)//磁盘缓存配置（小图片，可选～三级缓存的小图优化缓存）

        imagePipelineConfig = configBuilder.build()

        return imagePipelineConfig
    }

    /**
     * 初始化配置，单例
     */
    fun getImagePipelineConfig(context: Application): ImagePipelineConfig {
        if (imagePipelineConfig == null) {
            synchronized(ImagePipelineConfig::class.java) {
                if (imagePipelineConfig == null) {
                    imagePipelineConfig = configureCaches(context)
                }
            }
        }
        return imagePipelineConfig!!
    }

}