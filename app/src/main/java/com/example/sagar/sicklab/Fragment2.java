package com.example.sagar.sicklab;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sagar.database.GetDataDisease;
import com.example.sagar.database.GetDataDiseaseHistory;

import java.util.ArrayList;

public class Fragment2 extends Fragment {

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "Fragment2";
    public Fragment2() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_fragment2, container, false);


        FloatingActionButton myFab = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                Intent intent = new Intent(getActivity(), MakeReports.class);

                startActivityForResult(intent, 1);
            }
        });


        RecyclerView rv = (RecyclerView) view.findViewById(R.id.my_recycler_view);
//        mLayoutManager = new LinearLayoutManager(getActivity());
//        mRecyclerView.setLayoutManager(mLayoutManager);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);

        mAdapter = new MyRecyclerViewAdapter1(getDataSet());
        rv.setAdapter(mAdapter);


        rv.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), rv, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // do whatever


                        GetDataDisease d1=new GetDataDisease(getActivity());

                        Intent intent = new Intent(getActivity(), DiseaseDescription.class);
                        Bundle extras = new Bundle();
                        extras.putString("description", d1.getData(position+1, 7));
                        extras.putString("name", d1.getData(position+1, 1));
                        extras.putString("img_url", d1.getData(position+1, 6));
                        intent.putExtras(extras);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapter1) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter1
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + position);
            }
        });
    }

    private ArrayList<DataObject> getDataSet() {
        ArrayList results = new ArrayList<DataObject>();
        GetDataDiseaseHistory received = new GetDataDiseaseHistory(getActivity());
        int count = received.getNoOfData();


        for (int i = 0; i < count; i++) {
            DataObject obj = new DataObject("Start Date:" + received.getData(i + 1, 4) + "\n"+
                    "End Date:" + received.getData(i + 1, 5) + "    "+"\n"

                    ,
                    "No of Reports:" + received.getData(i + 1, 3),

                    received.getData(i + 1, 1)
            );
            results.add(i, obj);
        }


        return results;
    }
}
