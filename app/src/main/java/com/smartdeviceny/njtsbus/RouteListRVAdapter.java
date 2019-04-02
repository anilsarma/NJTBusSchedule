package com.smartdeviceny.njtsbus;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.smartdeviceny.njtsbus.route.RouteDetails;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public class RouteListRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements onMoveAndSwipedListener {

    class StopHolder {
      public String item="";

      public RouteDetails route;
      public double miles = 0;

      public StopHolder(String item, RouteDetails stop) {
          this.route = stop;
          this.item = item;
      }
    };
    private Context context;
    private List<StopHolder> mItems;
    private int color = 0;
    private View parentView;

    private final int TYPE_NORMAL = 1;
    private final int TYPE_FOOTER = 2;
    private final int TYPE_HEADER = 3;
    private final String FOOTER = "footer";
    private final String HEADER = "header";

    public RouteListRVAdapter(Context context) {
        this.context = context;
        mItems = new ArrayList();
    }

    public void setItems(List<RouteDetails> data) {
        this.mItems.clear();
        ArrayList<StopHolder> sh = new ArrayList<>();
        HashMap<String, String> unique = new HashMap<>();
        for(RouteDetails s:data) {
            if( unique.containsKey(s.route_short_name)) {
                continue;
            }
            StopHolder h = new StopHolder(s.route_short_name, s);
            sh.add(h);
            //unique.put(s.route_short_name, s.route_short_name);
        }
        this.mItems.addAll(sh);
        notifyDataSetChanged();
    }

    public void addItem(int position, RouteDetails insertData) {
        StopHolder h = new StopHolder("", insertData);
        mItems.add(position, h);
        notifyItemInserted(position);
    }

    public void addItems(List<RouteDetails> data) {
        StopHolder h = new StopHolder(HEADER, null);
        mItems.add(h);
        ArrayList<StopHolder> sh = new ArrayList<>();

        HashMap<String, String> unique = new HashMap<>();
        for(RouteDetails s:data) {
            if( unique.containsKey(s.route_short_name)) {
                continue;
            }
            StopHolder hh = new StopHolder(s.route_short_name, s);
            sh.add(hh);
            //unique.put(s.route_short_name, s.route_short_name);
        }
        sh.addAll(sh);
        notifyItemInserted(mItems.size() - 1);
    }

    public void addHeader() {
        StopHolder h = new StopHolder(HEADER, null);
        mItems.add(h);
        notifyItemInserted(mItems.size() - 1);
    }

    public void addFooter() {
        StopHolder h = new StopHolder(FOOTER, null);
        mItems.add(h);
        notifyItemInserted(mItems.size() - 1);
    }

    public void removeFooter() {
        mItems.remove(mItems.size() - 1);
        notifyItemRemoved(mItems.size());
    }

    public void setColor(int color) {
        this.color = color;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        parentView = parent;
        if (viewType == TYPE_NORMAL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view, parent, false);
            return new RecyclerViewHolder<RouteDetails>(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_footer, parent, false);
            return new FooterViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_header, parent, false);
            return new HeaderViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        StopHolder data = mItems.get(position);

        if (holder instanceof HeaderViewHolder) {
            final HeaderViewHolder recyclerViewHolder = (HeaderViewHolder) holder;
            recyclerViewHolder.header_text.setText("Footer " + position);
        }
        if (holder instanceof RecyclerViewHolder) {
            final RecyclerViewHolder<RouteDetails> recyclerViewHolder = (RecyclerViewHolder<RouteDetails> ) holder;
            recyclerViewHolder.data = data.route;
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_recycler_item_show);
            recyclerViewHolder.mView.startAnimation(animation);

            AlphaAnimation aa1 = new AlphaAnimation(1.0f, 0.1f);
            aa1.setDuration(400);
            recyclerViewHolder.rela_round.startAnimation(aa1);

            AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
            aa.setDuration(400);

            if (color == 1) {
                recyclerViewHolder.rela_round.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.google_blue)));
            } else if (color == 2) {
                recyclerViewHolder.rela_round.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.google_green)));
            } else if (color == 3) {
                recyclerViewHolder.rela_round.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.google_yellow)));
            } else if (color == 4) {
                recyclerViewHolder.rela_round.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.google_red)));
            } else {
                recyclerViewHolder.rela_round.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.gray)));
            }
            recyclerViewHolder.tv_recycler_item_1.setText("Route #" + data.route.arrival_time );
            recyclerViewHolder.tv_round_track.setText( data.route.route_short_name );

            recyclerViewHolder.tv_recycler_item_2.setText(" Stop #"  + data.route.stop_id);
            recyclerViewHolder.tv_recycler_item_3.setText("Trip #" + data.route.trip_id);
            recyclerViewHolder.rela_round.startAnimation(aa);

            recyclerViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayList<RouteDetails> routeDetails =  SQLSingleton.getInstance(context).getWrapper().getRoutesAtStop(recyclerViewHolder.data.stop_id);
                    Snackbar.make(view, "Stop " + recyclerViewHolder.data.stop_id + " " + recyclerViewHolder.data.route_short_name + " " + routeDetails.size(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    Intent intent = new Intent(context, StopTimeActivity.class);
                    PersistableBundle bundle = new PersistableBundle();

                    intent.putExtra("stop_id",recyclerViewHolder.data.stop_id);
                    intent.putExtra("trip_id",recyclerViewHolder.data.trip_id);
                    intent.putExtra("route_id",recyclerViewHolder.data.route_id);
                    intent.putExtra("route_short_name",recyclerViewHolder.data.route_short_name);
                    context.startActivity(intent);


//                    Intent intent = new Intent(context, ShareViewActivity.class);
//                    intent.putExtra("color", color);
//                    context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation
//                            ((Activity) context, recyclerViewHolder.rela_round, "shareView").toBundle());
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        StopHolder s = mItems.get(position);
        switch (s.item) {
            case HEADER:
                return TYPE_HEADER;
            case FOOTER:
                return TYPE_FOOTER;
            default:
                return TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(final int position) {
        mItems.remove(position);
        notifyItemRemoved(position);

//        Snackbar.make(parentView, context.getString(R.string.item_swipe_dismissed), Snackbar.LENGTH_SHORT)
//                .setAction(context.getString(R.string.item_swipe_undo), new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        addItem(position, mItems.get(position));
//                    }
//                }).show();
    }


    private class FooterViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progress_bar_load_more;

        private FooterViewHolder(View itemView) {
            super(itemView);
            progress_bar_load_more = itemView.findViewById(R.id.progress_bar_load_more);
        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView header_text;

        private HeaderViewHolder(View itemView) {
            super(itemView);
            header_text = itemView.findViewById(R.id.header_text);
        }
    }

}
