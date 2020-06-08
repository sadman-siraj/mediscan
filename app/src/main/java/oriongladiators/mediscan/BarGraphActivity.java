package oriongladiators.mediscan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class BarGraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_graph);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarNew);
        setSupportActionBar(toolbar);

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);


        CombinedChart combinedChart = (CombinedChart) findViewById(R.id.chart);

//        ArrayList<BarEntry> group1 = new ArrayList<>();
//        group1.add(new BarEntry(4f, 0));
//        group1.add(new BarEntry(8f, 1));
//        group1.add(new BarEntry(6f, 2));
//        group1.add(new BarEntry(12f, 3));
//        group1.add(new BarEntry(18f, 4));
//        group1.add(new BarEntry(9f, 5));
//
//        ArrayList<BarEntry> group2 = new ArrayList<>();
//        group2.add(new BarEntry(6f, 0));
//        group2.add(new BarEntry(7f, 1));
//        group2.add(new BarEntry(8f, 2));
//        group2.add(new BarEntry(12f, 3));
//        group2.add(new BarEntry(15f, 4));
//        group2.add(new BarEntry(10f, 5));
//
//        BarDataSet barDataSet1 = new BarDataSet(group1, "Group 1");
//        //barDataSet1.setColor(Color.rgb(0, 155, 0));
//        barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);
//
//        BarDataSet barDataSet2 = new BarDataSet(group2, "Group 2");
//        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);
//
//        ArrayList<BarDataSet> dataSets = new ArrayList<>();
//        dataSets.add(barDataSet1);
//        dataSets.add(barDataSet2);

        CombinedData data = new CombinedData(getXAxisValues());
        data.setData(barDataOne());
        //data.setData(barDataTwo());
        data.setData(lineData());
        combinedChart.setData(data);
        combinedChart.setDescription("Formalin Test");
        combinedChart.animateXY(2000, 2000);
        combinedChart.invalidate();
    }


    private ArrayList<String> getXAxisValues() {
        ArrayList<String> labels = new ArrayList<>();
        labels.add("JAN");
        labels.add("FEB");
        labels.add("MAR");
        labels.add("APR");
        labels.add("MAY");
        labels.add("JUN");
        return labels;
    }

    public LineData lineData() {
        ArrayList<Entry> line = new ArrayList<>();
        line.add(new Entry(2f, 0));
        line.add(new Entry(4f, 1));
        line.add(new Entry(3f, 2));
        line.add(new Entry(6f, 3));
        line.add(new Entry(9f, 4));
        line.add(new Entry(4f, 5));
        LineDataSet lineDataSet = new LineDataSet(line, "Group 2");
        lineDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        LineData lineData = new LineData(getXAxisValues(), lineDataSet);
        return lineData;
    }

    public BarData barDataOne() {
        ArrayList<BarEntry> group1 = new ArrayList<>();
        group1.add(new BarEntry(4f, 0));
        group1.add(new BarEntry(8f, 1));
        group1.add(new BarEntry(6f, 2));
        group1.add(new BarEntry(12f, 3));
        group1.add(new BarEntry(18f, 4));
        group1.add(new BarEntry(9f, 5));
        BarDataSet barDataSet1 = new BarDataSet(group1, "Group 1");
        //barDataSet1.setColor(Color.rgb(0, 155, 0));
        barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData barDataOne = new BarData(getXAxisValues(), barDataSet1);
        return barDataOne;
    }

    public BarData barDataTwo() {
        ArrayList<BarEntry> group2 = new ArrayList<>();
        group2.add(new BarEntry(6f, 0));
        group2.add(new BarEntry(7f, 1));
        group2.add(new BarEntry(8f, 2));
        group2.add(new BarEntry(12f, 3));
        group2.add(new BarEntry(15f, 4));
        group2.add(new BarEntry(10f, 5));
        BarDataSet barDataSet2 = new BarDataSet(group2, "Group 2");
        //barDataSet1.setColor(Color.rgb(0, 155, 0));
        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData barDataTwo = new BarData(getXAxisValues(), barDataSet2);
        return barDataTwo;
    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }


}
