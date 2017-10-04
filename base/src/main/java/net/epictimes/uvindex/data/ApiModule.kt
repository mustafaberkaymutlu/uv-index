package net.epictimes.uvindex.data

import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import net.epictimes.uvindex.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApiModule {

    @Singleton
    @Provides
    internal fun provideServices(retrofit: Retrofit): Services =
            retrofit.create(Services::class.java)

    @Singleton
    @Provides
    internal fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(Services.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
    }

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
