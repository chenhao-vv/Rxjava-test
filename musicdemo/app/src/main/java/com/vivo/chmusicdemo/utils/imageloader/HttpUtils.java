package com.vivo.chmusicdemo.utils.imageloader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {
    public static InputStream castielDownLoad(String key) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(key).openConnection();
        return conn.getInputStream();
    }
}
