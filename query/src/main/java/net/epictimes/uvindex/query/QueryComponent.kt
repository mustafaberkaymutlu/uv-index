package net.epictimes.uvindex.query

import dagger.Component
import net.epictimes.uvindex.di.SingletonComponent

@QueryFeatureScoped
@Component(dependencies = arrayOf(SingletonComponent::class),
        modules = arrayOf(QueryActivityModule::class))
interface QueryComponent {

    fun inject(queryActivity: QueryActivity)

}
