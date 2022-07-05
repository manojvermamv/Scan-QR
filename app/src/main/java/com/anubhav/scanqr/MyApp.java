package com.anubhav.scanqr;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.PrintWriter;
import java.io.StringWriter;

public class MyApp extends Application {

    public static String appName;
    public static Context appContext;
    public static String packageName;
    public static String TAG = MyApp.class.getSimpleName();
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        this.uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(@NonNull Thread thread, @NonNull Throwable ex) {
                Intent intent = new Intent(getApplicationContext(), DebugActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                intent.putExtra("error", getStackTrace(ex));

                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 11111, intent, PendingIntent.FLAG_ONE_SHOT);

                AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000, pendingIntent);

                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(2);

                uncaughtExceptionHandler.uncaughtException(thread, ex);
            }
        });

        super.onCreate();
        appContext = this;
        packageName = getPackageName();
        appName = getString(R.string.app_name);

        initFirebaseApp();
    }

    private static final String PROJECT_ID = "trackx-8ffac";
    private static final String API_KEY = "AIzaSyCu0Rnzud5yyIpDqrwKbYABMt42ScYO9lQ";
    private static final String DATABASE_URL = "https://trackx-8ffac-default-rtdb.firebaseio.com";
    private static final String STORAGE_BUCKET = "trackx-8ffac.appspot.com";
    private static final String APP_ID = "1:757024137844:android:ecb3eb797bc23ab57b2721";
    private static final String WEB_CLIENT_ID = "757024137844-ob6phrr8g8kppqsfkafl136uh43mvhr4.apps.googleusercontent.com";

    private void initFirebaseApp() {
        FirebaseOptions.Builder optionsBuilder = new FirebaseOptions.Builder()
                .setProjectId(PROJECT_ID)
                .setApiKey(API_KEY)
                .setStorageBucket(STORAGE_BUCKET)
                .setDatabaseUrl(DATABASE_URL)
                .setApplicationId(APP_ID);

        FirebaseApp.initializeApp(getApplicationContext(), optionsBuilder.build());
    }

    private String getStackTrace(Throwable th) {
        Exception e = new Exception(th);
        StringWriter result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        while (th != null) {
            th.printStackTrace(printWriter);
            th = th.getCause();
        }
        String r = result.toString();

        //Uncomment below lines to write logs to local storage when your app crashes
        //Make sure you request storage permissions on devices with API 23+
        /*
        result = new StringWriter();
        printWriter = new PrintWriter(result);
        e.printStackTrace(printWriter);
        String r2 = result.toString();
        FileUtil.writeFile(FileUtil.getExternalStorageDir() + "/logcat.txt", r2);*/
        return r;
    }

}