package moviles.xml.app.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import moviles.xml.app.R
import moviles.xml.app.databinding.ActivityMainBinding
import moviles.xml.app.domain.Item
import moviles.xml.app.domain.usecase.AddItemUseCase
import moviles.xml.app.domain.usecase.DeleteItemUseCase
import moviles.xml.app.domain.usecase.LoadItemListUseCase

class MainActivity : AppCompatActivity() {
    private val binding : ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(
            AddItemUseCase(),
            DeleteItemUseCase(),
            LoadItemListUseCase()
        )
    }

    private lateinit var adapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            with(systemBars) {
                view.setPadding(left, top, right, bottom)
            }
            insets
        }
        configureRecycler()
        configureEvents()
        observeState()
    }

    private fun configureRecycler() {
        binding.list.layoutManager = LinearLayoutManager(this)
        adapter = ItemAdapter(deleteItem = viewModel::deleteItem)
        binding.list.adapter = adapter
    }

    private fun configureEvents() = with(binding) {
        addButton.setOnClickListener {
            viewModel.addItem(Item(5))
        }
    }

    private fun observeState() {
        viewModel.state.observe(this) {
            it?.itemList.let { list ->
                adapter.submitList(list)
            }
        }
    }
}