package com.example.digitaltablet.di

import android.app.Application
import android.speech.SpeechRecognizer
import androidx.media3.common.C.Priority
import com.example.digitaltablet.data.remote.LanguageModelApi
import com.example.digitaltablet.data.remote.LanguageModelHeaderInterceptor
import com.example.digitaltablet.data.remote.RcslApi
import com.example.digitaltablet.data.remote.RcslHeaderInterceptor
import com.example.digitaltablet.data.repository.LanguageModelRepository
import com.example.digitaltablet.data.repository.MqttRepository
import com.example.digitaltablet.data.repository.RcslRepository
import com.example.digitaltablet.domain.repository.ILanguageModelRepository
import com.example.digitaltablet.domain.repository.IMqttRepository
import com.example.digitaltablet.domain.repository.IRcslRepository
import com.example.digitaltablet.domain.usecase.LanguageModelUseCase
import com.example.digitaltablet.domain.usecase.MqttUseCase
import com.example.digitaltablet.domain.usecase.RcslUseCase
import com.example.digitaltablet.util.Constants.LanguageModel
import com.example.digitaltablet.util.Constants.Rcsl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMqttRepository(
        application: Application
    ): IMqttRepository = MqttRepository(application)

    @Provides
    @Singleton
    fun provideMqttUseCase(
        repository: IMqttRepository
    ): MqttUseCase = MqttUseCase(repository)

    @Provides
    @Singleton
    fun provideLanguageModelApi(): LanguageModelApi {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(LanguageModelHeaderInterceptor())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .baseUrl(LanguageModel.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LanguageModelApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLanguageModelRepository(
        languageModelApi: LanguageModelApi
    ): ILanguageModelRepository = LanguageModelRepository(languageModelApi)

    @Provides
    @Singleton
    fun provideLanguageModelUseCase(
        languageModelRepository: ILanguageModelRepository
    ): LanguageModelUseCase = LanguageModelUseCase(languageModelRepository)

    @Provides
    @Singleton
    fun provideRcslApi(): RcslApi {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(RcslHeaderInterceptor())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .baseUrl(Rcsl.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RcslApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRcslRepository(
        rcslApi: RcslApi
    ): IRcslRepository = RcslRepository(rcslApi)

    @Provides
    @Singleton
    fun provideRcslUseCase(
        rcslRepository: IRcslRepository
    ): RcslUseCase = RcslUseCase(rcslRepository)
}