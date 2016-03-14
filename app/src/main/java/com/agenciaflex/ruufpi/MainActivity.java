package com.agenciaflex.ruufpi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.agenciaflex.ruufpi.adapters.MyAdapter;
import com.agenciaflex.ruufpi.fragments.MainFragment;
import com.agenciaflex.ruufpi.fragments.RuDialogFragment;
import com.agenciaflex.ruufpi.listener.RecyclerViewOnItemTouchListener;
import com.agenciaflex.ruufpi.tasks.GetCardapioAsyncTask;
import com.agenciaflex.ruufpi.util.AndroidSystemUtil;
import com.agenciaflex.ruufpi.util.RuUfpiHelper;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.Calendar;
import java.util.GregorianCalendar;


public class MainActivity extends AppCompatActivity {

    private TextView subTitle;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private TabLayout tabLayout;
    private String dias[] = {"Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado"};
    private Calendar day = new GregorianCalendar();
    private Calendar day6 = new GregorianCalendar();
    private FloatingActionMenu floatingMenu;
    private FloatingActionButton floatingButton2;
    private FloatingActionButton floatingButton3;
    private View error;

    private ProgressBar pBar;
    private Toolbar toolbar;

    private int unidade_id;
    private SharedPreferences sp;

    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences(RuUfpiHelper.spString, MODE_PRIVATE);

        unidade_id = sp.getInt("unidade_id", 1);

        setContentView(R.layout.activity_main);

        pBar = (ProgressBar) findViewById(R.id.pBar);
        pBar.setVisibility(View.GONE);

        if (checkPlayServices()) {
            RuUfpiHelper.regId = AndroidSystemUtil.getRegistrationId(MainActivity.this);

            if (RuUfpiHelper.regId.trim().length() == 0) {
                RuUfpiHelper.registerIdInBackground(this);
            }
        }

        updateTime();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("RU UFPI");
        setSupportActionBar(toolbar);

