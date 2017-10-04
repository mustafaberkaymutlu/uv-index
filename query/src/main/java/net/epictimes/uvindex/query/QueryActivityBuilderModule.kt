package net.epictimes.uvindex.query

import dagger.Module
import dagger.android.ContributesAndroidInjector
import net.epictimes.uvindex.di.ActivityScoped

@Module
abstract class QueryActivityBuilderModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = arrayOf(QueryActivityModule::class))
    abstract fun contributeQueryActivityInjector(): QueryActivity

}