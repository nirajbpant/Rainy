package com.example.rainy.di


import android.content.Context
import coil.ImageLoader
import com.example.rainy.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request

@Module
@InstallIn(SingletonComponent::class)
object ImageLoaderModule {

    @Provides
    fun provideImageLoader(
        @ApplicationContext context: Context,
        okHttpClient: OkHttpClient
    ): ImageLoader {
        return ImageLoader.Builder(context)
            .okHttpClient {
                okHttpClient.newBuilder()
                    .addInterceptor { chain ->
                        val request: Request = chain.request().newBuilder()
                            .header("User-Agent", context.getString(R.string.app_name))
                            .build()
                        chain.proceed(request)
                    }
                    .build()
            }
            .build()
    }
}
