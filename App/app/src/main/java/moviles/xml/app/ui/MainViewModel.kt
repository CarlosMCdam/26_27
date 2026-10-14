package moviles.xml.app.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import moviles.xml.app.domain.Item
import moviles.xml.app.domain.usecase.AddItemUseCase
import moviles.xml.app.domain.usecase.DeleteItemUseCase
import moviles.xml.app.domain.usecase.LoadItemListUseCase

class MainViewModel(
    private val addItemUseCase: AddItemUseCase,
    private val deleteItemUseCase: DeleteItemUseCase,
    private val loadItemListUseCase: LoadItemListUseCase
) : ViewModel() {
    private val _state = MutableLiveData(MainState())
    val state: LiveData<MainState> = _state

    init {
        loadItemList()
    }

    fun addItem(item: Item) {
        addItemUseCase(item)
        loadItemList()
    }

    fun deleteItem(item: Item) {
        deleteItemUseCase(item)
        loadItemList()
    }

    fun loadItemList() {
        _state.value = _state.value?.copy(itemList = loadItemListUseCase())
    }
}

class MainViewModelFactory(
    private val addItemUseCase: AddItemUseCase,
    private val deleteItemUseCase: DeleteItemUseCase,
    private val loadItemListUseCase: LoadItemListUseCase
) : Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(
            addItemUseCase = addItemUseCase,
            deleteItemUseCase = deleteItemUseCase,
            loadItemListUseCase = loadItemListUseCase
        ) as T
    }
}