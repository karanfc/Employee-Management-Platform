package com.pkf.karan.admin.weapp.Fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pkf.karan.admin.weapp.Adapters.PendingAndTodaysEngagementsAdapter;
import com.pkf.karan.admin.weapp.DataClasses.EngagementData;
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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PendingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PendingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PendingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private RecyclerView pendingRecycler;
    private PendingAndTodaysEngagementsAdapter mAdapter;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    final List<EngagementData> Pendingdata=new ArrayList<>();
    LinearLayoutManager mLayoutManager;
    FloatingActionButton mFloatingActionButton;
    UserInformation userInfo;
    private String empId, empName, responseString;
    LinearLayout noengagementsView;
    TextView noEngagementsTitle;
    Typeface font;



    public PendingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PendingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PendingFragment newInstance(String param1, String param2) {
        PendingFragment fragment = new PendingFragment();
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
        final View view = inflater.inflate(R.layout.fragment_pending, container, false);
        userInfo = (UserInformation)getActivity().getApplicationContext();

        font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/OpenSans-Regular.ttf");


        noengagementsView = (LinearLayout)view.findViewById(R.id.noEngagementsLayout);
        noengagementsView.setVisibility(View.GONE);

        noEngagementsTitle = (TextView)view.findViewById(R.id.noEngagementsTitle);
        noEngagementsTitle.setTypeface(font);

        empId = userInfo.getUserId();

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadData();

            }
        });

        pendingRecycler = (RecyclerView)view.findViewById(R.id.pendingRecycler);
        pendingRecycler.setVisibility(View.VISIBLE);
        mFloatingActionButton = (FloatingActionButton)this.getActivity().findViewById(R.id.floating_action_button_pending);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        if(Pendingdata.size()==0)
        {
            LoadData();

        }

        setUpRecyclerView();

        pendingRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && mFloatingActionButton.getVisibility() == View.VISIBLE) {
                    mFloatingActionButton.hide();
                } else if (dy < 0 && mFloatingActionButton.getVisibility() != View.VISIBLE) {
                    mFloatingActionButton.show();
                }
            }
        });

        return view;
    }

    private void setUpRecyclerView() {
        mAdapter = new PendingAndTodaysEngagementsAdapter(getContext(), Pendingdata, "pending", progressBar, mFloatingActionButton, noengagementsView, pendingRecycler);
        pendingRecycler.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getContext());
        pendingRecycler.setLayoutManager(mLayoutManager);

    }

    private void LoadData() {


        noengagementsView.setVisibility(View.GONE);
        pendingRecycler.setVisibility(View.VISIBLE);

        progressBar.setVisibility(View.VISIBLE);
        OkHttpClient client = new OkHttpClient();


        final Request request = new Request.Builder()
                .url("http://13.127.11.204:10002/api/HomeApi/GetPendingAllocations" + "?" + "empId=" + empId)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("Error",e.toString());
                        progressBar.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                responseString = response.body().string();

                Log.e("Pending",responseString);
                Log.e("Code pending",String.valueOf(response.code()));

                if(String.valueOf(response.code()).equals("204"))
                {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mFloatingActionButton.setImageResource(R.drawable.ic_action_happy);

                            noengagementsView.setVisibility(View.VISIBLE);
                            pendingRecycler.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            Pendingdata.clear();
                            mAdapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                }

                else
                {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                Pendingdata.clear();

                                noengagementsView.setVisibility(View.GONE);
                                pendingRecycler.setVisibility(View.VISIBLE);

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
                                    Pendingdata.add(engagementData);
                                }

                                mFloatingActionButton.setImageResource(R.drawable.ic_action_name);

                                progressBar.setVisibility(View.GONE);
                                mAdapter.notifyDataSetChanged();
                                swipeRefreshLayout.setRefreshing(false);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
            }
        });
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
