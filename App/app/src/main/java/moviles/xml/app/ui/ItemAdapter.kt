package moviles.xml.app.ui

import android.view.LayoutInflater.from
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import moviles.xml.app.databinding.ItemBinding
import moviles.xml.app.databinding.ItemBinding.inflate
import moviles.xml.app.domain.Item

typealias DeleteItem = (Item) -> Unit

class ItemAdapter(
    private val deleteItem: DeleteItem
) : ListAdapter<Item, ItemViewHolder>(ItemDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = inflate(from(parent.context), parent, false)
        return ItemViewHolder(binding, deleteItem)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) = holder.bind(getItem(position))
}

class ItemViewHolder(
    private val binding: ItemBinding,
    private val deleteItem: DeleteItem
) : ViewHolder(binding.root) {
    fun bind(item: Item) = with(binding) {
        id.text = item.id.toString()
        root.setOnClickListener {
            deleteItem(item)
        }
    }
}

private class ItemDiffCallback : ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Item, newItem: Item) = oldItem == newItem
}