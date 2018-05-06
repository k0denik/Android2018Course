package com.afl.przedszkolelabapp;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link databaseViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class databaseViewFragment extends android.support.v4.app.Fragment {
    private ChildViewModel childViewModel;


    public databaseViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment databaseViewFragment.
     */
    public static databaseViewFragment newInstance() {
        databaseViewFragment fragment = new databaseViewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_database_view, container, false);
        Context thisContext = rootView.getContext();
        RecyclerView recyclerView = rootView.findViewById(R.id.childRecyclerView);
        final ChildViewAdapter adapter = new ChildViewAdapter(thisContext);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(thisContext));
        childViewModel = ViewModelProviders.of(this).get(ChildViewModel.class);
        childViewModel.getallChildren().observe(this, new Observer<List<Child>>() {
            @Override
            public void onChanged(@Nullable List<Child> children) {
                adapter.setChildren(children);
            }
        });


        return rootView;
    }

}
