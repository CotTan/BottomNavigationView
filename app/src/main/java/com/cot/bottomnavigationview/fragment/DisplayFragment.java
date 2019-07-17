package com.cot.bottomnavigationview.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cot.bottomnavigationview.R;

/**
 * @author COT
 * @version 1.0
 * @since 2019-7-17
 */
public class DisplayFragment extends Fragment {
    public DisplayFragment() {
    }

    public static DisplayFragment newInstance() {
        return new DisplayFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_display, container, false);
    }


}