package com.pkf.karan.admin.weapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pkf.karan.admin.weapp.LoginActivity;
import com.pkf.karan.admin.weapp.MainPackage.EngagementsActivity;
import com.pkf.karan.admin.weapp.MainPackage.FAQActivity;
import com.pkf.karan.admin.weapp.R;
import com.pkf.karan.admin.weapp.UserInformation;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView nameLabel, name, emailLabel, email, phoneLabel, phone;

    private OnFragmentInteractionListener mListener;

    private Toolbar toolbar;
    UserInformation userInfo;
    Typeface font;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);

        userInfo = (UserInformation)getActivity().getApplicationContext();
        font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/OpenSans-Regular.ttf");

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("Profile");

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

        nameLabel = (TextView)view.findViewById(R.id.nameLabel);
        name = (TextView)view.findViewById(R.id.name);
        name.setText(userInfo.getUserName());
        if(userInfo.getUserName().equals("null"))
            name.setText("-");
        name.setTypeface(font);
        nameLabel.setTypeface(font);

        emailLabel = (TextView)view.findViewById(R.id.emailLabel);
        email = (TextView)view.findViewById(R.id.email);
        email.setText(userInfo.getUserEmail());
        if(userInfo.getUserEmail().equals("null"))
            email.setText("-");
        email.setTypeface(font);
        emailLabel.setTypeface(font);

        phoneLabel = (TextView)view.findViewById(R.id.phoneLabel);
        phone = (TextView)view.findViewById(R.id.phone);
        phone.setTypeface(font);
        phoneLabel.setTypeface(font);
        phone.setText(userInfo.getUserPhone());

        if(userInfo.getUserPhone().equals("null"))
            phone.setText("-");


        return view;
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
