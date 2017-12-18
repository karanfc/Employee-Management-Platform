package com.pkf.karan.admin.weapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pkf.karan.admin.weapp.Adapters.NotificationsAdapter;
import com.pkf.karan.admin.weapp.DataClasses.NotificationData;
import com.pkf.karan.admin.weapp.LoginActivity;
import com.pkf.karan.admin.weapp.MainPackage.EngagementsActivity;
import com.pkf.karan.admin.weapp.MainPackage.FAQActivity;
import com.pkf.karan.admin.weapp.R;
import com.pkf.karan.admin.weapp.UserInformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationsFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Toolbar toolbar;

    private RecyclerView notificationsRecycler;
    private NotificationsAdapter mAdapter;
    UserInformation userInfo;
    RelativeLayout noNotificationsLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar progressBar;
    TextView noNotificationsTitle;
    Typeface font;


    final List<NotificationData> data=new ArrayList<>();
    LinearLayoutManager mLayoutManager;

    String responseString;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationsFragment newInstance(String param1, String param2) {
        NotificationsFragment fragment = new NotificationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        userInfo = (UserInformation)getActivity().getApplicationContext();
        font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/OpenSans-Regular.ttf");

        noNotificationsLayout = (RelativeLayout)view.findViewById(R.id.noNotificationsLayout);
        noNotificationsLayout.setVisibility(View.GONE);

        noNotificationsTitle = (TextView)view.findViewById(R.id.noNotificationsTitle);
        noNotificationsTitle.setTypeface(font);

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setRefreshing(false);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progressBar.setVisibility(View.VISIBLE);
                LoadData();
            }
        });

        userInfo = (UserInformation)getActivity().getApplicationContext();


        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("Notifications");

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.menu_main_signout)
                {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }

                else if(item.getItemId() == R.id.menu_main_faq)
                {
                    Intent intent = new Intent(getActivity(), FAQActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });

        notificationsRecycler = (RecyclerView)view.findViewById(R.id.notificationRecycler);
        notificationsRecycler.setVisibility(View.VISIBLE);

        LoadData();

        setUpRecyclerView();


        return view;
    }

    private void LoadData() {

        progressBar.setVisibility(View.VISIBLE);


        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(userInfo.getServerUrl() + "/api/AllocationApi/GetNotificationList?userId=" + userInfo.getUserId())
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                data.clear();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        {
                            try {
                                responseString = response.body().string();
                                Log.e("Notif",responseString);

                                JSONArray jsonArray= new JSONArray(responseString);
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    NotificationData notificationData = new NotificationData();
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    notificationData.description = obj.getString("Remarks");
                                    Log.e("description",notificationData.description);
                                    notificationData.isNotifRead = !Boolean.valueOf(obj.getString("IsUnRead"));
                                    notificationData.notifType = obj.getString("NotificationType");
                                    notificationData.timeStamp = obj.getString("NotificationDate");
                                    data.add(notificationData);
                                }

                                if(jsonArray.length() == 0)
                                {
                                    noNotificationsLayout.setVisibility(View.VISIBLE);
                                    notificationsRecycler.setVisibility(View.GONE);
                                }

                                mAdapter.notifyDataSetChanged();
                                swipeRefreshLayout.setRefreshing(false);
                                progressBar.setVisibility(View.GONE);


                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });
            }
        });


    }

    private void setUpRecyclerView()
    {
        mAdapter = new NotificationsAdapter(getContext(), data);
        notificationsRecycler.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getContext());
        notificationsRecycler.setLayoutManager(mLayoutManager);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
