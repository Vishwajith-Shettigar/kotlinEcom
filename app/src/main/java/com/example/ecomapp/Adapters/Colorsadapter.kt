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


class Colorsadapter: RecyclerView.Adapter<Colorsadapter.Colorsviewholder>() {
    private var colorPicked=-1;

    inner class Colorsviewholder(val binding:ColorLayoutBinding):ViewHolder(binding.root){

        fun bind( color:Int,position: Int) {

            val imaegDraable=ColorDrawable(color)
        binding.imagecolor.setImageDrawable(imaegDraable)


if(colorPicked==position){ //color is selected

    binding.imageshadow.visibility=View.VISIBLE
    binding.imagepick.visibility=View.VISIBLE

}
            else{
    binding.imageshadow.visibility=View.INVISIBLE
    binding.imagepick.visibility=View.INVISIBLE
            }


        }


    }

    private val diffcallback=object :DiffUtil.ItemCallback<Int>(){
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return   oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return   oldItem==newItem
        }

    }
    val differ=AsyncListDiffer(this,diffcallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Colorsviewholder {
        return Colorsviewholder(
            ColorLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }



    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: Colorsviewholder, position: Int) {
        val color=differ.currentList[position]
        holder.bind(color,position)
        holder.itemView.setOnClickListener{
if(colorPicked>=0){
    notifyItemChanged(colorPicked)
}
            colorPicked=holder.adapterPosition
            notifyItemChanged(colorPicked)
            onItemClick?.invoke(color)
        }

    }
    var onItemClick:((Int)->Unit)?=null

}