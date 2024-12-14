package tn.iptv.nextplayer.feature.player

import RetrofitInstance
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("UnsafeOptInUsageError")
@AndroidEntryPoint
class ServerActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_server)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.setLayoutManager(layoutManager);

        fetchServices()
    }

    private fun fetchServices() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.getServices()
                if (response.response) {
                    val services = response.data
                    runOnUiThread {
                        recyclerView.adapter = ServiceAdapter(services)
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@ServerActivity, "Erreur lors du chargement des services", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@ServerActivity, "Erreur : ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}