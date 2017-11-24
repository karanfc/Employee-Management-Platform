package com.pkf.karan.admin.weapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.transition.TransitionManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pkf.karan.admin.weapp.DataClasses.EngagementData;
import com.pkf.karan.admin.weapp.DataClasses.FaqData;
import com.pkf.karan.admin.weapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by karan on 22/10/2017.
 */

public class FaqsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private Context context;
    private LayoutInflater inflater;
    List<FaqData> data;
    FaqData current;





    public FaqsAdapter(Context context, List<FaqData> data){
        this.context=context;
        inflater= LayoutInflater.from(context);

        this.data=data;
    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.faq_card, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder= (MyHolder) holder;
        current=data.get(position);

        Typeface font = Typeface.createFromAsset(context.getAssets(),  "fonts/OpenSans-Regular.ttf");

        myHolder.faqQuestion.setTypeface(font);
        myHolder.faqAnswer.setTypeface(font);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        CardView faqCard;
        TextView faqQuestion;
        TextView faqAnswer;
        ImageButton viewAnswer;
        Boolean isAnswerVisible = false;


        // create constructor to get widget reference
        public MyHolder(final View itemView) {
            super(itemView);

            faqCard = (CardView)itemView.findViewById(R.id.faqCard);
            faqQuestion = (TextView) itemView.findViewById(R.id.question);
            faqAnswer = (TextView) itemView.findViewById(R.id.answer);
            viewAnswer = (ImageButton) itemView.findViewById(R.id.viewAnswer);

            viewAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    if(isAnswerVisible)
                    {
                        //TransitionManager.beginDelayedTransition(faqCard);
                        faqAnswer.setVisibility(View.GONE);
                        isAnswerVisible = false;

                    }
                    else
                    {
                        TransitionManager.beginDelayedTransition(faqCard);
                        faqAnswer.setVisibility(View.VISIBLE);
                        isAnswerVisible = true;

                    }

                }
            });

        }

    }


}
