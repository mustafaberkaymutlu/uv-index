package net.epictimes.uvindex.autocomplete

import android.os.Bundle
import net.epictimes.uvindex.ui.BaseViewStateActivity
import javax.inject.Inject


class AutoCompleteActivity : BaseViewStateActivity<AutoCompleteView, AutoCompletePresenter, AutoCompleteViewState>(), AutoCompleteView {

    @Inject
    lateinit var autoCompletePresenter: AutoCompletePresenter

    @Inject
    lateinit var autoCompleteViewState: AutoCompleteViewState

    override fun createPresenter(): AutoCompletePresenter = autoCompletePresenter

    override fun createViewState(): AutoCompleteViewState = autoCompleteViewState

    override fun onNewViewStateInstance() {
        // no-op
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerAutoCompleteComponent.builder()
                .singletonComponent(singletonComponent)
                .build()
                .inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_complete)
    }
}
