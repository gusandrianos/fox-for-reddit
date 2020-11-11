package io.github.gusandrianos.foxforreddit.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.gusandrianos.foxforreddit.data.repositories.SearchRepository

@Suppress("UNCHECKED_CAST")
class SearchViewModelFactory(private val mSearchRepository: SearchRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchViewModel(mSearchRepository) as T
    }
}
