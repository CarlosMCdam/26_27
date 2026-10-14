package moviles.xml.app.domain.usecase

import moviles.xml.app.data.itemList
import moviles.xml.app.domain.Item

class DeleteItemUseCase {
    operator fun invoke(item: Item) = itemList.remove(item)
}