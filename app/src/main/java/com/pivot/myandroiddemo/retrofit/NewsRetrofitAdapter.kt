package com.pivot.myandroiddemo.retrofit

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
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
    
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyHolder {
        return MyHolder((LayoutInflater.from(parent?.context).inflate(R.layout.news_item_layout, parent, false)))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val data = mDatas.get(position)
        holder.newsTitle.text = data.title ?: ""
        holder.newsAuthor.text = data.author_name ?: ""
        holder.newsDate.text = data.date ?: ""
        ImageLoaderUtils.displayImage<Context, String>(mContext, data.thumbnail_pic_s, holder.newsImage)
        holder.newsItemLayout.setOnClickListener {
            val intent = Intent(mContext, NewsWebActivity::class.java)
            intent.putExtra("newsUrl", data.url)
            mContext?.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return mDatas.size
    }
    
    class MyHolder(itemView: View?) : RecyclerView.ViewHolder(itemView){
        var newsItemLayout: View = itemView?.findViewById(R.id.layout_news_item)!!
        var newsTitle: TextView = itemView?.findViewById(R.id.tv_news_title)!!
        var newsAuthor: TextView = itemView?.findViewById(R.id.tv_news_author)!!
        var newsDate: TextView = itemView?.findViewById(R.id.tv_news_date)!!
        var newsImage: ImageView = itemView?.findViewById(R.id.iv_news_item)!!
    }
}