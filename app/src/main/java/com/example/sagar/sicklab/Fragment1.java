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
import android.widget.TextView;
import android.widget.Toast;

import com.example.sagar.database.GetDataDisease;
import com.example.sagar.database.GetUserData;

import java.util.ArrayList;

public class Fragment1 extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "Fragment1";

    public Fragment1() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_fragment1, container, false);

        RecyclerView rv = (RecyclerView) view.findViewById(R.id.my_recycler_view);
//        mLayoutManager = new LinearLayoutManager(getActivity());
//        mRecyclerView.setLayoutManager(mLayoutManager);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);


        mAdapter = new MyRecyclerViewAdapter(getDataSet());
        rv.setAdapter(mAdapter);


        rv.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), rv, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // do whatever


                        GetUserData get = new GetUserData(getActivity());
                        String user_type = get.getData("user_type");


                        GetDataDisease d1 = new GetDataDisease(getActivity());

                        if (user_type.equals("normal")) {
                            Intent intent = new Intent(getActivity(), DiseaseDescription.class);
                            Bundle extras = new Bundle();

                            String s1 = d1.getData(position+1, 7);
                            String s2 = d1.getData(position+1, 1);
                            String s3 = d1.getData(position+1, 6);
                            extras.putString("description", s1);
                            extras.putString("name", s2);
                            extras.putString("img_url", s3);
                            intent.putExtras(extras);
                            startActivity(intent);


                        } else {
                            Intent intent = new Intent(getActivity(), DiseaseDescriptionForDoctors.class);
                            Bundle extras = new Bundle();
                            extras.putString("description", d1.getData(position+1, 7));
                            extras.putString("name", d1.getData(position+1, 1));
                            extras.putString("img_url", d1.getData(position+1, 6));
                            intent.putExtras(extras);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );


        FloatingActionButton myFab = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                Intent intent = new Intent(getActivity(), MakeReports.class);

                startActivityForResult(intent, 1);
            }
        });


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + position);
            }
        });
    }

    private ArrayList<DataObject> getDataSet() {
        ArrayList results = new ArrayList<DataObject>();


        GetDataDisease received = new GetDataDisease(getActivity());
        int count = received.getNoOfData();


        for (int i = 0; i < count; i++) {
            DataObject obj = new DataObject(
                     "No of Reports:" + received.getData(i + 1, 3),
                    "Kathmandu",

                    received.getData(i + 1, 1)
            );
            results.add(i, obj);
        }


        return results;
    }
}
