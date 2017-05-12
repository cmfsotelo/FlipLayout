package com.csot.fliplayout.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.csot.fliplayout.lib.FlipLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.fliplayout1)
    FlipLayout fliplayout1;
    @BindView(R.id.fliplayout2)
    FlipLayout fliplayout2;
    @BindView(R.id.fliplayout3)
    FlipLayout fliplayout3;
    @BindView(R.id.fliplayout4)
    FlipLayout fliplayout4;
    @BindView(R.id.fliplayout5)
    FlipLayout fliplayout5;

    @OnClick(R.id.fliplayout1)
    public void flipComposerClick1() {
        fliplayout1.showNextChild();
    }

    @OnClick(R.id.fliplayout2)
    public void flipComposerClick2() {
        fliplayout2.showNextChild();
    }

    @OnClick(R.id.fliplayout3)
    public void flipComposerClick3() {
        fliplayout3.showNextChild();
    }

    @OnClick(R.id.fliplayout4)
    public void flipComposerClick4() {
        fliplayout4.showNextChild();
    }

    @OnClick(R.id.fliplayout5)
    public void flipComposerClick5() {
        fliplayout5.showNextChild();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }
}
