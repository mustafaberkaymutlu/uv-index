package net.epictimes.uvindex.query

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import net.epictimes.uvindex.BaseApplication
import net.epictimes.uvindex.di.SingletonComponent

@QueryFeatureScoped
@Component(dependencies = arrayOf(SingletonComponent::class),
        modules = arrayOf(AndroidInjectionModule::class, QueryActivityBuilderModule::class))
interface QueryComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(baseApplication: BaseApplication): Builder

        fun singletonComponent(singletonComponent: SingletonComponent): Builder

        fun build(): QueryComponent
    }

    fun inject(queryApp: QueryApplication)

}
