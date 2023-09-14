package com.example.ecomapp.Adapters

import android.graphics.drawable.ColorDrawable
import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.ecomapp.Data.Address
import com.example.ecomapp.R
import com.example.ecomapp.databinding.AddressRvItemBinding

class Addressadapter: RecyclerView.Adapter<Addressadapter.Addressviewholder>() {

    var selectedAddress=-1
    inner class Addressviewholder(val binding:AddressRvItemBinding):ViewHolder(binding.root){
        fun bind(address: Address,selected:Boolean) {
            binding.apply {
                buttonAddress.text=address.addressTitle

                if(selected){
                    buttonAddress.background=ColorDrawable(itemView.context.resources.getColor(R.color.g_blue))
                }
                else{
                    buttonAddress.background=ColorDrawable(itemView.context.resources.getColor(R.color.white))

                }
            }



        }


    }

    private val diffUtil=object:DiffUtil.ItemCallback<Address>(){
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
           return oldItem.addressTitle==newItem.addressTitle && oldItem.fullname==newItem.fullname
        }

        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem==newItem
        }


    }

    val differ=AsyncListDiffer(this,diffUtil)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Addressviewholder {

        return Addressviewholder(AddressRvItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return  differ.currentList.size

    }

    override fun onBindViewHolder(holder: Addressviewholder, position: Int) {

        val address=differ.currentList.get(position)
        holder.bind(address,selectedAddress==position)
        holder.binding.buttonAddress.setOnClickListener{

            if(selectedAddress>=0)
                notifyItemChanged(selectedAddress)
            selectedAddress=holder.adapterPosition
            notifyItemChanged(selectedAddress)
            onclick?.invoke(address)
        }

    }
    init {
        differ.addListListener{_,_->
            notifyItemChanged(selectedAddress)

        }
    }
    var onclick:((Address)->Unit)?=null


}