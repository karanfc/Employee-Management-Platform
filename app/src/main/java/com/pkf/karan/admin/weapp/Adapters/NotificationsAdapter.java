package com.pkf.karan.admin.weapp.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pkf.karan.admin.weapp.DataClasses.EngagementData;
import com.pkf.karan.admin.weapp.DataClasses.NotificationData;
import com.pkf.karan.admin.weapp.R;

import java.util.Calendar;
import java.util.List;

/**
 * Created by dell on 04/11/2017.
 */

public class NotificationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private LayoutInflater inflater;
    private List<NotificationData> data;
    private NotificationData current;
    String notifDate, notifTime;


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
        myHolder.timeStamp.setTypeface(font);

        Calendar c3 = Calendar.getInstance();
        c3.setTimeInMillis(Long.parseLong(current.timeStamp.substring(current.timeStamp.indexOf("(")+1,current.timeStamp.indexOf(")"))));  //here your time in miliseconds
        notifDate = "" + c3.get(Calendar.DAY_OF_MONTH) + "/" + String.valueOf(Integer.parseInt(String.valueOf(c3.get(Calendar.MONTH)))+1) + "/" + c3.get(Calendar.YEAR);
        notifTime = "" + c3.get(Calendar.HOUR_OF_DAY) + ":" + c3.get(Calendar.MINUTE);
        myHolder.timeStamp.setText(notifDate);


        myHolder.description.setTypeface(font);
        myHolder.description.setText(current.description);


        myHolder.notifType.setTypeface(font);
        if(current.notifType.equals("ByAdmin"))
        {
            myHolder.notifType.setText("Admin");
        }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        TextView timeStamp;
        TextView description;
        TextView notifType;



        // create constructor to get widget reference
        public MyHolder(final View itemView) {
            super(itemView);
            timeStamp = (TextView) itemView.findViewById(R.id.timeStamp);
            description = (TextView) itemView.findViewById(R.id.description);
            notifType = (TextView)itemView.findViewById(R.id.notifType);
        }

    }

}
