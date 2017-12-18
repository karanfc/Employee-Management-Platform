package com.pkf.karan.admin.weapp.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pkf.karan.admin.weapp.Adapters.FaqsAdapter;
import com.pkf.karan.admin.weapp.DataClasses.FaqData;
import com.pkf.karan.admin.weapp.LoginActivity;
import com.pkf.karan.admin.weapp.MainPackage.EngagementsActivity;
import com.pkf.karan.admin.weapp.MainPackage.FAQActivity;
import com.pkf.karan.admin.weapp.R;
import com.pkf.karan.admin.weapp.UserInformation;

import org.w3c.dom.Text;

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
 * {@link AskForAllotmentsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AskForAllotmentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AskForAllotmentsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Toolbar toolbar;

    private OnFragmentInteractionListener mListener;

    Button askForAllotment;
    Typeface font;
    UserInformation userInfo;
    private ProgressBar progressBar;

    TextView subtitle;
    EditText remark;





    public AskForAllotmentsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AskForAllotmentsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AskForAllotmentsFragment newInstance(String param1, String param2) {
        AskForAllotmentsFragment fragment = new AskForAllotmentsFragment();
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
        final View view = inflater.inflate(R.layout.fragment_ask_for_allotments, container, false);

        userInfo = (UserInformation)getActivity().getApplicationContext();
        font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/OpenSans-Regular.ttf");

        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        subtitle = (TextView)view.findViewById(R.id.subtitle);
        remark = (EditText)view.findViewById(R.id.remark);

        subtitle.setTypeface(font);
        remark.setTypeface(font);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("Ask for Allocation");

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

        askForAllotment = (Button)view.findViewById(R.id.askForAllocation);
        askForAllotment.setTypeface(font);
        askForAllotment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog(v);
            }
        });

        return view;
    }

    private void showConfirmDialog(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Do you want to ask for allocation?");



        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                confirmAskForAllocation();
            }
        });
        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        textView.setTypeface(font);
    }

    private void confirmAskForAllocation() {

        progressBar.setVisibility(View.VISIBLE);

        OkHttpClient client = new OkHttpClient();

        FormBody body = new FormBody.Builder()
                .add("employeeId", userInfo.getUserId())
                .build();


        final Request request = new Request.Builder()
                .url(userInfo.getServerUrl()+"/api/AllocationApi/AskForAllocation")
                .post(body)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Error",e.toString());
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String alreadyAsked;
                alreadyAsked = response.body().string();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);

                        if(alreadyAsked.equals("false"))
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                            builder.setMessage("Request successfully sent.");

                            builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();

                            TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                            textView.setTypeface(font);
                        }
                        else
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                            builder.setMessage("You have already asked for allocation.");

                            builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();

                            TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                            textView.setTypeface(font);
                        }
                    }
                });
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
