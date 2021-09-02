package com.example.vCovid.di

import com.example.vCovid.data.network.FlagApi
import com.example.vCovid.data.network.CovidApi
import com.example.vCovid.util.Constants.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideHttpClient() : OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    @Retrofit1
    fun provideRetrofitInstance(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }


    @Singleton
    @Provides
    @Retrofit2
    fun provideRetrofitInstanceSec(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.countryflags.io")
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(@Retrofit1 retrofit: Retrofit): CovidApi {
        return retrofit.create(CovidApi::class.java)
    }

    @Singleton
    @Provides
    fun provideFlagService(@Retrofit2 retrofit: Retrofit): FlagApi {
        return retrofit.create(FlagApi::class.java)
    }

}


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Retrofit1


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Retrofit2



