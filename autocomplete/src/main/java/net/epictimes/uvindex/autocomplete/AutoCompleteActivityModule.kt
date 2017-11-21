package net.epictimes.uvindex.autocomplete

import dagger.Module
import dagger.Provides

@Module
class AutoCompleteActivityModule {

    @AutoCompleteFeatureScoped
    @Provides
    fun provideAutoCompetePresenter(): AutoCompletePresenter = AutoCompletePresenter()

    @AutoCompleteFeatureScoped
    @Provides
    fun provideQueryViewState(): AutoCompleteViewState = AutoCompleteViewState()

}