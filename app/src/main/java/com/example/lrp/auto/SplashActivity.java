package com.example.lrp.auto;

/**
 * Created by admin on 2017/7/25.
 */


        import android.app.Activity;
        import android.content.Intent;
        import android.content.pm.PackageInfo;
        import android.content.pm.PackageManager;
        import android.content.pm.PackageManager.NameNotFoundException;
        import android.graphics.PixelFormat;
        import android.os.Bundle;
        import android.view.WindowManager;
        import android.widget.TextView;
        import java.util.Timer;
        import java.util.TimerTask;


public class SplashActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);

        setContentView(R.layout.splashscreen);

        //Display the current version number
        PackageManager pm = getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo("org.wordpress.android", 0);
            TextView versionNumber = (TextView) findViewById(R.id.versionNumber);
            versionNumber.setText("Version " + pi.versionName);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        final Intent it = new Intent(this, MainActivity.class); //你要转向的Activity
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startActivity(it); //跳转界面
            }
        };
        TimerTask task1 = new TimerTask() {
            @Override
            public void run() {
                finish(); //销毁活动
            }
        };
        timer.schedule(task, 2000);
        timer.schedule(task1, 2000);
    }
}