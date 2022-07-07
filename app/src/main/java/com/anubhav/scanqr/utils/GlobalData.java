package com.anubhav.scanqr.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;

import com.anubhav.commonutility.LocaleHelper;
import com.anubhav.commonutility.spinner.SpinnerModel;
import com.anubhav.scanqr.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GlobalData {
    private Context context;

    public GlobalData(Context context) {
        this.context = context;
    }

    public static <T> T castObject(Object obj, Class<T> clazz) {
        return clazz.cast(obj);
    }

    public static <T> List<T> castList(Object obj, Class<T> clazz) {
        List<T> result = new ArrayList<T>();
        if (obj instanceof List<?>) {
            List<?> list = (List<?>) obj;
            //for (Object o : list) {
            for (int i = 0; i < list.size(); i++) {
                result.add(clazz.cast(list.get(i)));
            }
            return result;
        }
        return result;
    }

    public static <T> ArrayList<T> castArrayList(Object obj, Class<T> clazz) {
        ArrayList<T> result = new ArrayList<T>();
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return result;
    }

    public static int getHeight(Window window) {
        Display display = window.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenHeight = size.y;
        return screenHeight;
    }

    public static int getHeight(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenHeight = size.y;
        return screenHeight;
    }

    public static int getWidth(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return width;
    }

    public static String getUniqueString() {
        Calendar today = Calendar.getInstance();
        int date = today.get(Calendar.DATE);
        int month = today.get(Calendar.MONTH);
        int year = today.get(Calendar.YEAR);
        int hour = today.get(Calendar.HOUR);
        int minute = today.get(Calendar.MINUTE);
        int second = today.get(Calendar.SECOND);

        return date + "" + month + "" + year + "" + hour + "" + minute + "" + second;
    }

    public static void setEditTextCursor(EditText editText, @DrawableRes int drawableRes) {
        try {
            @SuppressLint("SoonBlockedPrivateApi") Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(editText, drawableRes);
        } catch (Exception ignored) {
        }
    }

    public static void disableTooltipText(BottomNavigationView navigationView) {
        try {
            Menu menu = navigationView.getMenu();
            for (int i = 0; i < menu.size(); i++) {
                MenuItem menuItem = menu.getItem(i);
                View view = navigationView.findViewById(menuItem.getItemId());
                view.setOnLongClickListener(v -> true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static void openAppStore(Context context) {
        try {
            Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
            Intent storeIntent = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            storeIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            context.startActivity(storeIntent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }

    public static void openAppStoreDeveloper(Context context) {
        String devId = "7809285959465313029";
        Uri uri = Uri.parse("https://play.google.com/store/apps/dev?id=" + devId);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    public static String removeFirstCountChar(String word, int count) {
        return word.substring(count);
    }

    public static String removeLastCountChar(String word, int count) {
        return word.substring(0, word.length() - count);
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static int getApiVersion() {
        return Build.VERSION.SDK_INT; // e.g. sdkVersion := 8;
    }

    public String getDeviceVersion() {
        return Build.VERSION.RELEASE; // e.g. myVersion := "1.6"
    }

    public String getDeviceVersionName() {
        // Names taken from android.os.build.VERSION_CODES
        String[] mapper = new String[]{
                "ANDROID_BASE", "ANDROID_BASE1.1", "CUPCAKE", "DONUT",
                "ECLAIR", "ECLAIR_0_1", "ECLAIR_MR1", "FROYO", "GINGERBREAD",
                "GINGERBREAD_MR1", "HONEYCOMB", "HONEYCOMB_MR1", "HONEYCOMB_MR2",
                "ICE_CREAM_SANDWICH", "ICE_CREAM_SANDWICH_MR1", "JELLY_BEAN", "JELLY_BEAN_MR1", "JELLY_BEAN_MR2",
                "KITKAT", "LOLLYPOP"
        };
        int index = Build.VERSION.SDK_INT - 1;
        String versionName = index < mapper.length ? mapper[index] : "UNKNOWN_VERSION"; // > KITKAT)
        return versionName;
    }

    public static double roundUp(double value, int roundAfterDecimal) {
        BigDecimal totaalAmt = new BigDecimal(value);
        BigDecimal strtotaalAmt = totaalAmt.setScale(roundAfterDecimal, RoundingMode.HALF_UP);
        double roundedValue = Double.parseDouble(String.valueOf(strtotaalAmt));
        return roundedValue;
    }

//    public static String getDeviceId(Context mContext) {
//        final TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
//        final String tmDevice, tmSerial, androidId;
//        tmDevice = "" + tm.getDeviceId();
//        tmSerial = "" + tm.getSimSerialNumber();
//        androidId = "" + android.provider.Settings.Secure.getString(mContext.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
//
//        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
//        String deviceId = deviceUuid.toString();
//
//        return deviceId;
//    }

//    public static String getIMEINumber(Context mContext) {
//        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
//        return tm.getDeviceId();
//    }


//    public static void setFont(ViewGroup group, Typeface font) {
//        int count = group.getChildCount();
//        View v;
//        for (int i = 0; i < count; i++) {
//            v = group.getChildAt(i);
//            if (v instanceof TextView || v instanceof EditText || v instanceof Button) {
//                ((TextView) v).setTypeface(font);
//            } else if (v instanceof EditText || v instanceof Button) {
//                ((EditText) v).setTypeface(font);
//            } else if (v instanceof Button) {
//                ((Button) v).setTypeface(font);
//            } else if (v instanceof ViewGroup)
//                setFont((ViewGroup) v, font);
//        }
//    }

//    public static void setThemeColor(ViewGroup group, Context svContext, boolean isDarkTheme) {
//        int count = group.getChildCount();
//        View v;
//        for (int i = 0; i < count; i++) {
//            v = group.getChildAt(i);
//            if (v instanceof TextView || v instanceof EditText || v instanceof Button) {
//                if (isDarkTheme) {
////                    ((TextView) v).setBackgroundResource(R.drawable.back_textviewdark);
//                    ((TextView) v).setTextColor(svContext.getResources().getColor(R.color.dark_fontcolortextview));
//                }else {
////                    ((TextView) v).setBackgroundResource(R.drawable.back_textviewlight);
//                    ((TextView) v).setTextColor(svContext.getResources().getColor(R.color.fontcolortextview));
//                }
//            } else if (v instanceof EditText || v instanceof Button) {
//                if (isDarkTheme) {
////                    ((EditText) v).setBackgroundResource(R.drawable.back_edittextl_dark);
//                    ((EditText) v).setTextColor(svContext.getResources().getColor(R.color.dark_fontcoloreditext));
//                }else {
////                    ((EditText) v).setBackgroundResource(R.drawable.back_edittext_light);
//                    ((EditText) v).setTextColor(svContext.getResources().getColor(R.color.fontcoloreditext));
//                }
//            } else if (v instanceof Button) {
//                if (isDarkTheme) {
//                    ((EditText) v).setBackgroundResource(R.drawable.back_button_dark);
//                    ((EditText) v).setTextColor(svContext.getResources().getColor(R.color.dark_fontcolorbutton));
//                }else {
//                    ((EditText) v).setBackgroundResource(R.drawable.back_button_light);
//                    ((EditText) v).setTextColor(svContext.getResources().getColor(R.color.fontcolorbutton));
//                }
//            } else if (v instanceof ViewGroup)
//                setThemeColor((ViewGroup) v, svContext, isDarkTheme);
//        }
//        if (isDarkTheme) {
////            group.setBackgroundResource(R.drawable.back_lay_dark);
//            setTheme(svContext, true);
//        }else {
////            group.setBackgroundResource(R.drawable.back_lay_light);
//            setTheme(svContext, false);
//        }
//    }

//    public static void setTheme(Context con, boolean isDarkTheme) {
//        if (isDarkTheme) {
//            con.setTheme(R.style.DarkTheme);
//        } else {
//            con.setTheme(R.style.LightTheme);
//        }
//    }

    public static void print(String strPrint) {
        System.out.println(strPrint + "..........print.........");
    }

    public static boolean createDirIfNotExists(String path) {
        boolean ret = true;
        File file = new File(Environment.getExternalStorageDirectory(), path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                //System.out.println("Problem creating Image folder");
                ret = false;
            }
        }
        return ret;
    }

    public static boolean createDirIfNotExist(File file) {
        boolean ret = true;
        if (!file.exists()) {
            if (!file.mkdirs()) {
                //System.out.println("Problem creating Image folder");
                ret = false;
            }
        }
        return ret;
    }

    public static String getRGBtoHEX(String RGB) {
        String[] rgb = RGB.split(",");
        int r = Integer.parseInt(rgb[0]);
        int g = Integer.parseInt(rgb[1]);
        int b = Integer.parseInt(rgb[2]);
        String hexColor = String.format("#%02x%02x%02x", r, g, b);
        return hexColor;
    }

    public static int getSpinnerPosByValue(List<String> spinnerItem, String myString) {
        int index = 0;
        for (int i = 0; i < spinnerItem.size(); i++) {
            System.out.println(spinnerItem.get(i) + "....spinner pos....." + myString);
            // For compare with id write [0] and for value write [1]
            if (spinnerItem.get(i).trim().equalsIgnoreCase(myString.trim())) {
                index = i;
                break;
            }
        }
        return index;
    }

    public static String getSpinnerTitleByValue(List<SpinnerModel> spinnerItem, String myString) {
        String str = "";
        for (int i = 0; i < spinnerItem.size(); i++) {
            System.out.println(spinnerItem.get(i) + "........." + myString);
            // For compare with id write [0] and for value write [1]
            if ((spinnerItem.get(i).getId().trim().equals(myString.trim()))) {
                str = spinnerItem.get(i).getTitle().trim();
                break;
            }
        }
        return str;
    }

    public static String getSpinnerImageUrlByValue(List<SpinnerModel> spinnerItem, String myString) {
        String str = "";
        for (int i = 0; i < spinnerItem.size(); i++) {
            System.out.println(spinnerItem.get(i) + "........." + myString);
            // For compare with id write [0] and for value write [1]
            if ((spinnerItem.get(i).getId().trim().equals(myString.trim()))) {
                str = spinnerItem.get(i).getStrImgUrl();
                break;
            }
        }
        return str;
    }

    public static boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files == null) {
                return true;
            }
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }

    public static String getDeviceType(Context context) {
        //		System.out.println("Quick Mobi Indfo "
        //				+ android.os.Build.VERSION.RELEASE + "  "
        //				+ android.os.Build.MODEL);
        return Build.MODEL;
    }

    public static String getOsVersion(Context context) {
        return Build.VERSION.RELEASE;
    }

    public static void SaveStringInFile(Context svContext, final String fileData, String fileName) throws IOException {
        File dir = new File(Global.defaultAppPath);
        dir.mkdirs();
        // create the file in which we will write the contents
        File file = new File(dir, fileName + ".txt");
        FileOutputStream os;
        try {
            os = new FileOutputStream(file);
            os.write(fileData.getBytes());
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void OpenInstagramPage(String InstaPath, Context context) {
        Uri uri = Uri.parse("http://instagram.com/_u/" + InstaPath);
        Intent instaPath = new Intent(Intent.ACTION_VIEW, uri);

        instaPath.setPackage("com.instagram.android");

        try {
            context.startActivity(instaPath);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/" + InstaPath)));
        }
    }

    public static void OpenWhatsappContact(String number, Context context) {
        Uri uri = Uri.parse("smsto:" + number);
        Intent i = new Intent(Intent.ACTION_SENDTO, uri);
        i.setPackage("com.whatsapp");
        //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i, null);
    }

    public static void OpenHikeContact(String number, Context context) {
        Uri uri = Uri.parse("smsto:" + number);
        Intent i = new Intent(Intent.ACTION_SENDTO, uri);
        i.setPackage("com.bsb.hike");
        //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i, null);
    }

    public static void openWebPage(Context context, String urlString) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.android.chrome");
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            // Chrome browser presumably not installed so allow user to choose instead
            intent.setPackage(null);
            context.startActivity(intent);
        }
    }

    public static String getStringRes(Context aContext, int strId) {
        String str = aContext.getResources().getString(strId);
        return str;
    }

    public static void SetLanguage(Context svContext) {
        String defLanguage = Preferences.readString(svContext, Preferences.LANGUAGE, "en");
        if (defLanguage.equalsIgnoreCase("en")) {
            LocaleHelper.setLocale(svContext, "en");
        } else {
            LocaleHelper.setLocale(svContext, "hi");
        }
    }

    public static void Fullscreen(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    public static void setStatusBarFullScreen(Activity activity) {
        setStatusBarFullScreen(activity, android.R.color.transparent);
    }

    /**
     * Sets the status bar fullscreen with color.
     *
     * @param activity activity reference required.
     * @param color    background color resources for statusBar background.
     */
    public static void setStatusBarFullScreen(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            View decorView = window.getDecorView();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }

            //set status background black
            window.setStatusBarColor(ContextCompat.getColor(activity, color));
        }
    }

    public static void setStatusBarFullScreen(Window window, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //set status background black
            window.setStatusBarColor(color);
        }
    }

    public static void setLightStatusBar(Activity activity) {
        setLightStatusBar(activity, R.color.transparent);
    }

    /**
     * Sets the light status bar with color.
     *
     * @param activity activity reference required.
     * @param color    background color resources for statusBar background.
     */
    public static void setLightStatusBar(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            View decorView = window.getDecorView();
            //set status text light
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            //set status background black
            window.setStatusBarColor(ContextCompat.getColor(activity, color));
        }
    }

    /**
     * Sets the status bar background color.
     *
     * @param activity  activity reference required.
     * @param color     background color resources for statusBar background.
     * @param lightText set true to show light text color in status bar.
     */
    public static void setStatusBarBackgroundColor(Activity activity, @ColorRes int color, boolean lightText) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            View decorView = window.getDecorView();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (lightText) {
                    //set status text light
                    decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                } else {
                    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
            }
            //set status background color
            window.setStatusBarColor(ContextCompat.getColor(activity, color));
        }
    }

    /**
     * Sets the status bar background drawable.
     *
     * @param activity  activity reference required.
     * @param drawable  background drawable resources.
     * @param lightText set true to show light text color in status bar.
     */
    public static void setStatusBarBackgroundDrawable(Activity activity, @DrawableRes int drawable, boolean lightText) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            View decorView = window.getDecorView();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (lightText) {
                    //set status text light
                    decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                } else {
                    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
            }

            //set status background color
            window.setStatusBarColor(ContextCompat.getColor(activity, android.R.color.transparent));
            //window.setNavigationBarColor(ContextCompat.getColor(activity, android.R.color.transparent));
            window.setBackgroundDrawable(ContextCompat.getDrawable(activity, drawable));
        }
    }

    public static void setStatusBarBackgroundTransformURL(Activity activity, String imageUrl) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            View decorView = window.getDecorView();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }

            //set status background color
            window.setStatusBarColor(ContextCompat.getColor(activity, android.R.color.transparent));
            //window.setNavigationBarColor(ContextCompat.getColor(activity, android.R.color.transparent));

            try {
                URL url = new URL(imageUrl);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                Drawable background = new BitmapDrawable(activity.getResources(), bmp);
                window.setBackgroundDrawable(background);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


            Window window = activity.getWindow();

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            // window.setNavigationBarColor(activity.getResources().getColor(R.color.transparent));

        }
    }

    public static String extractYoutubeVideoId(String ytUrl) {
        String vId = "";
        Pattern pattern = Pattern.compile("^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(ytUrl);
        if (matcher.matches()) {
            vId = matcher.group(1);
        }
        return vId;
    }

}