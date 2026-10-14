package moviles.xml.app.ui

import moviles.xml.app.domain.Item

data class MainState(
    val itemList: List<Item> = emptyList()
)