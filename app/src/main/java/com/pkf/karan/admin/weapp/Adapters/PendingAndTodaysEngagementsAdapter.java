package com.pkf.karan.admin.weapp.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.TransitionManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pkf.karan.admin.weapp.DataClasses.EngagementData;
import com.pkf.karan.admin.weapp.Fragments.PendingFragment;
import com.pkf.karan.admin.weapp.Fragments.TodayFragment;
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

import static com.pkf.karan.admin.weapp.R.id.input_email;
import static com.pkf.karan.admin.weapp.R.id.progressBar;
import static com.pkf.karan.admin.weapp.R.id.todayRecycler;

/**
 * Created by karan on 22/10/2017.
 */

public class PendingAndTodaysEngagementsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    String type;
    List<EngagementData> data;
    EngagementData current;
    String startDate = "", startTime, endDate = "", endTime, allocDate, allocTime;
    String dailyAllocationId;
    Boolean isCardExpanded = false;
    Typeface font;
    UserInformation userInfo;
    String responseString;
    String url;
    String location;
    ProgressBar pb;
    FloatingActionButton mFab;
    Boolean isHappy = false;
    LinearLayout noEngagementsView;
    RecyclerView recyclerView;
    TextView status;
    String employeeStatus;



    public PendingAndTodaysEngagementsAdapter(Context context, List<EngagementData> data, String type, ProgressBar progressBar, FloatingActionButton mFloatingActionButton, LinearLayout noengagementsView, RecyclerView recyclerView, TextView status){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.type = type;
        this.pb = progressBar;
        this.data=data;
        this.noEngagementsView = noengagementsView;
        this.recyclerView = recyclerView;
        this.mFab = mFloatingActionButton;
        this.status = status;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.pending_and_todays_engagement_card, parent,false);
        MyHolder holder=new MyHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        current=data.get(position);
        isCardExpanded = false;
        userInfo = (UserInformation)context.getApplicationContext();

        final MyHolder myHolder= (MyHolder) holder;

        font = Typeface.createFromAsset(context.getAssets(),  "fonts/OpenSans-Regular.ttf");

        myHolder.expandedCard.setVisibility(View.GONE);

        myHolder.engagementCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(myHolder.engagementStatus.getText().equals("Unconfirmed"))
                {
                    if(isCardExpanded)
                    {
                        myHolder.expandedCard.setVisibility(View.GONE);
                        isCardExpanded = false;
                    }
                    else
                    {
                        TransitionManager.beginDelayedTransition(myHolder.card);
                        myHolder.expandedCard.setVisibility(View.VISIBLE);
                        isCardExpanded = true;
                    }
                }
            }
        });

        myHolder.engagementTitle.setText(current.EngagementTitle);
        myHolder.engagementTitle.setTypeface(font);

        if(current.status.equals("UnConfirmed"))
        {
            myHolder.engagementStatus.setText("Unconfirmed");
            myHolder.engagementStatus.setTextColor(Color.parseColor("#F44336"));
        }
        else if(current.status.equals("Confirmed"))
        {
            myHolder.engagementStatus.setText("Confirmed");
            myHolder.engagementStatus.setTextColor(Color.parseColor("#76FF03"));
        }
        myHolder.engagementStatus.setTypeface(font);

        myHolder.clientName.setText("Client name: " + current.ClientName);
        myHolder.clientName.setTypeface(font);


        if(!current.allocDate.equals("null"))
        {
            Calendar c3 = Calendar.getInstance();
            c3.setTimeInMillis(Long.parseLong(current.allocDate.substring(current.allocDate.indexOf("(")+1,current.allocDate.indexOf(")"))));  //here your time in miliseconds
            allocDate = "" + c3.get(Calendar.DAY_OF_MONTH) + "/" + String.valueOf(Integer.parseInt(String.valueOf(c3.get(Calendar.MONTH)))+1) + "/" + c3.get(Calendar.YEAR);
            allocTime = "" + c3.get(Calendar.HOUR_OF_DAY) + ":" + c3.get(Calendar.MINUTE) + ":" + c3.get(Calendar.SECOND);
            myHolder.allocationDate.setText("Allocation date: " + allocDate);
            myHolder.allocationDate.setTypeface(font);
        }


        if(!current.StartDate.equals("null"))
        {
            Calendar cl = Calendar.getInstance();
            cl.setTimeInMillis(Long.parseLong(current.StartDate.substring(current.StartDate.indexOf("(")+1,current.StartDate.indexOf(")"))));  //here your time in miliseconds
            startDate = "" + cl.get(Calendar.DAY_OF_MONTH) + "/" + String.valueOf(Integer.parseInt(String.valueOf(cl.get(Calendar.MONTH)))+1) + "/" + cl.get(Calendar.YEAR);
            startTime = "" + cl.get(Calendar.HOUR_OF_DAY) + ":" + cl.get(Calendar.MINUTE) + ":" + cl.get(Calendar.SECOND);
        }

        if(!current.EndDate.equals("null"))
        {
            Calendar c2 = Calendar.getInstance();
            c2.setTimeInMillis(Long.parseLong(current.EndDate.substring(current.EndDate.indexOf("(")+1,current.EndDate.indexOf(")"))));  //here your time in miliseconds
            endDate = "" + c2.get(Calendar.DAY_OF_MONTH) + "/" + String.valueOf(Integer.parseInt(String.valueOf(c2.get(Calendar.MONTH)))+1) + "/" + c2.get(Calendar.YEAR);
            endTime = "" + c2.get(Calendar.HOUR_OF_DAY) + ":" + c2.get(Calendar.MINUTE) + ":" + c2.get(Calendar.SECOND);
        }


        myHolder.duration.setText(startDate + " - " + endDate);
        myHolder.duration.setTypeface(font);

        myHolder.office.setTypeface(font);
        myHolder.client.setTypeface(font);

        myHolder.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailyAllocationId = data.get(myHolder.getAdapterPosition()).dailyAllocationId;


                if(myHolder.office.isChecked() | myHolder.client.isChecked())
                {
                    if(myHolder.client.isChecked()) location = "Client";
                    else location = "Office";

                    showConfirmDialog(v, userInfo.getUserId(), dailyAllocationId, location);
                }
                else
                {
                    showAlertDialog(v);
                }
            }
        });

        myHolder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailyAllocationId = data.get(myHolder.getAdapterPosition()).dailyAllocationId;

                showRejectDialog(v, userInfo.getUserId(), dailyAllocationId);
            }
        });

    }

    private void showRejectDialog(View v, final String userId, final String dailyAllocationId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage("Do you want to launch a change request?");

        Log.e("entity", dailyAllocationId);

        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(context, TellUsTheIssue.class);
                intent.putExtra("rejectionType", "1");
                intent.putExtra("userId", userId);
                intent.putExtra("entityId", dailyAllocationId);
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

    private void showAlertDialog(View v) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage("Please select a location first");

        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();

        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        textView.setTypeface(font);
    }

    private void showConfirmDialog(View v, final String userId, final String dailyAllocationId, final String location) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage("Do you want to confirm the engagement?");


        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                 confirmEngagement(userId, dailyAllocationId, location);
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

    private void confirmEngagement(String userId, String dailyAllocationId, String location) {

        OkHttpClient client = new OkHttpClient();

        pb.setVisibility(View.VISIBLE);


        Log.e("DAID",dailyAllocationId);
        Log.e("userId", userId);

        FormBody body = new FormBody.Builder()
                .add("dailyAllocationId", dailyAllocationId)
                .add("userId", userId)
                .add("location",location)
                .build();


        final Request request = new Request.Builder()
                .url(userInfo.getServerUrl()+"/api/AllocationApi/UpdateDailyAllocationDetails")
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
                            if(type.equals("today"))
                            {
                                LoadData("today");
                            }
                            else
                            {
                                LoadData("pending");
                            }
                        }
                    }
                });

            }
        });


    }

    private void LoadData(final String type) {

        data.clear();

        OkHttpClient client = new OkHttpClient();

        if(type.equals("today"))
        {
            url = userInfo.getServerUrl()+"/api/HomeApi/GetTodaysAllocation" + "?" + "empId=" + userInfo.getUserId();
        }

        else
        {
            url = userInfo.getServerUrl()+"/api/HomeApi/GetPendingAllocations" + "?" + "empId=" + userInfo.getUserId();
        }

        Request request = new Request.Builder()
                .url(url)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                responseString = response.body().string();

                if(String.valueOf(response.code()).equals("204"))
                {
                    ((EngagementsActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setVisibility(View.GONE);
                            noEngagementsView.setVisibility(View.VISIBLE);
                            notifyDataSetChanged();
                            pb.setVisibility(View.GONE);
                            mFab.setImageResource(R.drawable.ic_action_happy);
                        }
                    });
                }

                else
                {

                    ((EngagementsActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                recyclerView.setVisibility(View.VISIBLE);
                                noEngagementsView.setVisibility(View.GONE);
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
                                    employeeStatus = engagementData.ClientLocation;

                                    if(engagementData.ClientLocation.equals("") | engagementData.ClientLocation.equals("Client") | engagementData.ClientLocation.equals("Office"))
                                    {
                                        engagementData.ClientLocation = "Active";
                                        employeeStatus = "Active";
                                        status.setText("Employee Status: " + employeeStatus);
                                    }

                                    if(!engagementData.EngagementTitle.equals(""))
                                    {
                                        data.add(engagementData);
                                    }
                                }

                                if(type.equals("today"))
                                {
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
                                        mFab.setImageResource(R.drawable.ic_action_happy);
                                    }
                                    else
                                    {
                                        mFab.setImageResource(R.drawable.ic_action_name);
                                    }
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
        LinearLayout expandedCard;
        TextView engagementTitle;
        TextView clientName;
        TextView duration;
        TextView allocationDate;
        TextView engagementStatus;
        CardView card;
        RadioGroup radioGroup;
        RadioButton client, office;
        ImageButton confirm, reject;


        // create constructor to get widget reference
        public MyHolder(final View itemView) {
            super(itemView);
            card = (CardView)itemView.findViewById(R.id.Card);
            engagementCard = (LinearLayout) itemView.findViewById(R.id.engagementCard);
            expandedCard = (LinearLayout)itemView.findViewById(R.id.expandedCard);
            expandedCard.setVisibility(View.GONE);
            engagementStatus = (TextView)itemView.findViewById(R.id.engagement_status);
            engagementTitle = (TextView) itemView.findViewById(R.id.engagement_name);
            clientName = (TextView) itemView.findViewById(R.id.client_name);
            allocationDate = (TextView)itemView.findViewById(R.id.allocation_date);
            radioGroup = (RadioGroup)itemView.findViewById(R.id.radioGroup);
            client = (RadioButton)itemView.findViewById(R.id.clientRadio);
            office = (RadioButton)itemView.findViewById(R.id.officeRadio);
            duration = (TextView)itemView.findViewById(R.id.duration);
            confirm = (ImageButton)itemView.findViewById(R.id.confirm);
            reject = (ImageButton)itemView.findViewById(R.id.reject);
        }
    }


}
