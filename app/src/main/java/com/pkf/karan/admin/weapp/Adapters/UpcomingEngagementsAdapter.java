package com.pkf.karan.admin.weapp.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pkf.karan.admin.weapp.DataClasses.EngagementData;
import com.pkf.karan.admin.weapp.MainPackage.EngagementsActivity;
import com.pkf.karan.admin.weapp.MainPackage.TellUsTheIssue;
import com.pkf.karan.admin.weapp.R;
import com.pkf.karan.admin.weapp.UserInformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by karan on 22/11/2017.
 */

public class UpcomingEngagementsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private Context context;
    private LayoutInflater inflater;
    List<EngagementData> data;
    EngagementData current;
    String startDate, startTime, endDate, endTime;
    String dailyAllocationId;
    String EmployeeEngagementId;
    Typeface font;
    UserInformation userInfo;
    String responseString;
    ProgressBar pb;
    FloatingActionButton mfab;
    Boolean isHappy = false;
    LinearLayout noEngagementsView;
    RecyclerView upcomingRecycler;





    public UpcomingEngagementsAdapter(Context context, List<EngagementData> data, ProgressBar progressBar, FloatingActionButton mFloatingActionButton, RecyclerView upcomingRecycler, LinearLayout noengagementsView){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.pb = progressBar;
        this.mfab = mFloatingActionButton;
        this.noEngagementsView = noengagementsView;
        this.upcomingRecycler = upcomingRecycler;
        this.data=data;
    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.upcoming_engagments_card, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        current=data.get(position);
        userInfo = (UserInformation)context.getApplicationContext();

        final MyHolder myHolder= (MyHolder) holder;

        font = Typeface.createFromAsset(context.getAssets(),  "fonts/OpenSans-Regular.ttf");


        myHolder.engagementTitle.setText(current.EngagementTitle);
        myHolder.engagementTitle.setTypeface(font);

       if(current.status.equals("UnConfirmed"))
        {
            myHolder.engagementStatus.setText("Unconfirmed");
            myHolder.engagementStatus.setTextColor(Color.parseColor("#F44336"));
            myHolder.accept.setVisibility(View.VISIBLE);
            myHolder.reject.setVisibility(View.VISIBLE);
        }
        else if(current.status.equals("Confirmed"))
        {
            myHolder.engagementStatus.setText("Confirmed");
            myHolder.engagementStatus.setTextColor(Color.parseColor("#76FF03"));
            myHolder.accept.setVisibility(View.GONE);
            myHolder.reject.setVisibility(View.GONE);
        }

        myHolder.engagementStatus.setTypeface(font);

        myHolder.clientName.setText("Client name: " + current.ClientName);
        myHolder.clientName.setTypeface(font);



        Calendar cl = Calendar.getInstance();
        cl.setTimeInMillis(Long.parseLong(current.StartDate.substring(current.StartDate.indexOf("(")+1,current.StartDate.indexOf(")"))));  //here your time in milliseconds
        startDate = "" + cl.get(Calendar.DAY_OF_MONTH) + "/" + String.valueOf(Integer.parseInt(String.valueOf(cl.get(Calendar.MONTH)))+1) + "/" + cl.get(Calendar.YEAR);
        startTime = "" + cl.get(Calendar.HOUR_OF_DAY) + ":" + cl.get(Calendar.MINUTE) + ":" + cl.get(Calendar.SECOND);
        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(Long.parseLong(current.EndDate.substring(current.EndDate.indexOf("(")+1,current.EndDate.indexOf(")"))));  //here your time in milliseconds
        endDate = "" + c2.get(Calendar.DAY_OF_MONTH) + "/" + String.valueOf(Integer.parseInt(String.valueOf(c2.get(Calendar.MONTH)))+1) + "/" + c2.get(Calendar.YEAR);
        endTime = "" + c2.get(Calendar.HOUR_OF_DAY) + ":" + c2.get(Calendar.MINUTE) + ":" + c2.get(Calendar.SECOND);
        myHolder.duration.setText(startDate + " - " + endDate);
        myHolder.duration.setTypeface(font);


        myHolder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailyAllocationId = data.get(myHolder.getAdapterPosition()).dailyAllocationId;
                EmployeeEngagementId = data.get(myHolder.getAdapterPosition()).EmployeeEngagementId;
                showConfirmDialog(v, userInfo.getUserId(), EmployeeEngagementId);
            }
        });

        myHolder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailyAllocationId = data.get(myHolder.getAdapterPosition()).dailyAllocationId;
                EmployeeEngagementId = data.get(myHolder.getAdapterPosition()).EmployeeEngagementId;
                showRejectDialog(v, userInfo.getUserId(), EmployeeEngagementId);
            }
        });

    }

    private void showRejectDialog(View v, String userId, final String employeeEngagementId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage("Do you want to launch a change request?");


        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(context, TellUsTheIssue.class);
                intent.putExtra("rejectionType", "2");
                intent.putExtra("userId", userInfo.getUserId());
                intent.putExtra("entityId", employeeEngagementId);
                context.startActivity(intent);
            }
        });
        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        textView.setTypeface(font);
    }

    private void showConfirmDialog(View v, final String userId, final String EmployeeEngagementId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage("Do you want to accept the engagement?");



        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                confirmEngagement(userId, EmployeeEngagementId);
            }
        });
        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        textView.setTypeface(font);
    }

    private void confirmEngagement(String userId, String dailyAllocationId) {

        pb.setVisibility(View.VISIBLE);

        OkHttpClient client = new OkHttpClient();



        FormBody body = new FormBody.Builder()
                .add("employeeEngagementId", EmployeeEngagementId)
                .add("userId", userId)
                .build();


        final Request request = new Request.Builder()
                .url("http://13.127.11.204:10002/api/AllocationApi/AcceptUpcomingAllocation")
                .post(body)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                ((EngagementsActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(String.valueOf(response.code()).equals("200"))
                        {
                            LoadData();
                        }
                    }
                });

            }
        });


    }

    private void LoadData() {
        data.clear();

        final OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder()
                .url("http://13.127.11.204:10002/api/HomeApi/GetUpcomingAllocations" + "?" + "empId=" + userInfo.getUserId())
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                ((EngagementsActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("Error",e.toString());
                        Toast.makeText(context, "Check your internet connection", Toast.LENGTH_SHORT).show();
                        pb.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                responseString = response.body().string();

                Log.e("Upcoming", responseString);

                if(String.valueOf(response.code()).equals("204"))
                {
                    ((EngagementsActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            upcomingRecycler.setVisibility(View.GONE);
                            noEngagementsView.setVisibility(View.VISIBLE);
                            notifyDataSetChanged();
                            pb.setVisibility(View.GONE);
                        }
                    });
                }


                else
                {
                    upcomingRecycler.setVisibility(View.VISIBLE);
                    noEngagementsView.setVisibility(View.GONE);

                    ((EngagementsActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                JSONArray allotments = new JSONArray(responseString);
                                for (int i = 0; i < allotments.length(); i++) {
                                    JSONObject allotmentObj = allotments.getJSONObject(i);
                                    EngagementData engagementData = new EngagementData();
                                    engagementData.ClientName = allotmentObj.getString("ClientName");
                                    engagementData.EngagementTitle = allotmentObj.getString("EngagementName");
                                    engagementData.StartDate = allotmentObj.getString("StartDate");
                                    engagementData.EndDate = allotmentObj.getString("EndDate");
                                    engagementData.ClientLocation = allotmentObj.getString("CurrentLocation");
                                    engagementData.allocDate = allotmentObj.getString("AllocationDate");
                                    engagementData.dailyAllocationId = allotmentObj.getString("DailyAllocationId");
                                    engagementData.status = allotmentObj.getString("Status");
                                    engagementData.EmployeeEngagementId = allotmentObj.getString("EmployeeEngagementId");
                                    data.add(engagementData);
                                }

                                for(int i=0; i<data.size(); i++)
                                {
                                    if(data.get(i).status.equals("UnConfirmed"))
                                    {
                                        isHappy = false;
                                        break;
                                    }
                                    else
                                    {
                                        isHappy = true;
                                    }
                                }

                                if(isHappy)
                                {
                                    mfab.setImageResource(R.drawable.ic_action_happy);
                                }
                                else
                                {
                                    mfab.setImageResource(R.drawable.ic_action_name);
                                }
                                notifyDataSetChanged();
                                pb.setVisibility(View.GONE);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        LinearLayout engagementCard;
        TextView engagementTitle;
        TextView clientName;
        TextView duration;
        TextView engagementStatus;
        ImageButton accept, reject;

        // create constructor to get widget reference
        public MyHolder(final View itemView) {
            super(itemView);
            engagementCard = (LinearLayout) itemView.findViewById(R.id.engagementCard);
            engagementStatus = (TextView)itemView.findViewById(R.id.engagement_status);
            engagementTitle = (TextView) itemView.findViewById(R.id.engagement_name);
            clientName = (TextView) itemView.findViewById(R.id.client_name);

            duration = (TextView)itemView.findViewById(R.id.duration);
            accept = (ImageButton)itemView.findViewById(R.id.accept);
            reject = (ImageButton)itemView.findViewById(R.id.reject);
        }
    }


}

