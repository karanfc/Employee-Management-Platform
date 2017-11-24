package com.pkf.karan.admin.weapp.MainPackage;

import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.pkf.karan.admin.weapp.Fragments.FAQFragment;
import com.pkf.karan.admin.weapp.Fragments.NotificationsFragment;
import com.pkf.karan.admin.weapp.Fragments.PendingFragment;
import com.pkf.karan.admin.weapp.Fragments.ProfileFragment;
import com.pkf.karan.admin.weapp.Fragments.TodayFragment;
import com.pkf.karan.admin.weapp.Fragments.UpcomingFragment;
import com.pkf.karan.admin.weapp.R;

import java.util.ArrayList;
import java.util.List;

public class EngagementsActivity extends AppCompatActivity implements FAQFragment.OnFragmentInteractionListener,
ProfileFragment.OnFragmentInteractionListener, NotificationsFragment.OnFragmentInteractionListener,
TodayFragment.OnFragmentInteractionListener, UpcomingFragment.OnFragmentInteractionListener,
PendingFragment.OnFragmentInteractionListener{

    private BottomNavigationView bottomNavigation;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CoordinatorLayout tabContainer;
    private FrameLayout fragmentContainer;
    private Boolean isEngagementsTabOpen = true;
    Typeface font;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engagements);

        font = Typeface.createFromAsset(getAssets(),  "fonts/OpenSans-Regular.ttf");
        initializeViews();
    }

    private void initializeViews() {

        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/OpenSans-Regular.ttf");


        tabContainer = (CoordinatorLayout)findViewById(R.id.tabcontainer);
        fragmentContainer = (FrameLayout)findViewById(R.id.fragmentcontainer);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);


        bottomNavigation = (BottomNavigationView)findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigation);

        fragmentManager = getSupportFragmentManager();

        initializeTabs();

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                int flag = 0;
                switch (id) {
                    case R.id.menu_notifications:
                        fragment = new NotificationsFragment();
                        isEngagementsTabOpen = false;
                        break;

                    case R.id.menu_help:
                        fragment = new FAQFragment();
                        isEngagementsTabOpen = false;
                        break;

                    case R.id.menu_profile:
                        fragment = new ProfileFragment();
                        isEngagementsTabOpen = false;
                        break;

                    case R.id.menu_engagement:
                        isEngagementsTabOpen = true;
                        break;
                }

                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                if(!isEngagementsTabOpen)
                {
                    transaction.replace(R.id.fragmentcontainer, fragment).commit();
                    transaction.addToBackStack(null);

                    fragmentContainer.setVisibility(View.VISIBLE);
                    tabContainer.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.GONE);
                }
                else
                {

                    transaction.remove(fragment).commit();
                    transaction.addToBackStack(null);

                    fragmentContainer.setVisibility(View.GONE);
                    tabContainer.setVisibility(View.VISIBLE);
                    tabLayout.setVisibility(View.VISIBLE);
                }

                return true;
            }
        });


    }


    private void initializeTabs() {
        setupViewPager(viewPager);

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        changeTabsFont();

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PendingFragment(), "Pending");
        adapter.addFragment(new TodayFragment(), "Today");
        adapter.addFragment(new UpcomingFragment(), "Upcoming");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void changeTabsFont() {

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(font);
                }
            }
        }
    }
}
