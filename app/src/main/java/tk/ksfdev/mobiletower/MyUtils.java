package tk.ksfdev.mobiletower;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 */

public class MyUtils {



    public static boolean isNetworkActive(){
        //ping Google site
        try {
            HttpURLConnection urlc = (HttpURLConnection) (new URL("http://clients3.google.com/generate_204").openConnection());
            urlc.setRequestProperty("User-Agent", "Android");
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(1500);
            urlc.connect();
            if (urlc.getResponseCode() == 204 && urlc.getContentLength() == 0){
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }




}
