package moviles.xml.app.domain.usecase

import moviles.xml.app.data.itemList
import moviles.xml.app.domain.Item

class AddItemUseCase {
    operator fun invoke(item: Item) = itemList.add(item)
}