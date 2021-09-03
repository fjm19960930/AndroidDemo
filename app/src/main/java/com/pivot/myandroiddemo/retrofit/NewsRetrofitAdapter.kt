package com.pivot.myandroiddemo.retrofit

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.pivot.myandroiddemo.R
import com.pivot.myandroiddemo.activity.NewsWebActivity
import com.pivot.myandroiddemo.entity.NewsEntity
import com.zcolin.frame.imageloader.ImageLoaderUtils

class NewsRetrofitAdapter() : RecyclerView.Adapter<NewsRetrofitAdapter.MyHolder>() {
    private var mContext: Context? = null
    private var mDatas = mutableListOf<NewsEntity.NewsItemEntity>()
    
    constructor(context: Context) : this() {
        mContext = context
    }

    fun setDatas(datas: MutableList<NewsEntity.NewsItemEntity>) {
        mDatas.clear()
        mDatas = datas
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder((LayoutInflater.from(parent.context).inflate(R.layout.news_item_layout, parent, false)))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val data = mDatas[position]
        holder.newsTitle?.text = data.title ?: ""
        holder.newsAuthor?.text = data.author_name ?: ""
        holder.newsDate?.text = data.date ?: ""
        ImageLoaderUtils.displayImage<Context, String>(mContext, data.thumbnail_pic_s, holder.newsImage)
        holder.newsItemLayout?.setOnClickListener {
            val intent = Intent(mContext, NewsWebActivity::class.java)
            intent.putExtra("newsUrl", data.url)
            mContext?.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return mDatas.size
    }
    
    class MyHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!){
        var newsItemLayout: View? = null
        var newsTitle: TextView? = null
        var newsAuthor: TextView? = null
        var newsDate: TextView? = null
        var newsImage: ImageView? = null
        init {
            newsItemLayout = itemView?.findViewById(R.id.layout_news_item)!!
            newsTitle = itemView.findViewById(R.id.tv_news_title)!!
            newsAuthor = itemView.findViewById(R.id.tv_news_author)!!
            newsDate = itemView.findViewById(R.id.tv_news_date)!!
            newsImage = itemView.findViewById(R.id.iv_news_item)!!
        }
    }
}