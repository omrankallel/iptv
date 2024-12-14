package tn.iptv.nextplayer.feature.player

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.PagerSnapHelper
import tn.iptv.nextplayer.feature.player.databinding.ActivityCarouselBinding // Assurez-vous que le bon package est utilisé

class CarouselActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCarouselBinding // Déclarez le binding

    private val dummyNotices = listOf(
        Cat("https://cdn2.thecatapi.com/images/6q6.jpg", "link1"),
        Cat("https://cdn2.thecatapi.com/images/9pi.jpg", "link2"),
        Cat("https://cdn2.thecatapi.com/images/c2b.jpg", "link3")
    )

    private val carouselAdapter by lazy {
        CarouselAdapter(::catClicked)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarouselBinding.inflate(layoutInflater) // Initialisez le binding
        setContentView(binding.root)

        binding.rvCat.run {
            PagerSnapHelper().attachToRecyclerView(this)
            adapter = carouselAdapter.apply {
                setItems(dummyNotices)
                addItemDecoration(CircularIndicatorDecoration(this@CarouselActivity))
            }
            resumeAutoScroll()
        }
    }

    private fun catClicked(cat: Cat) {
        binding.tv.text = "clicked ${cat.linkUrl}" // Utilisez le binding pour accéder aux vues
    }
}

data class Cat(
    override val imgUrl: String,
    val linkUrl: String
) : CarouselEntity()
