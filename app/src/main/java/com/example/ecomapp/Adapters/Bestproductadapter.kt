package com.example.ecomapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecomapp.Data.Product
import com.example.ecomapp.databinding.BestDealsRvItemBinding
import com.example.ecomapp.databinding.ProductRvItemBinding
import com.google.firebase.firestore.core.View

class Bestproductadapter : RecyclerView.Adapter<Bestproductadapter.Bestdealsviewholder>() {

    var diffcalbback=object: DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return    oldItem.id==newItem.id

        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return   oldItem==newItem
        }


    }

    val differ= AsyncListDiffer(this,diffcalbback)

    inner  class Bestdealsviewholder(private val binding: ProductRvItemBinding) : RecyclerView.ViewHolder(binding.root) {


        fun bind(product: Product){

            binding.apply {
                Glide.with(itemView).load(product.images[0]).into(imgProduct)
                tvName.text=product.name
                tvPrice.text="$"+product.price.toString()
                tvNewPrice.text="$"+(product.price-(product.offerPercentage?.times(100)!!)).toString()
if(product.offerPercentage==null){
    tvNewPrice.visibility=android.view.View.INVISIBLE
}

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Bestdealsviewholder {

        return Bestdealsviewholder(
            ProductRvItemBinding.inflate(
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