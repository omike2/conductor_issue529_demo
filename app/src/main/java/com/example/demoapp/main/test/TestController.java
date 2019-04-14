package com.example.demoapp.main.test;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.demoapp.R;
import com.example.demoapp.controller.BaseController;

public class TestController extends BaseController implements TestContract.View {
    public static final String TAG = "TestController";

    private TestView view;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        view = (TestView) inflater.inflate(R.layout.view_test, container, false);
        return view;
    }


    @Override
    protected String getTitle() {
        return "Test";
    }


}
