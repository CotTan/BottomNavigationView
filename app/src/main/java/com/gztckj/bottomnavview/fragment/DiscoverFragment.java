package com.gztckj.bottomnavview.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gztckj.bottomnavview.R;

public class DiscoverFragment extends Fragment {
    public DiscoverFragment() {
    }

    public static DiscoverFragment newInstance() {
        return new DiscoverFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }
}
