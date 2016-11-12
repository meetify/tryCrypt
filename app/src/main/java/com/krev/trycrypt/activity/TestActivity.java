package com.krev.trycrypt.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.krev.trycrypt.R;
import com.mingle.sweetpick.CustomDelegate;
import com.mingle.sweetpick.SweetSheet;

public class TestActivity extends AppCompatActivity {

    private SweetSheet sweetSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup group = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_test, null);
        sweetSheet = new SweetSheet(group);

        CustomDelegate customDelegate = new CustomDelegate(true,
                CustomDelegate.AnimationType.DuangAnimation);
        View view = LayoutInflater.from(this).inflate(R.layout.place, group, false);
        customDelegate.setCustomView(view);
        sweetSheet.setDelegate(customDelegate);
        setContentView(group);
        group.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sweetSheet.show();
            }
        });

//        view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //close
//                mSweetSheet3.dismiss();
//                }
//            });
    }
}
