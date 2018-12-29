package mapps.com.tourvision;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

public class ScheduleActivity extends AppCompatActivity {
    SharedPreferences sp;

    DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
    ViewPager mViewPager;

    ActionBar actionBar;
    ActionBar.TabListener tabListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        actionBar=getSupportActionBar();

        mDemoCollectionPagerAdapter =
                new DemoCollectionPagerAdapter(
                        getSupportFragmentManager());
        mViewPager = findViewById(R.id.pager);


        // Specify that tabs should be displayed in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create a tab listener that is called when the user changes tabs.
        tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                // show the given tab

                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // hide the given tab
            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // probably ignore this event
            }
        };

        sp=getSharedPreferences("details",MODE_PRIVATE);

        int k=getK();

        ArrayList<landmark> landmarks=readLandmarks();

        try {
            ArrayList<ArrayList<landmark>> clusters=cluster(landmarks,k);
//            applyToList(clusters);
            applyToTabs(clusters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public DemoCollectionPagerAdapter(FragmentManager manager) {
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

    public static class DemoObjectFragment extends Fragment {
        public static final String ARG_OBJECT = "object";

        ArrayList<String> lsst;
        public DemoObjectFragment(ArrayList<String> lst){
            lsst=lst;
        }

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            // The last two arguments ensure LayoutParams are inflated
            // properly.
            View rootView = inflater.inflate(
                    R.layout.schedule_tab, container, false);

            ListView listView=rootView.findViewById(R.id.day_schedule);

            listView.setAdapter(new ArrayAdapter<String>(inflater.getContext(),android.R.layout.simple_list_item_1,lsst));
            return rootView;
        }
    }

    int getK(){
        Date startDate=stringToDate(sp.getString("start_date","")),
            endDate=stringToDate(sp.getString("end_date",""));

        return dateDiff(startDate,endDate);
    }

    int dateDiff(Date a,Date b){
        long diff = b.getTime() - a.getTime();

        return (int)diff / (24 * 60 * 60 * 1000);
    }

    Date stringToDate(String sdate){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = format.parse(sdate);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    ArrayList<landmark> readLandmarks(){
        ArrayList<landmark> toRet=new ArrayList<>();
        try {
            String dest=sp.getString("dest","jodhpur");
            Scanner snr=new Scanner(new InputStreamReader(getAssets().open(dest+".txt")));
            int n=snr.nextInt();
            snr.nextLine();
            for(int i=0;i<n;i++){
                landmark lmark=new landmark();
                lmark.name=snr.nextLine();
                lmark.latitude=Float.parseFloat(snr.nextLine());
                lmark.longitude=Float.parseFloat(snr.nextLine());
                lmark.desc=snr.nextLine();
                lmark.rating=Float.parseFloat(snr.nextLine());

                ArrayList<String> reviews=new ArrayList<>();
                reviews.add(snr.nextLine());
                reviews.add(snr.nextLine());
                reviews.add(snr.nextLine());

                lmark.reviews=reviews;

                lmark.timeDuration=Integer.parseInt(snr.nextLine());
                lmark.ticketCost=Integer.parseInt(snr.nextLine());

                lmark.BTOD=Integer.parseInt(snr.nextLine());

                toRet.add(lmark);
            }

            return toRet;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    ArrayList<ArrayList<landmark>> cluster(ArrayList<landmark> lmarks,int k) throws Exception {
        ArrayList<Attribute> attrs=new ArrayList<>();
        attrs.add(new Attribute("latitude"));
        attrs.add(new Attribute("longitude"));
        Instances instances=new Instances("Points",attrs,0);

        for(landmark lmark:lmarks){
            double ar[]=new double[2];
            ar[0]=lmark.latitude;
            ar[1]=lmark.longitude;
            instances.add(new DenseInstance(1.0f,ar));
        }


        SimpleKMeans kmeans = new SimpleKMeans();

        kmeans.setSeed(10);

        //important parameter to set: preserver order, number of cluster.
        kmeans.setPreserveInstancesOrder(true);
        kmeans.setNumClusters(k);

        kmeans.buildClusterer(instances);

        // This array returns the cluster number (starting with 0) for each instance
        // The array has as many elements as the number of instances
        int[] assignments = kmeans.getAssignments();

        ArrayList<ArrayList<landmark>> toRet=new ArrayList<>();

        for(int i=0;i<kmeans.getNumClusters();i++){
            toRet.add(new ArrayList<landmark>());
        }
        int i=0;
        for(int clusterNum : assignments) {
            toRet.get(clusterNum).add(lmarks.get(i));
            i++;
        }

        return toRet;
    }

    void applyToList(ArrayList<ArrayList<landmark>> clusters){
        ArrayList<String> lstAr=new ArrayList<>();
        int i=1;
        for(ArrayList<landmark> cluster:clusters){
            lstAr.add("Day "+String.valueOf(i)+" : ");
            i++;
            for(landmark lmark:cluster){
                lstAr.add(lmark.name);
            }
        }

//        schedule.setAdapter(new ArrayAdapter<>(getBaseContext(),android.R.layout.simple_list_item_1,lstAr));
    }

    void applyToTabs(ArrayList<ArrayList<landmark>> clusters){
        int i=1;
        for(ArrayList<landmark> cluster:clusters){
            ArrayList<String> lstAr=new ArrayList<>();
            actionBar.addTab(
                    actionBar.newTab()
                            .setText("Day " + (i))
                            .setTabListener(tabListener));

            for(landmark lmark:cluster){
                lstAr.add(lmark.name);
            }

            mDemoCollectionPagerAdapter.addFragment(new DemoObjectFragment(lstAr),"Day "+(i));
            i++;
        }

        mViewPager.setAdapter(mDemoCollectionPagerAdapter);
//        schedule.setAdapter(new ArrayAdapter<>(getBaseContext(),android.R.layout.simple_list_item_1,lstAr));
    }

    public void next(View v){
        Intent intent=new Intent(this,GuideActivity.class);
        startActivity(intent);
        finish();
    }

    class landmark{
        String name,desc;
        float latitude,longitude,rating;
        int timeDuration,ticketCost,BTOD; // BTOD=Best Time of day
        ArrayList<String> reviews;
    }
}
