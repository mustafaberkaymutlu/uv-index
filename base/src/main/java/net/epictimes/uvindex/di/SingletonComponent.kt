package net.epictimes.uvindex.di

import dagger.BindsInstance
import dagger.Component
import net.epictimes.uvindex.BaseApplication
import net.epictimes.uvindex.data.ApiModule
import net.epictimes.uvindex.data.Services
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(SingletonModule::class, ApiModule::class))
interface SingletonComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(baseApplication: BaseApplication): Builder

        fun singletonModule(singletonModule: SingletonModule): Builder

        fun apiModule(apiModule: ApiModule): Builder

        fun build(): SingletonComponent
    }

    fun inject(baseApplication: BaseApplication)

    fun getServices(): Services

}
