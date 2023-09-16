package com.example.ecomapp.Adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.ecomapp.Data.order.Order
import com.example.ecomapp.Data.order.Orderstatus
import com.example.ecomapp.Data.order.getOrderstatus
import com.example.ecomapp.R
import com.example.ecomapp.databinding.OrderItemBinding

class Allordersadapter:Adapter<Allordersadapter.Ordersviewholder>() {

  inner  class Ordersviewholder(val binding:OrderItemBinding) :ViewHolder(binding.root) {

    fun bind(order:Order){

      binding.apply {
        tvOrderId.text=order.orderid.toString()
        tvOrderDate.text=order.date.toString()

        val resources = itemView.resources

        val colorDrawable = when (getOrderstatus(order.orderstatus)) {
          is Orderstatus.Ordered -> {
            ColorDrawable(resources.getColor(R.color.g_orange_yellow))
          }
          is Orderstatus.Confirmed -> {
            ColorDrawable(resources.getColor(R.color.g_green))
          }
          is Orderstatus.Delivered -> {
            ColorDrawable(resources.getColor(R.color.g_green))
          }
          is Orderstatus.Shipped -> {
            ColorDrawable(resources.getColor(R.color.g_green))
          }
          is Orderstatus.Canceled -> {
            ColorDrawable(resources.getColor(R.color.g_red))
          }
          is Orderstatus.Returned -> {
            ColorDrawable(resources.getColor(R.color.g_red))
          }
        }

        imageOrderState.setImageDrawable(colorDrawable)

      }
    }

    }

  private val diffUtil=object:DiffUtil.ItemCallback<Order>(){
    override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
     return  oldItem.orderid==newItem.orderid
    }

    override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {

      return  oldItem==newItem
    }

  }

  val differ=AsyncListDiffer(this,diffUtil)
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Ordersviewholder {

    return Ordersviewholder(
      OrderItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

    )

  }

  override fun getItemCount(): Int {
    return differ.currentList.size
  }

  override fun onBindViewHolder(holder: Ordersviewholder, position: Int) {

          holder.bind(differ.currentList.get(position))

    holder.itemView.setOnClickListener{
      onClick?.invoke(differ.currentList.get(position))
    }

  }

  var onClick:((Order)->Unit)?=null


}