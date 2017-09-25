package com.demo.smarthome.base.utils;

import android.util.Log;

import com.demo.smarthome.base.exception.NetException;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by liukun on 2016/2/25.
 */
public class JsonUtils {
    private static String TAG="JsonUtils";

    public static String getJsonByPost(String url, Object obj) throws NetException {
        if (url != null) {
            Log.i(TAG, "url=" + url);
        }
        try {
            String result = null;
            HttpClient client = HttpsHelper.getHttpsClient();
            // HttpClient client = HttpsHelper.getHttpClient();
            HttpPost post = new HttpPost(url);
            if (obj != null) {
                Gson gson = new Gson();
                String content = gson.toJson(obj);
                Log.i(TAG, "post param=" + content);
                StringEntity entity = new StringEntity(content, HTTP.UTF_8);
                entity.setContentType("application/json");
                post.setEntity(entity);
            }
            // 发送请求
            HttpResponse response = client.execute(post);
            // 获取状态行
            StatusLine line = response.getStatusLine();
            // 获取状态码
            int code = line.getStatusCode();
            if (code == 200) {
                HttpEntity resEntity = response.getEntity();
                InputStream is = resEntity.getContent();
                result = StreamUtil.stream2String(is, HTTP.UTF_8);
                Log.i(TAG, result);
            } else {
                Log.i(TAG, "code=" + code);
                throw new NetException("Request Error, Server Abnormal");
            }
            return result;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new NetException("Net encode error");
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            throw new NetException("Net protocol error");
        } catch (IOException e) {
            e.printStackTrace();
            throw new NetException("Net connnection error");
        }
    }

    public static String getJsonByPost(String url,  Map<String, String> param) throws NetException {

        if (url != null) {
            Log.i(TAG, "url=" + url);
        }
        try {
            String result = null;
            HttpClient client = HttpsHelper.getHttpsClient();
            HttpPost post = new HttpPost(url);
            if (param != null) {
                Gson gson = new Gson();
                String content = gson.toJson(param);
                Log.i(TAG, "post param=" + content);
                StringEntity entity = new StringEntity(content, HTTP.UTF_8);
                entity.setContentType("application/json");
                post.setEntity(entity);
            }
            // 发送请求
            HttpResponse response = client.execute(post);
            // 获取状态行
            StatusLine line = response.getStatusLine();
            // 获取状态码
            int code = line.getStatusCode();
            if (code == 200) {
                HttpEntity resEntity = response.getEntity();
                InputStream is = resEntity.getContent();
                result = StreamUtil.stream2String(is, HTTP.UTF_8);
                Log.i(TAG, result);
            } else {
                Log.i(TAG, "code=" + code);
                throw new NetException("Request Error, Server Abnormal");
            }
            return result;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new NetException("Net encode error");
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            throw new NetException("Net protocol error");
        } catch (IOException e) {
            e.printStackTrace();
            throw new NetException("Net connnection error");
        }
    }

    public static String getJsonByGet(String url) throws NetException {
        if (url != null) {
            Log.i(TAG, "url=" + url);
        }
        try {
            String result;
            HttpClient client = HttpsHelper.getHttpsClient();
            // 提交方式
            HttpGet get = new HttpGet(url);
            // 发送请求
            HttpResponse response = client.execute(get);
            // 获取状态行
            StatusLine line = response.getStatusLine();
            // 获取状态码
            int code = line.getStatusCode();
            if (code == 200) {
                HttpEntity resEntity = response.getEntity();
                InputStream is = resEntity.getContent();
                result = StreamUtil.stream2String(is, HTTP.UTF_8);
                Log.i(TAG, result);
            } else {
                Log.i(TAG, "code=" + code);
                throw new NetException("Request Error, Server Abnormal");
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "Net connnection error");
            throw new NetException("IOException");
        }
    }
}
