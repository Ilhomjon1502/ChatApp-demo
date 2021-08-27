package com.example.hom64chatapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hom64chatapp.Models.Message
import com.example.hom64chatapp.databinding.ItemFromMessageBinding
import com.example.hom64chatapp.databinding.ItemToMessageBinding

class MessageUserAdapter (val list: List<Message>, val uid:String)
    :RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    inner class FromVh(var itemRv:ItemFromMessageBinding):RecyclerView.ViewHolder(itemRv.root){

        fun onBind(message: Message){
            itemRv.txtMessageText.text = message.text
            itemRv.txtDateMessage.text = message.date
        }
    }

    inner class ToVh(var itemRv:ItemToMessageBinding):RecyclerView.ViewHolder(itemRv.root){
        fun onBind(message: Message){
            itemRv.txtMessageText.text = message.text
            itemRv.txtDateMessage.text = message.date
            itemRv.imageUserMessage.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == 1){
            return FromVh(ItemFromMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }else{
            return ToVh(ItemToMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (getItemViewType(position) == 1){
            val fromVh = holder as FromVh
            fromVh.onBind(list[position])
        }else{
            val toVh = holder as ToVh
            toVh.onBind(list[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (list[position].fromUid == uid){
            return 1
        }else{
            return 2
        }
    }

    override fun getItemCount(): Int = list.size
}