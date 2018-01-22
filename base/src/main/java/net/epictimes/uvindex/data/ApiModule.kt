package net.epictimes.uvindex.data

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import net.epictimes.uvindex.base.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class ApiModule {

    companion object {
        const val API_DATE_FORMAT = "yyyy-MM-dd:HH"
    }

    @Singleton
    @Provides
    fun provideServices(retrofit: Retrofit): Services = retrofit.create(Services::class.java)

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit = Retrofit.Builder()
            .baseUrl(Services.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().setDateFormat(API_DATE_FORMAT).create()

    @Singleton
    @Provides
    fun provideOkHttpClient(defaultInterceptor: DefaultInterceptor,
                            httpLoggingInterceptor: HttpLoggingInterceptor?): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()

        httpLoggingInterceptor?.let {
            okHttpClientBuilder.addInterceptor(it)
            it.setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        okHttpClientBuilder.addNetworkInterceptor(defaultInterceptor)

        return okHttpClientBuilder.build()
    }

    @Singleton
    @Provides
    fun provideDefaultInterceptor(): DefaultInterceptor = DefaultInterceptor()

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor? = if (BuildConfig.DEBUG) HttpLoggingInterceptor() else null
}
