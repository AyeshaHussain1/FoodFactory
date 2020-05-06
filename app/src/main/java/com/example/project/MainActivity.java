package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;


public class MainActivity extends AppCompatActivity {
    private static final int SPLASH_SCREEN = 2000;
private ProgressBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        splashscreen();
        connectionOfXml();
        bar.setVisibility(View.VISIBLE);

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
                Intent intent = new Intent(MainActivity.this, Login_Page.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);
    }
}
