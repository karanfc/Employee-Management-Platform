package com.pkf.karan.admin.weapp.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pkf.karan.admin.weapp.DataClasses.EngagementData;
import com.pkf.karan.admin.weapp.DataClasses.NotificationData;
import com.pkf.karan.admin.weapp.R;

import java.util.List;

/**
 * Created by dell on 04/11/2017.
 */

public class NotificationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private LayoutInflater inflater;
    List<NotificationData> data;
    NotificationData current;


    public NotificationsAdapter(Context context, List<NotificationData> data){
        this.context=context;
        inflater= LayoutInflater.from(context);

        this.data=data;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.notification_card, parent,false);
        NotificationsAdapter.MyHolder holder=new NotificationsAdapter.MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        current=data.get(position);


        Typeface font = Typeface.createFromAsset(context.getAssets(),  "fonts/OpenSans-Regular.ttf");

        NotificationsAdapter.MyHolder myHolder= (NotificationsAdapter.MyHolder) holder;
        myHolder.notificationTitle.setTypeface(font);
        myHolder.timeStamp.setTypeface(font);
        myHolder.description.setTypeface(font);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        TextView notificationTitle;
        TextView timeStamp;
        TextView description;



        // create constructor to get widget reference
        public MyHolder(final View itemView) {
            super(itemView);
            notificationTitle = (TextView) itemView.findViewById(R.id.notificationTitle);
            timeStamp = (TextView) itemView.findViewById(R.id.timeStamp);
            description = (TextView) itemView.findViewById(R.id.description);

        }

    }

}