        subTitle = (TextView) findViewById(R.id.subTitle);
        subTitle.setText(RuUfpiHelper.unidades[sp.getInt("unidade_id", 1) - 1] + " - " +
                String.format("%02d", day.get(Calendar.DAY_OF_MONTH)) + "/" + String.format("%02d", day.get(Calendar.MONTH) + 1) + " a " +
                String.format("%02d", day6.get(Calendar.DAY_OF_MONTH)) + "/" + String.format("%02d", day6.get(Calendar.MONTH) + 1));

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);

        mRecyclerView.setHasFixedSize(true);

        int ICON = R.drawable.cardapio_focu_50;
        RecyclerView.Adapter mAdapter = new MyAdapter(ICON, RuUfpiHelper.CARDAPIO_INDEX);

        mRecyclerView.setAdapter(mAdapter);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
        mRecyclerView.addOnItemTouchListener(new RecyclerViewOnItemTouchListener(MainActivity.this, drawer, RuUfpiHelper.CARDAPIO_INDEX));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }


        };
        drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Segunda"));
        tabLayout.addTab(tabLayout.newTab().setText("Terça"));
        tabLayout.addTab(tabLayout.newTab().setText("Quarta"));
        tabLayout.addTab(tabLayout.newTab().setText("Quinta"));
        tabLayout.addTab(tabLayout.newTab().setText("Sexta"));
        tabLayout.addTab(tabLayout.newTab().setText("Sábado"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        updateTab();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//        tabHost.setDistributeEvenly(true);
//        tabHost.setSelectedIndicatorColors(getResources().getColor(android.R.color.white));
//        tabHost.setCustomTabView(R.layout.tab_view, R.id.tv_tab);
//        tabHost.setViewPager(mPager);

        floatingMenu = (FloatingActionMenu) findViewById(R.id.floating_menu);
        FloatingActionButton floatingButton1 = (FloatingActionButton) findViewById(R.id.floating_button_1);
        floatingButton2 = (FloatingActionButton) findViewById(R.id.floating_button_2);
        floatingButton3 = (FloatingActionButton) findViewById(R.id.floating_button_3);

        floatingMenu.showMenuButton(true);
        floatingMenu.setClosedOnTouchOutside(true);

        floatingButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp.edit();
                if (sp.getInt("unidade_id", 1) == 1) {
                    editor.putInt("restaurante_id", 1);
                } else {
                    editor.putInt("restaurante_id", sp.getInt("unidade_id", 1) + 2);
                }
                editor.commit();
                updateAdapter();

                floatingMenu.close(true);
            }
        });
        floatingButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("restaurante_id", 2);
                editor.commit();
                updateAdapter();

                floatingMenu.close(true);
            }
        });

        floatingButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("restaurante_id", 3);
                editor.commit();
                updateAdapter();

                floatingMenu.close(true);
            }
        });


        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    floatingMenu.hideMenuButton(true);
                    subTitle.setVisibility(View.GONE);
                } else {
                    floatingMenu.showMenuButton(true);
                    subTitle.setVisibility(View.VISIBLE);
                }
            }
        });

        error = findViewById(R.id.error);

        findViewById(R.id.buttonRecarregar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error.setVisibility(View.GONE);
                pBar.setVisibility(View.VISIBLE);
                GetCardapioAsyncTask asynkTask = new GetCardapioAsyncTask(MainActivity.this);
                asynkTask.execute();
            }
        });
    }

    private void updateTime() {
        day = Calendar.getInstance();
        day.add(Calendar.DAY_OF_MONTH, -(day.get(Calendar.DAY_OF_WEEK) - 2));
        day6.setTime(day.getTime());
        day6.add(Calendar.DAY_OF_MONTH, 5);
    }

    public void updateAdapter() {
        int index = mPager.getCurrentItem();
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(index);


        if (sp.getInt("unidade_id", 1) != 1) {
//            floatingButton2.setVisibility(View.GONE);
//            floatingButton3.setVisibility(View.GONE);
            floatingMenu.setVisibility(View.GONE);
        } else {
            floatingMenu.setVisibility(View.VISIBLE);
//            floatingButton2.setVisibility(View.VISIBLE);
//            floatingButton3.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPlayServices();
        updateTime();
//        updateTab();
        subTitle.setText(RuUfpiHelper.unidades[sp.getInt("unidade_id", 1) - 1] + " - " +
                String.format("%02d", day.get(Calendar.DAY_OF_MONTH)) + "/" + String.format("%02d", day.get(Calendar.MONTH) + 1) + " a " +
                String.format("%02d", day6.get(Calendar.DAY_OF_MONTH)) + "/" + String.format("%02d", day6.get(Calendar.MONTH) + 1));
        if (unidade_id != sp.getInt("unidade_id", 1)) {
            updateTab();
            unidade_id = sp.getInt("unidade_id", 1);
            GetCardapioAsyncTask asynkTask = new GetCardapioAsyncTask(this);
            asynkTask.execute();
        } else {
            updateAdapter();
        }

    }

    private void updateTab() {
        int a = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        if (a == 1)
            a = 0;
        else
            a = a - 2;
        mPager.setCurrentItem(a);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_info) {
            FragmentTransaction ft = getSupportFragmentManager()
                    .beginTransaction();
            Fragment prev = getSupportFragmentManager()
                    .findFragmentByTag("dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);
            // Create and show the dialog.
            DialogFragment newFragment = RuDialogFragment
                    .newInstance("Informativos", "", MainActivity.this);
            newFragment.show(ft, "dialog");
            return true;
        }

        if (id == R.id.action_refresh) {
            GetCardapioAsyncTask asynkTask = new GetCardapioAsyncTask(this);
            asynkTask.execute();
        }

        return super.onOptionsItemSelected(item);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment mainFragment = new MainFragment();
            Bundle args = new Bundle();
            args.putInt("position", position);
            args.putSerializable("day", new GregorianCalendar(day.get(Calendar.YEAR), day.get(Calendar.MONTH), day.get(Calendar.DAY_OF_MONTH)));
            mainFragment.setArguments(args);

            return mainFragment;
        }

        public CharSequence getPageTitle(int position) {
            return dias[position].toUpperCase();
        }

        @Override
        public int getCount() {
            return dias.length;
        }
    }


    // UTIL
    public boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(MainActivity.this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, MainActivity.this, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                Toast.makeText(MainActivity.this, "PlayServices sem suporte", Toast.LENGTH_SHORT).show();
                finish();
            }
            return (false);
        }
        return (true);
    }

    public ProgressBar getpBar() {
        return pBar;
    }

    public ViewPager getmPager() {
        return mPager;
    }

    public FloatingActionMenu getFloatingMenu() {
        return floatingMenu;
    }

    public View getError() {
        return error;
    }
}
