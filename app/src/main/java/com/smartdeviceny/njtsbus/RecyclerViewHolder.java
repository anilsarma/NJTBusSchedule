package com.smartdeviceny.njtsbus;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartdeviceny.njtsbus.route.RouteDetails;
import com.smartdeviceny.njtsbus.route.Stop;

import androidx.recyclerview.widget.RecyclerView;

public class  RecyclerViewHolder<T>  extends RecyclerView.ViewHolder {
    public  View mView;
    public View rela_round;
    public TextView tv_round_track;
    public TextView tv_recycler_item_1;
    public TextView tv_recycler_item_2;
    public TextView tv_recycler_item_3;
    public T data;

    RecyclerViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        rela_round = itemView.findViewById(R.id.rela_round);
        tv_round_track = itemView.findViewById(R.id.tv_round_track);

        tv_recycler_item_1 = itemView.findViewById(R.id.tv_recycler_item_1);
        tv_recycler_item_2 = itemView.findViewById(R.id.tv_recycler_item_2);
        tv_recycler_item_3 = itemView.findViewById(R.id.tv_recycler_item_3);
    }
}
