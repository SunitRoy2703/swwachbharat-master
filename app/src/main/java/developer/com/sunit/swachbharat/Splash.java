package developer.com.sunit.swachbharat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class Splash extends AppCompatActivity {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        final int splash_length = 2000;
        new Handler().postDelayed(
                new Runnable() {

                    @Override
                    public void run() {


                        startActivity(new Intent(Splash.this,LoginActivity.class));

                    }


                }, splash_length);


    }
}
