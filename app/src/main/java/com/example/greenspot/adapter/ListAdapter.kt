package com.example.greenspot.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.greenspot.R
import com.example.greenspot.model.GSpot

class ListAdapter: RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var gSpotList = emptyList<GSpot>()
    internal var btnEditClick: (gSpot: GSpot) -> Unit = { _ -> }
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.tvTitle)
        val location: TextView = itemView.findViewById(R.id.tvPlace)
        val date: TextView = itemView.findViewById(R.id.tvDate)
        val btnEdit: CardView = itemView.findViewById(R.id.cardItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       return MyViewHolder(LayoutInflater
           .from(parent.context)
           .inflate(
               R.layout.single_item_view,
               parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = gSpotList[position]
        holder.apply {
            title.text = currentItem.title
            location.text = currentItem.place
            date.text = currentItem.date
            btnEdit.setOnClickListener {
                btnEditClick.invoke(currentItem)
            }
        }
        
    }

    override fun getItemCount(): Int {
        return gSpotList.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<GSpot>) {
        this.gSpotList = list
        notifyDataSetChanged()
    }
}

