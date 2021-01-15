package com.github.worldmaomao.http;

import com.github.worldmaomao.constant.GlobalVariables;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 */
public class JwtInterceptor implements Interceptor {
    @NotNull
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request originalRequest = chain.request();
        if (StringUtils.isNotEmpty(GlobalVariables.JWT)) {
            Request jwtRequest = originalRequest.newBuilder().header("Authorization", "Bearer " + GlobalVariables.JWT).build();
            return chain.proceed(jwtRequest);
        } else {
            return chain.proceed(originalRequest);

        }
    }
}
