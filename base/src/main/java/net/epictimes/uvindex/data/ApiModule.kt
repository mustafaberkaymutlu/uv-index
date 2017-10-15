package net.epictimes.uvindex.data

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import net.epictimes.uvindex.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class ApiModule {

    companion object {
        val API_DATE_FORMAT = "yyyy-MM-dd:HH"
    }

    @Singleton
    @Provides
    fun provideServices(retrofit: Retrofit): Services =
            retrofit.create(Services::class.java)

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
                .baseUrl(Services.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()
    }

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().setDateFormat(API_DATE_FORMAT).create()

    @Singleton
    @Provides
    fun provideOkHttpClient(defaultInterceptor: DefaultInterceptor,
                            stethoInterceptor: StethoInterceptor?): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()

        stethoInterceptor?.let { okHttpClientBuilder.addNetworkInterceptor(it) }

        okHttpClientBuilder.addNetworkInterceptor(defaultInterceptor)

        return okHttpClientBuilder.build()
    }

    @Singleton
    @Provides
    fun provideDefaultInterceptor(): DefaultInterceptor = DefaultInterceptor()

    @Singleton
    @Provides
    fun provideStethoInterceptor(): StethoInterceptor? =
            if (BuildConfig.DEBUG) StethoInterceptor() else null

}
