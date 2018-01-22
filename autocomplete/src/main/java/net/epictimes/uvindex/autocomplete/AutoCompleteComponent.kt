package net.epictimes.uvindex.autocomplete

import dagger.Component
import net.epictimes.uvindex.di.SingletonComponent

@AutoCompleteFeatureScoped
@Component(dependencies = [(SingletonComponent::class)],
        modules = [(AutoCompleteActivityModule::class)])
interface AutoCompleteComponent {

    fun inject(autoCompleteActivity: AutoCompleteActivity)

}
