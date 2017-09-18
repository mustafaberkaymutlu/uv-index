package net.epictimes.uvindex.query

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class QueryActivityBuilderModule {

    @ContributesAndroidInjector(modules = arrayOf(QueryActivityModule::class))
    internal abstract fun contributeQueryActivityInjector(): QueryActivity

}