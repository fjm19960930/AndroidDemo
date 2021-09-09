package com.pivot.myandroiddemo.livedata.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pivot.myandroiddemo.R

class PopupAdapter(context: Context): RecyclerView.Adapter<PopupAdapter.PopupViewHolder>() {
    private var mContext: Context? = null
    private var mData = mutableListOf<String>()
    private var mClickListener: (position: Int) -> Unit = {}

    fun setItemClickListener(e: (position: Int) -> Unit) {
        this.mClickListener = e
    }
    
    init {
        mContext = context
    }
    
    fun setData(data: MutableList<String>) {
        mData = data
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopupViewHolder {
        return PopupViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.popup_item_layout, parent, false))
    }

    override fun onBindViewHolder(holder: PopupViewHolder, position: Int) {
        holder.popupText?.text = mData[position]
        holder.popupText?.setOnClickListener {
            mClickListener(position)
        }
        if (itemCount == 1) {
            holder.popupText?.setBackgroundResource(R.drawable.radius_10_stroke_white_shape)
            return
        }
        when (position) {
            0 -> {
                holder.popupText?.setBackgroundResource(R.drawable.radius_10_stroke_white_shape_top)
            }
            (itemCount - 1) -> {
                holder.popupText?.setBackgroundResource(R.drawable.radius_10_stroke_white_shape_bottom)
                holder.splitLine?.visibility = View.GONE
            }
            else -> {
                holder.popupText?.setBackgroundResource(R.drawable.radius_0_stroke_white_shape)
            }
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }
    
    class PopupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var popupText: TextView? = null
        var splitLine: View? = null
        init {
            popupText = itemView.findViewById(R.id.popup_item_text)
            splitLine = itemView.findViewById(R.id.popup_item_split)
        }
    }
}