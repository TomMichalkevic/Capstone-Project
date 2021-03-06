package com.tomasmichalkevic.seevilnius;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tomasmichalkevic.seevilnius.utils.SavingUtilities;

import java.util.Objects;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
//
//Code heavily influenced from:
//https://github.com/Suleiman19/Android-Material-Design-for-pre-Lollipop/blob/master/MaterialSample/app/src/main/java/com/suleiman/material/activities/PagerActivity.java
//Icons were created by user Pause08 and downloaded from https://www.flaticon.com/packs/travel-110

public class WelcomeActivity extends AppCompatActivity {

    @BindView(R.id.container)
    ViewPager mViewPager;
    @BindView(R.id.intro_btn_next)
    ImageButton mNextBtn;
    @BindView(R.id.intro_btn_skip)
    Button mSkipBtn;
    @BindView(R.id.intro_btn_finish)
    Button mFinishBtn;
    @BindView(R.id.intro_indicator_0)
    ImageView zero;
    @BindView(R.id.intro_indicator_1)
    ImageView one;
    @BindView(R.id.intro_indicator_2)
    ImageView two;
    @BindView(R.id.main_content) CoordinatorLayout mCoordinator;
    @BindColor(R.color.cyan)
    int color1;
    @BindColor(R.color.orange)
    int color2;
    @BindColor(R.color.green)
    int color3;

    private ImageView[] indicators;
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        indicators = new ImageView[]{zero, one, two};
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setCurrentItem(page);
        updateIndicators(page);

        final int[] colorList = new int[]{color1, color2, color3};

        final ArgbEvaluator evaluator = new ArgbEvaluator();

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                int colorUpdate = (Integer) evaluator.evaluate(positionOffset, colorList[position], colorList[position == 2 ? position : position + 1]);
                mViewPager.setBackgroundColor(colorUpdate);

            }

            @Override
            public void onPageSelected(int position) {

                page = position;

                updateIndicators(page);

                switch (position) {
                    case 0:
                        mViewPager.setBackgroundColor(color1);
                        break;
                    case 1:
                        mViewPager.setBackgroundColor(color2);
                        break;
                    case 2:
                        mViewPager.setBackgroundColor(color3);
                        break;
                }


                mNextBtn.setVisibility(position == 2 ? View.GONE : View.VISIBLE);
                mFinishBtn.setVisibility(position == 2 ? View.VISIBLE : View.GONE);


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page += 1;
                mViewPager.setCurrentItem(page, true);
            }
        });

        mSkipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("welcomed", true);
                setResult(1, intent);
                finish();
                SavingUtilities.saveSharedSetting(WelcomeActivity.this, MainActivity.PREF_USER_FIRST_TIME, "false");
            }
        });

        mFinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("welcomed", true);
                setResult(1, intent);
                finish();
                //  update 1st time pref
                SavingUtilities.saveSharedSetting(WelcomeActivity.this, MainActivity.PREF_USER_FIRST_TIME, "false");

            }
        });


    }

    private void updateIndicators(int position) {
        for (int i = 0; i < indicators.length; i++) {
            indicators[i].setBackgroundResource(
                    i == position ? R.drawable.indicator_selected : R.drawable.indicator_unselected
            );
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return item.getItemId() == R.id.action_settings || super.onOptionsItemSelected(item);

    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        @BindView(R.id.section_img) ImageView img;
        @BindView(R.id.section_label) TextView sectionLabelTV;
        @BindView(R.id.section_desc) TextView sectionDescTV;
        @BindString(R.string.page_one_label) String label_one;
        @BindString(R.string.page_two_label) String label_two;
        @BindString(R.string.page_three_label) String label_three;
        @BindString(R.string.page_one_desc) String desc_one;
        @BindString(R.string.page_two_desc) String desc_two;
        @BindString(R.string.page_three_desc) String desc_three;

        final int[] bgs = new int[]{R.drawable.ic_004_map, R.drawable.ic_008_gps, R.drawable.ic_017_compass};
        String[] labels, descriptions;

        public PlaceholderFragment() {
        }

        static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_welcome, container, false);
            ButterKnife.bind(this, rootView);
            labels = new String[]{label_one, label_two, label_three};
            descriptions = new String[]{desc_one, desc_two, desc_three};
            sectionLabelTV.setText(labels[Objects.requireNonNull(getArguments()).getInt(ARG_SECTION_NUMBER) - 1]);
            sectionDescTV.setText(descriptions[getArguments().getInt(ARG_SECTION_NUMBER) - 1]);
            img.setBackgroundResource(bgs[getArguments().getInt(ARG_SECTION_NUMBER) - 1]);


            return rootView;
        }
    }

    class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);

        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}
