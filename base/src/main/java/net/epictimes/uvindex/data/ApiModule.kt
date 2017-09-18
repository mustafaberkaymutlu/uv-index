package net.epictimes.uvindex.data

import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import net.epictimes.uvindex.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class ApiModule {

    @Provides
    internal fun provideServices(retrofit: Retrofit): Services {
        return retrofit.create(Services::class.java)
    }

    @Provides
    internal fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(Services.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
    }

    @Provides
    fun provideOkHttpClient(stethoInterceptor: StethoInterceptor?): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            okHttpClientBuilder.addNetworkInterceptor(stethoInterceptor)
        }

        return okHttpClientBuilder.build()
    }

    @Provides
    fun provideStethoInterceptor(): StethoInterceptor? {
        return if (BuildConfig.DEBUG) StethoInterceptor() else null
    }

}
