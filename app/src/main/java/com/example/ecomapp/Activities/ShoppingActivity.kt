package com.example.ecomapp.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.load.engine.Resource
import com.example.ecomapp.R
import com.example.ecomapp.Viewmodel.Cartviewmodel
import com.example.ecomapp.databinding.ActivityShoppingBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ShoppingActivity : AppCompatActivity() {

    private val binding by lazy{
        ActivityShoppingBinding.inflate(layoutInflater)
    }

    val viewModel by viewModels<Cartviewmodel>()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val navController=findNavController(R.id.shopinghostfragment)

        binding.bottomnavigation.setupWithNavController(navController)

        lifecycleScope.launchWhenStarted {
            viewModel.cartproducts.collectLatest {
                when(it){
                    is com.example.ecomapp.Util.Resource.Success->{
                        val count=it.data?.size?:0
                        val bottomNavigationView=findViewById<BottomNavigationView>(R.id.bottomnavigation)
                        bottomNavigationView.getOrCreateBadge(R.id.cart_fragment).apply {
                            number=count
                            backgroundColor=resources.getColor(R.color.g_red)

                        }

                    }
                    else->Unit
                }
            }
        }


    }
}
