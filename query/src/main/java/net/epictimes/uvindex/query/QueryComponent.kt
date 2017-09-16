package net.epictimes.uvindex.query

import dagger.BindsInstance
import dagger.Component
import net.epictimes.uvindex.BaseApplication
import net.epictimes.uvindex.di.SingletonModule

@Component(modules = arrayOf(SingletonModule::class))
interface QueryComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: BaseApplication): Builder

        fun singletonModule(singletonModule: SingletonModule): Builder

        fun build(): QueryComponent
    }

}
