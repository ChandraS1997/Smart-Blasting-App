package com.smart_blasting_drilling.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import androidx.core.content.ContextCompat;
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.app.AppDelegate;
import com.smart_blasting_drilling.android.helper.StringHelper;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

      //  onNewIntent(getIntent());

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                /*if (BaseApplication.getPreferenceManger().getUserDetails() == null) {
                    startActivity(new Intent(SplashActivity.this, AuthActivity.class));
                    finishAffinity();
                } else {*/
                    //HomeActivity.openHomeActivity(SplashActivity.this);
                startActivity(new Intent(SplashActivity.this, AuthActivity.class));
                    finishAffinity();
                /*}*/
            }
        }, 1000);

    }

   /* @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getExtras() != null) {
            if (!StringHelper.isEmpty(intent.getExtras().getString("action_type"))) {
                AppDelegate.getInstance().setNotificationIntent(intent);
            }
        }
    }
*/
}