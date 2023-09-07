package com.example.ecomapp.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecomapp.Data.Product
import com.example.ecomapp.databinding.SpecialRvItemBinding

class Specialproductadapter: RecyclerView.Adapter<Specialproductadapter.Specialproductviewholder>() {


    private val diffcallback=object:DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
          return oldItem.id==newItem.id

        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return   return oldItem==newItem
        }


    }

   val differ= AsyncListDiffer(this,diffcallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Specialproductviewholder {

        return Specialproductviewholder(
            SpecialRvItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: Specialproductviewholder, position: Int) {

val product=differ.currentList[position]
        holder.bind(product)

        holder.itemView.setOnClickListener{
            onClick?.invoke(product)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onClick :((Product)->Unit)?=null

    inner class Specialproductviewholder(private val binding:SpecialRvItemBinding)
        :RecyclerView.ViewHolder(binding.root)

    {


            fun bind(product: Product){
                binding.apply {
                    Glide.with(itemView).load(product.images[0]).into(imgAd)
                    Log.e("#", "lol "+product.images[0])

                    tvAdName.text=product.name
                    tvAdPrice.text=product.price.toString()


                }
            }


    }


}