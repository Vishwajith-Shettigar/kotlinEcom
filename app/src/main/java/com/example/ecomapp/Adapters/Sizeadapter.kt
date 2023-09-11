package com.example.ecomapp.Adapters

import android.graphics.drawable.ColorDrawable
import com.example.ecomapp.databinding.ColorLayoutBinding



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.ecomapp.databinding.SizeLayoutBinding


class Sizeadapter: RecyclerView.Adapter<Sizeadapter.Sizeviewholder>() {
    private var colorPicked=-1;

    inner class Sizeviewholder(val binding:SizeLayoutBinding):ViewHolder(binding.root){

        fun bind( size:String,position: Int) {

            binding.sizetext.text=size
            if(colorPicked==position){ //color is selected

                binding.imageshadow.visibility=View.VISIBLE


            }
            else {
                binding.imageshadow.visibility = View.INVISIBLE
            }


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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Sizeviewholder {
        return Sizeviewholder(
            SizeLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }



    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: Sizeviewholder, position: Int) {
        val size=differ.currentList[position]
        holder.bind(size,position)
        holder.itemView.setOnClickListener{
            if(colorPicked>=0){
                notifyItemChanged(colorPicked)
            }
            colorPicked=holder.adapterPosition
            notifyItemChanged(colorPicked)
            onItemClick?.invoke(size)
        }


    }
    var onItemClick:((String)->Unit)?=null

}