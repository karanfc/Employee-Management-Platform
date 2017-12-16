package com.pkf.karan.admin.weapp.MainPackage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.pkf.karan.admin.weapp.Adapters.FaqsAdapter;
import com.pkf.karan.admin.weapp.DataClasses.FaqData;
import com.pkf.karan.admin.weapp.R;

import java.util.ArrayList;
import java.util.List;

public class FAQActivity extends AppCompatActivity {

    private RecyclerView faqsRecycler;
    private FaqsAdapter mAdapter;
    final List<FaqData> data=new ArrayList<>();
    LinearLayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        faqsRecycler = (RecyclerView)findViewById(R.id.faqRecycler);

        LoadData();

        setUpRecyclerView();

    }

    private void setUpRecyclerView()
    {
        mAdapter = new FaqsAdapter(this, data);
        faqsRecycler.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        faqsRecycler.setLayoutManager(mLayoutManager);
    }

    private void LoadData() {
        for(int i=0;i<5;i++)
        {
            FaqData faqdata = new FaqData();
            faqdata.faqQuestion = "Question:" + i;
            data.add(faqdata);
        }
    }
}
