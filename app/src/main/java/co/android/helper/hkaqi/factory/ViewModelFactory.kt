package co.android.helper.hkaqi.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import co.android.helper.hkaqi.ui.home.repo.MainRepo
import co.android.helper.hkaqi.ui.home.map.viewmodel.HomeMapsViewModel

class ViewModelFactory<T>(val repo: T): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeMapsViewModel(repo as MainRepo) as T
    }
}