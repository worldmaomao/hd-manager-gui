package github.com.worldmaomao.http;

import com.alibaba.fastjson.JSON;
import github.com.worldmaomao.excpetion.AuthException;
import github.com.worldmaomao.excpetion.ServerException;
import github.com.worldmaomao.vo.ResponseVo;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 */
public class HttpUtil {

    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(new JwtInterceptor())
            .build();


    public ResponseVo post(String url, byte[] bytes) throws IOException {
        final Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(bytes))
                .build();
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
        switch (response.code()) {
            case 401:
                throw new AuthException("认证失败");
            case 500:
                throw new ServerException("服务器错误");
            case 201:
                return JSON.parseObject(response.body().bytes(), ResponseVo.class);
            default:
                throw new RuntimeException("Response code[" + response.code() + "]不处理");
        }

    }

    public ResponseVo put(String url, byte[] bytes) throws IOException {
        final Request request = new Request.Builder()
                .url(url)
                .put(RequestBody.create(bytes))
                .build();
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
        switch (response.code()) {
            case 401:
                throw new AuthException("认证失败");
            case 500:
                throw new ServerException("服务器错误");
            case 202:
                return JSON.parseObject(response.body().bytes(), ResponseVo.class);
            default:
                throw new RuntimeException("Response code[" + response.code() + "]不处理");
        }

    }

    public ResponseVo delete(String url, Map<String, String> params) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }
        final Request request = new Request.Builder()
                .url(urlBuilder.build())
                .delete()
                .build();
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
        switch (response.code()) {
            case 401:
                throw new AuthException("认证失败");
            case 500:
                throw new ServerException("服务器错误");
            case 202:
                return JSON.parseObject(response.body().bytes(), ResponseVo.class);
            default:
                throw new RuntimeException("Response code[" + response.code() + "]不处理");
        }

    }

    public ResponseVo get(String url, Map<String, String> params) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }
        final Request request = new Request.Builder()
                .url(urlBuilder.build())
                .get()
                .build();
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
        switch (response.code()) {
            case 401:
                throw new AuthException("认证失败");
            case 500:
                throw new ServerException("服务器错误");
            case 200:
                return JSON.parseObject(response.body().bytes(), ResponseVo.class);
            default:
                throw new RuntimeException("Response code[" + response.code() + "]不处理");
        }
    }


}
