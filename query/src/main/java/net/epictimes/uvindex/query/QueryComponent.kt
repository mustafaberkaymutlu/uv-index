package net.epictimes.uvindex.query

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import net.epictimes.uvindex.BaseApplication
import net.epictimes.uvindex.di.ActivityScoped
import net.epictimes.uvindex.di.SingletonComponent

@ActivityScoped
@Component(dependencies = arrayOf(SingletonComponent::class),
        modules = arrayOf(AndroidInjectionModule::class,
                QueryActivityModule::class,
                QueryActivityBuilderModule::class))
interface QueryComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(baseApplication: BaseApplication): Builder

        fun singletonComponent(singletonComponent: SingletonComponent): Builder

        fun queryActivityModule(queryActivityModule: QueryActivityModule): Builder

        fun build(): QueryComponent
    }

    fun inject(queryActivity: QueryActivity)

}
