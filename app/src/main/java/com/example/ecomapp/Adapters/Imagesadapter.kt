package com.example.ecomapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.ecomapp.databinding.ImagesLayoutBinding

class Imagesadapter: RecyclerView.Adapter<Imagesadapter.Imagesviewholder>() {

    inner class Imagesviewholder(val binding:ImagesLayoutBinding):ViewHolder(binding.root){
        fun bind( image:String) {

 Glide.with(itemView).load(image).into(binding.productimage)

        }


    }

    private val diffcallback=object :DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
         return   oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return   oldItem==newItem
        }

    }
    val differ=AsyncListDiffer(this,diffcallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Imagesviewholder {
        return Imagesviewholder(
            ImagesLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: Imagesviewholder, position: Int) {
       val image=differ.currentList[position]
        holder.bind(image)

    }

}