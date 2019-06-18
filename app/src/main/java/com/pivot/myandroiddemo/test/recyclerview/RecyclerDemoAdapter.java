package com.pivot.myandroiddemo.test.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pivot.myandroiddemo.R;

import java.util.ArrayList;

/**
 * RecyclerDemoView适配器
 */

public class RecyclerDemoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<String> mDatas;
    private ArrayList<Integer> mHeights = new ArrayList<>();

    public RecyclerDemoAdapter(Context context, ArrayList<String> datas) {
        mContext = context;
        mDatas = datas;
        
        for (int i = 0; i < mDatas.size(); i++) {
            mHeights.add(getRandomHeight());
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new NormalHolder(inflater.inflate(R.layout.recycler_demo_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {//在每次拉到顶部的时候，所有的Item会重新执行一次onBindViewHolder方法
        NormalHolder normalHolder = (NormalHolder) holder;
        normalHolder.mTV.setText(mDatas.get(position));

        //设置每个item的高度随机
//        ViewGroup.LayoutParams lp = normalHolder.mTV.getLayoutParams();
//        lp.height = mHeights.get(position);
//        normalHolder.mTV.setLayoutParams(lp);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {//该方法可以根据position的值来指定对应item的布局xml文件
        return super.getItemViewType(position);
    }

    private int getRandomHeight(){
        int randomHeight = 0;
        do{
            randomHeight = (int)(Math.random()*300);
        }while (randomHeight == 0);
        return randomHeight;
    }
    
    public class NormalHolder extends RecyclerView.ViewHolder {
        TextView mTV;
        NormalHolder(View itemView) {
            super(itemView);

            mTV = itemView.findViewById(R.id.recycler_demo_tv);
            mTV.setOnClickListener(v -> Toast.makeText(mContext, mTV.getText(), Toast.LENGTH_SHORT).show());
        }
    }
}
