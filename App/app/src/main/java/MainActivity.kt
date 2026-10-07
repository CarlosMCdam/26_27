package moviles.xml.app

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener
import androidx.core.view.WindowInsetsCompat.Type.systemBars
import moviles.xml.app.R.id.placeholder_main
import moviles.xml.app.R.layout.contador

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(contador)
        setOnApplyWindowInsetsListener(findViewById(placeholder_main)) { view, insets ->
            val systemBars = insets.getInsets(systemBars())
            with(systemBars) {
                view.setPadding(left, top, right, bottom)
            }
            insets
        }
    }
}