package net.epictimes.uvindex.query

import android.os.Bundle
import dagger.android.AndroidInjection
import net.epictimes.uvindex.BaseApplication
import net.epictimes.uvindex.ui.BaseViewStateActivity
import javax.inject.Inject

class QueryActivity : BaseViewStateActivity<QueryView, QueryPresenter, QueryViewState>() {

    @Inject
    lateinit var queryPresenter: QueryPresenter

    @Inject
    lateinit var queryViewState: QueryViewState

    override fun createViewState(): QueryViewState {
        return queryViewState
    }

    override fun createPresenter(): QueryPresenter {
        return queryPresenter
    }

    override fun onNewViewStateInstance() {
        // no-op
    }

    override fun onCreate(savedInstanceState: Bundle?) {
//        AndroidInjection.inject(this)

        // TODO use AndroidInjection

        DaggerQueryComponent.builder()
                .application(application as BaseApplication)
                .singletonComponent((application as BaseApplication).singletonComponent)
                .queryActivityModule(QueryActivityModule())
                .build()
                .inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_query)
    }
}
