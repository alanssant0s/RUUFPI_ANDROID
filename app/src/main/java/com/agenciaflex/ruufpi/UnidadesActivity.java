package com.agenciaflex.ruufpi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.agenciaflex.ruufpi.util.RuUfpiHelper;

/**
 * Created by alanssantos on 2/9/15.
 */
public class UnidadesActivity extends AppCompatActivity {

    private Spinner unidade;
    private Toolbar toolbar;

    private int ICON = R.drawable.ic_map_marker_radius_focus;


    private RecyclerView mRecyclerView;                           // Declaring RecyclerView
    private RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    private RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    private DrawerLayout Drawer;                                  // Declaring DrawerLayout

    private ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle

    private Button button;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences(RuUfpiHelper.spString, MODE_PRIVATE);

        setContentView(R.layout.activity_unit_select);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Unidades    ");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        unidade = (Spinner) findViewById(R.id.spinner);


        SpinnerAdapter adapter = new SpinnerAdapter(this,
                R.layout.spinner, RuUfpiHelper.unidades);

        unidade.setAdapter(adapter);

        unidade.setSelection(sp.getInt("unidade_id",1) -1);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("unidade_id", unidade.getSelectedItemPosition() + 1);

                if(unidade.getSelectedItemPosition() + 1 == 1)
                    editor.putInt("restaurante_id", 1);
                else
                    editor.putInt("restaurante_id", unidade.getSelectedItemPosition() + 3);

                editor.commit();

                Log.w("asdsadsads_________", sp.getInt("unidade_id", 1) + " ----- " + sp.getInt("restaurante_id", 1));

                RuUfpiHelper.registerIdInBackground(UnidadesActivity.this);
                UnidadesActivity.this.finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            super.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
