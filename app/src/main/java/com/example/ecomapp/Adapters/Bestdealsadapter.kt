package com.example.ecomapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.AsyncListUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecomapp.Data.Product
import com.example.ecomapp.databinding.BestDealsRvItemBinding
import com.example.ecomapp.databinding.SpecialRvItemBinding
import java.util.Objects

class Bestdealsadapter:RecyclerView.Adapter<Bestdealsadapter.Bestdealsviewholder>() {

    var diffcalbback=object:DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return    oldItem.id==newItem.id

        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return   oldItem==newItem
        }


    }

    val differ=AsyncListDiffer(this,diffcalbback)

  inner  class Bestdealsviewholder(private val binding:BestDealsRvItemBinding) : RecyclerView.ViewHolder(binding.root) {


        fun bind(product: Product){

            binding.apply {
                Glide.with(itemView).load(product.images[0]).into(imgBestDeal)
                tvDealProductName.text=product.name
                tvOldPrice.text="$"+product.price.toString()
                if(product.offerPercentage==null){
                    tvNewPrice.visibility=android.view.View.INVISIBLE
                }
                tvNewPrice.text="$"+(product.price-(product.offerPercentage?.times(100)!!)).toString()


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Bestdealsviewholder {

        return Bestdealsviewholder(
            BestDealsRvItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            ))
    }

    override fun getItemCount(): Int {
      return  differ.currentList.size
    }

    override fun onBindViewHolder(holder: Bestdealsviewholder, position: Int) {
        holder.bind(differ.currentList.get(position))


    }


}