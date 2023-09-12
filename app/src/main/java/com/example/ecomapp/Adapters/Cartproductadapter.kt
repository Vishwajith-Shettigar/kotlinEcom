package com.example.ecomapp.Adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecomapp.Data.Cartproduct
import com.example.ecomapp.Data.Product
import com.example.ecomapp.Helper.getProductPrice
import com.example.ecomapp.databinding.CartProductItemBinding
import com.example.ecomapp.databinding.SpecialRvItemBinding

class Cartproductadapter: RecyclerView.Adapter<Cartproductadapter.Cartproductviewholder>() {


    private val diffcallback=object:DiffUtil.ItemCallback<Cartproduct>(){
        override fun areItemsTheSame(oldItem: Cartproduct, newItem: Cartproduct): Boolean {
            return oldItem.product.id==newItem.product.id

        }

        override fun areContentsTheSame(oldItem: Cartproduct, newItem: Cartproduct): Boolean {
            return   return oldItem==newItem
        }


    }

    val differ= AsyncListDiffer(this,diffcallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Cartproductviewholder {

        return Cartproductviewholder(
            CartProductItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: Cartproductviewholder, position: Int) {

        val cartproduct=differ.currentList[position]
        holder.bind(cartproduct)

        holder.itemView.setOnClickListener {
            onProductClick?.invoke(cartproduct)
        }

        holder.binding.imagePlus.setOnClickListener{
            onPlusclick?.invoke(cartproduct)
        }
        holder.binding.imageMinus.setOnClickListener{
            onMinusclick?.invoke(cartproduct)
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onProductClick :((Cartproduct)->Unit)?=null
    var onPlusclick :((Cartproduct)->Unit)?=null
    var onMinusclick :((Cartproduct)->Unit)?=null

    inner class Cartproductviewholder( val binding:CartProductItemBinding)
        :RecyclerView.ViewHolder(binding.root)

    {


        fun bind(cartproduct: Cartproduct){
            binding.apply {
        Glide.with(itemView).load(cartproduct.product.images[0]).into(imageCartProduct)
                tvProductCartName.text=cartproduct.product.name
                tvCartProductQuantity.text=cartproduct.quantity.toString()

                val priceafteroffer=cartproduct.product.offerPercentage.getProductPrice(cartproduct.product.price)
                tvProductCartPrice.text="$"+priceafteroffer.toString()

                imageCartProductColor.setImageDrawable(ColorDrawable(cartproduct.selectedColor?: Color.TRANSPARENT))

                tvCartProductSize.text=cartproduct.selectedsize?:"".also { imageCartProductSize.setImageDrawable(ColorDrawable(Color.TRANSPARENT)) }



            }
        }


    }


}