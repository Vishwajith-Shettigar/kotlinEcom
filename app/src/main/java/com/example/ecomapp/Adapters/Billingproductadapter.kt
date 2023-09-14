package com.example.ecomapp.Adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.ecomapp.Data.Cartproduct
import com.example.ecomapp.Helper.getProductPrice
import com.example.ecomapp.databinding.BillingProductsRvItemBinding

class Billingproductadapter :Adapter<Billingproductadapter.Billingviewholder>() {

    inner class Billingviewholder(private val binding:BillingProductsRvItemBinding):ViewHolder(binding.root)
    {
        fun bind(billingproduct: Cartproduct) {

        binding.apply {
            Glide.with(itemView).load(billingproduct.product.images[0]).into(imageCartProduct)
            tvProductCartName.text=billingproduct.product.name
            tvBillingProductQuantity.text=billingproduct.quantity.toString()


            val priceafteroffer=billingproduct.product.offerPercentage.getProductPrice(billingproduct.product.price)
            tvProductCartPrice.text="$"+priceafteroffer.toString()

            imageCartProductColor.setImageDrawable(ColorDrawable(billingproduct.selectedColor?: Color.TRANSPARENT))

            tvCartProductSize.text=billingproduct.selectedsize?:"".also { imageCartProductSize.setImageDrawable(
                ColorDrawable(Color.TRANSPARENT)
            ) }

        }

        }

    }

    private val diffUtil=object:DiffUtil.ItemCallback<Cartproduct>(){
        override fun areItemsTheSame(oldItem: Cartproduct, newItem: Cartproduct): Boolean {
           return oldItem.product.id ==newItem.product.id
        }

        override fun areContentsTheSame(oldItem: Cartproduct, newItem: Cartproduct): Boolean {
            return oldItem.product==newItem.product
        }

    }
    val difffer=AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Billingviewholder {
                return Billingviewholder(
                    BillingProductsRvItemBinding.inflate(LayoutInflater.from(parent.context))
                )
    }

    override fun getItemCount(): Int {
       return difffer.currentList.size
    }

    override fun onBindViewHolder(holder: Billingviewholder, position: Int) {

        val billingproduct=difffer.currentList.get(position)
        holder.bind(billingproduct)
    }


}