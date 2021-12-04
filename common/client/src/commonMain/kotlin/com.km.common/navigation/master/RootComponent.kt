package com.km.common.navigation.master

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.observe
import com.arkivanov.decompose.value.reduce
import kotlinx.coroutines.flow.MutableStateFlow

class RootComponent(
    componentContext: ComponentContext
) : Root, ComponentContext by componentContext {

    private val database = DefaultArticleDatabase()

    private val _models = MutableValue(Root.Model())
    override val models: Value<Root.Model> = _models

    private val isDetailsToolbarVisible = MutableStateFlow(!_models.value.isMultiPane)
    private val selectedArticleIdSubject = MutableStateFlow<Long?>(null)

    private val listRouter =
        ListRouter(
            componentContext = this,
            database = database,
            selectedArticleId = selectedArticleIdSubject,
            onArticleSelected = ::onArticleSelected
        )

    override val listRouterState: Value<RouterState<*, Root.ListChild>> = listRouter.state

    private val detailsRouter =
        DetailsRouter(
            componentContext = this,
            database = database,
            isToolbarVisible = isDetailsToolbarVisible,
            onFinished = ::closeDetailsAndShowList
        )

    override val detailsRouterState: Value<RouterState<*, Root.DetailsChild>> = detailsRouter.state

    init {
        backPressedHandler.register {
            if (isMultiPaneMode() || !detailsRouter.isShown()) {
                false
            } else {
                closeDetailsAndShowList()
                true
            }
        }

        detailsRouter.state.observe(lifecycle) {
            selectedArticleIdSubject.value = it.activeChild.configuration.getArticleId()
        }
    }

    private fun closeDetailsAndShowList() {
        listRouter.show()
        detailsRouter.closeArticle()
    }

    private fun onArticleSelected(id: Long) {
        detailsRouter.showArticle(id = id)

        if (isMultiPaneMode()) {
            listRouter.show()
        } else {
            listRouter.moveToBackStack()
        }
    }

    override fun setMultiPane(isMultiPane: Boolean) {
        _models.reduce { it.copy(isMultiPane = isMultiPane) }
        isDetailsToolbarVisible.value = !isMultiPane

        if (isMultiPane) {
            switchToMultiPane()
        } else {
            switchToSinglePane()
        }
    }

    private fun switchToMultiPane() {
        listRouter.show()
    }

    private fun switchToSinglePane() {
        if (detailsRouter.isShown()) {
            listRouter.moveToBackStack()
        } else {
            listRouter.show()
        }
    }

    private fun isMultiPaneMode(): Boolean = _models.value.isMultiPane

    private fun DetailsRouter.Config.getArticleId(): Long? =
        when (this) {
            is DetailsRouter.Config.None -> null
            is DetailsRouter.Config.Details -> articleId
        }
}
