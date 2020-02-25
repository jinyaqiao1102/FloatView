package com.hht.topview.ui.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hht.topview.R;
import com.hht.topview.entity.GridItemEntity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewGridAdapter extends RecyclerView.Adapter<RecyclerViewGridAdapter.GridViewHolder>{
    private Context context;
    private List<GridItemEntity> dataList;
    private int itemMargin = 20;

    public RecyclerViewGridAdapter(Context context,List<GridItemEntity> dataList) {
        this.context = context;
        this.dataList = dataList;
    }
    public RecyclerViewGridAdapter(Context context,List<GridItemEntity> dataList,int itemMargin) {
        this.context = context;
        this.dataList = dataList;
        this.itemMargin = itemMargin;
    }

    @NonNull
    @Override
    public RecyclerViewGridAdapter.GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout view = (LinearLayout) View.inflate(context, R.layout.layout_gride_item, null);
        LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layout.topMargin = itemMargin;
        layout.bottomMargin =itemMargin;
        GridViewHolder gridViewHolder = new GridViewHolder(view);
        view.setLayoutParams(layout);
        return gridViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewGridAdapter.GridViewHolder holder, int position) {
        holder.setData(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class GridViewHolder extends RecyclerView.ViewHolder{

        private ImageView icon;
        private TextView title;

        public GridViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.grid_image);
            title = (TextView) itemView.findViewById(R.id.grid_title);
        }

        public void setData(GridItemEntity data) {
//            mIvIcon.setImageResource(data.getImage());
            title.setText(data.getTitle());
        }
    }

    public int getItemMargin() {
        return itemMargin;
    }

    public void setItemMargin(int itemMargin) {
        this.itemMargin = itemMargin;
        this.notifyDataSetChanged();
    }
}
