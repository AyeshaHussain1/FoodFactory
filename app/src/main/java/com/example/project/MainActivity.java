package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    private static final int SPLASH_SCREEN = 4000;
private ProgressBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        splashscreen();
        connectionOfXml();

    }
    public void connectionOfXml()
    {
        bar=findViewById(R.id.bar);
    }
    public void splashscreen()
    {
        //Status_bar finish code line
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //splash screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
                bar.setVisibility(View.VISIBLE);

                Intent intent = new Intent(MainActivity.this, Login_Page.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);
    }
}
