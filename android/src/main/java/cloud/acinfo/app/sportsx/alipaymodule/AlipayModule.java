package cloud.acinfo.app.sportsx.alipaymodule;

import android.util.Log;

import com.alipay.sdk.app.AuthTask;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/10.
 */

public class AlipayModule extends ReactContextBaseJavaModule {
    public AlipayModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "AlipayModule";
    }

    @ReactMethod
    public void alipayAuth(final String authUrl, final Callback callback) {
        Runnable authRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    // 新建一个URL对象
                    URL url = new URL(authUrl);
                    // 打开一个HttpURLConnection连接
                    HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                    // 设置连接主机超时时间
                    urlConn.setConnectTimeout(5 * 1000);
                    //设置从主机读取数据超时
                    urlConn.setReadTimeout(5 * 1000);
                    // 设置是否使用缓存  默认是true
                    urlConn.setUseCaches(true);
                    // 设置为Post请求
                    urlConn.setRequestMethod("GET");
                    //urlConn设置请求头信息
                    //设置请求中的媒体类型信息。
                    urlConn.setRequestProperty("Content-Type", "application/json");
                    //设置客户端与服务连接类型
                    urlConn.addRequestProperty("Connection", "Keep-Alive");
                    // 开始连接
                    urlConn.connect();
                    // 判断请求是否成功
                    if (urlConn.getResponseCode() == 200) {
                        // 获取返回的数据
                        String result = streamToString(urlConn.getInputStream());
                        JSONObject json = new JSONObject(result);
                        String authInfoObject = json.getString("data");
                        int result_code = json.getInt("errcode");
                        Log.i("Alipay", "result code: " + result_code);

                        if (result_code == 0) {
                            Log.i("Alipay", "authInfo: " + authInfoObject);
                            JSONObject authInfoJson = new JSONObject(authInfoObject);

                            String authInfo = authInfoJson.getString("authinfo");

                            Log.i("authInfo", authInfo);

                            AuthTask authTask = new AuthTask(getCurrentActivity());
                            // 调用授权接口，获取授权结果
                            Map<String, String> auth_result = authTask.authV2(authInfo, true);
                            callback.invoke("{errcode: 0, data:" + auth_result.toString() +"}");
                            Log.i("ALIPAY", auth_result.toString());
                        }
                        Log.e("Alipay", "Get方式请求成功，result--->" + result);
                    } else {
                        Log.e("Alipay", "Get方式请求失败");
                        callback.invoke("{errcode: -1, msg: 'request failed'}");
                    }
                    // 关闭连接
                    urlConn.disconnect();
                } catch (Exception e) {
                    Log.e("Alipay", e.toString());
                }

            }


            private String streamToString(InputStream inputStream) {
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = inputStream.read(buffer)) != -1) {
                        baos.write(buffer, 0, len);
                    }
                    baos.close();
                    inputStream.close();
                    byte[] byteArray = baos.toByteArray();
                    return new String(byteArray);
                } catch (Exception e) {
                    Log.e("Alipay", e.toString());
                    return null;
                }
            };
        };

        // 必须异步调用
        Thread authThread = new Thread(authRunnable);
        authThread.start();
    }
}
