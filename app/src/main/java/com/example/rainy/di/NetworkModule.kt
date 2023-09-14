package com.example.rainy.di

import android.app.Application
import android.content.Context
import com.example.rainy.BuildConfig
import com.example.rainy.core.constants.Constants.API_BASE_URL
import com.example.rainy.data.network.ConnectivityInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
open class NetworkModule {

    private val interceptor: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }
    }


    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application


    @Provides
    @Singleton
    internal fun providesGson() =
        GsonBuilder()
            .setPrettyPrinting()
            .create()

    @Provides
    @Singleton
    fun provideConnectivityInterceptor(context: Context): ConnectivityInterceptor {
        return ConnectivityInterceptor(context)
    }


    @Provides
    @Singleton
    internal fun providesOkHttpClient(
        connectivityInterceptor: ConnectivityInterceptor,
        gson: Gson
    ): OkHttpClient = OkHttpClient.Builder().apply {
        if (BuildConfig.DEBUG) {
            addInterceptor(interceptor)
        }
        addInterceptor(connectivityInterceptor)
    }.build()

    @Provides
    @Singleton
    fun provideRetrofit(httpClient: OkHttpClient, gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}
