package com.example.hom64chatapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hom64chatapp.Models.Message
import com.example.hom64chatapp.Models.User
import com.example.hom64chatapp.databinding.ItemUserRvBinding
import com.squareup.picasso.Picasso

class RvUserAdapter(val list: List<User>, val messageList: List<Message>, val uid:String, var rvClick: RvClick)
    :RecyclerView.Adapter<RvUserAdapter.Vh>() {

    inner class Vh(var itemRv:ItemUserRvBinding):RecyclerView.ViewHolder(itemRv.root){
        fun onBind(user: User){
            itemRv.txtDisplayNaeUserRv.text = user.displayName
            Picasso.get().load(user.photoUrl).into(itemRv.imageUserRv)
            itemRv.root.setOnClickListener {
                rvClick.onClick(user)
            }
            if (user.isOnline!!){
                itemRv.onlineLiner.visibility = View.VISIBLE
            }else{
                itemRv.onlineLiner.visibility = View.INVISIBLE
                itemRv.txtTimeUserRv.text = user.lastTime
            }

            for (message in messageList) {
                if ((message.toUid == user.uid && message.fromUid == uid) || (message.fromUid == user.uid && message.toUid == uid)){
                    itemRv.txtLastMessageUserRv.text = message.text
                }
            }

        }
    }

    interface RvClick{
        fun onClick(user: User)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemUserRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size
}