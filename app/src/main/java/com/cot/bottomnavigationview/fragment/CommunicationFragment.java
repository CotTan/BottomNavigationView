package com.cot.bottomnavigationview.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.cot.bottomnavigationview.R;

/**
 * @author COT
 * @version 1.0
 * @since 2019-7-17
 */
public class CommunicationFragment extends Fragment {
    public CommunicationFragment() {
    }

    public static CommunicationFragment newInstance() {
        return new CommunicationFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_communication, container, false);
    }


}
