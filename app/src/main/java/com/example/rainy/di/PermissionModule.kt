package com.example.rainy.di

import android.content.Context
import com.example.rainy.core.PermissionHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PermissionModule {

    @Provides
    @Singleton
    fun providePermissionHandler(@ApplicationContext context: Context): PermissionHandler {
        return PermissionHandler(context)
    }
}