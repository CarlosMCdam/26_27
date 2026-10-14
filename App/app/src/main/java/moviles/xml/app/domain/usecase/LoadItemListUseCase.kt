package moviles.xml.app.domain.usecase

import moviles.xml.app.data.itemList

class LoadItemListUseCase {
    operator fun invoke() = itemList.toList()
}