package cool.test.mycollege.IntroActivity;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import cool.test.mycollege.HomePage;
import cool.test.mycollege.R;


public class OnboardingActivity extends AppCompatActivity {
    private static final String TAG = "OnboardingActivity";
    //region Variables
    //static final String TAG = OnboardingActivity.class.getSimpleName()+" YOYO";
    public static final String PREFERENCES_FILE = "TakeIT_Settings";
    private static final Integer nPages = 5;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    CoordinatorLayout mCoordinator;

    ImageButton mNextBtn;
    Button mSkipBtn, mFinishBtn;
    ImageView[] indicators;

    int page =0;
    //endregion

    //region Design

    static int[] bgs = new int[]{
            R.drawable.page1_black,
            R.drawable.page2_lost_found,
            R.drawable.page3_event,
            R.drawable.page4_teach,
            R.drawable.page5_docs,
            R.drawable.admin};

    int colors[] = {
            R.color.cyan,
            R.color.colorAccent,
            R.color.dark_green,
            R.color.BlueGrey700,
            R.color.black_trans180,
            R.color.appIcon_back };

    static String title[] = {
            "Buy / Sell",
            "Lost and Found",
            "Events",
            "Attendence",
            "Documents",
            "ISM" };

    static String subtitle[] = {
            "Post an ad and get rid of the crap in your room",
            "Lost something? Inform everyone without spamming through mail",
            "See all upcoming events at one place and never miss a thing.",
            "Manage Your Attendence and Timetable",
            "See all important documents at one place without going through mail every time",
            "Welcome to ISM, create an account or login to get things started!" };

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_onboarding);
        Log.d(TAG, "onCreate: onBoarding");
        initVariables();

        setUpViewPager();
    }

    void setUpViewPager(){
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setCurrentItem(page);
        updateIndicators(page);

        final ArgbEvaluator evaluator = new ArgbEvaluator();

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int colorUpdate = (Integer) evaluator.evaluate(positionOffset,
                        gcolor(colors[position]), gcolor(colors[position == nPages ? position : position + 1]));

                mCoordinator.setBackgroundColor(colorUpdate);
                mViewPager.setBackgroundColor(colorUpdate);
            }

            @Override
            public void onPageSelected(int position) {
                page = position;
                updateIndicators(page);
                mViewPager.setBackgroundColor(gcolor(colors[position]));
                mNextBtn.setVisibility(position == nPages ? View.GONE : View.VISIBLE);
                mFinishBtn.setVisibility(position == nPages ? View.VISIBLE : View.GONE);
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
                finish();
               // saveSharedSetting(OnboardingActivity.this, HomePage.PREF_USER_FIRST_TIME, "false");
            }
        });

        mFinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //saveSharedSetting(OnboardingActivity.this, HomePage.PREF_USER_FIRST_TIME, "false");
            }
        });
    }

    int gcolor(int c){
        return ContextCompat.getColor(this,c);
    }

    void initVariables(){
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mNextBtn = findViewById(R.id.intro_btn_next);
        mSkipBtn = findViewById(R.id.intro_btn_skip);
        mFinishBtn = findViewById(R.id.intro_btn_finish);

        mCoordinator = findViewById(R.id.main_content);

        indicators = new ImageView[]{
                findViewById(R.id.intro_indicator_0),
                findViewById(R.id.intro_indicator_1),
                findViewById(R.id.intro_indicator_2),
                findViewById(R.id.intro_indicator_3),
                findViewById(R.id.intro_indicator_4),
                findViewById(R.id.intro_indicator_5)};

    }

    void updateIndicators(int position) {
        for (int i = 0; i < indicators.length; i++) {
            indicators[i].setBackgroundResource(
                    i == position ? R.drawable.indicator_selected : R.drawable.indicator_unselected
            );
        }
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        ImageView img;

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.frag_onboarding, container, false);

            TextView tv_Title = rootView.findViewById(R.id.section_Title);

            int position = getArguments().getInt(ARG_SECTION_NUMBER);
            tv_Title.setText(title[position]);

            TextView tv_Subtitle = rootView.findViewById(R.id.section_Subtitle);

            tv_Subtitle.setText(subtitle[position]);

            img = rootView.findViewById(R.id.section_img);

            img.setImageResource(bgs[position]);
//            GlideApp.with(container.getContext())
//                    .load(bgs[position])
//                    .dontAnimate()
//                    .into(img);

            return rootView;
        }
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {


        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return nPages+1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    /*public static void saveSharedSetting(Context ctx, String settingName, String settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(settingName, settingValue);
        editor.apply();
    }*/

}
