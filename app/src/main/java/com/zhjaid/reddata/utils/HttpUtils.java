package com.zhjaid.reddata.utils;

import com.zhjaid.reddata.pojo.ApiPojo;
import com.zhjaid.reddata.pojo.ResultPojo;

import java.io.IOException;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {
    @Data
    @AllArgsConstructor
    public static class Parameter {
        private String parameter;
        private String data;
    }

    public static ResultPojo sendData(String url, List<Parameter> parameters, List<ApiPojo.Api.Request.Header> headers, String requestMethod) {
        try {
            FormBody.Builder builder = new FormBody.Builder();
            /**
             * 封装参数
             */
            if (parameters != null) {
                for (Parameter parameter : parameters) {
                    builder.add(parameter.getParameter(), parameter.getData());
                }
            }
            FormBody build = builder.build();
            OkHttpClient client = new OkHttpClient();
            Request.Builder requestBuilder = new Request.Builder();
            requestBuilder.url(url);

            /**
             *  封装请求头
             */
            if (headers != null) {
                for (ApiPojo.Api.Request.Header header : headers) {
                    requestBuilder.addHeader(header.getKey(), header.getValue());
                }
            }

            /**
             * 设置请求方式
             */
            if (requestMethod.toUpperCase().equals("GET")) {
                requestBuilder.get();
            } else if (requestMethod.toUpperCase().equals("POST")) {
                requestBuilder.post(build);
            } else if (requestMethod.toUpperCase().equals("PUT")) {
                requestBuilder.put(build);
            } else if (requestMethod.toUpperCase().equals("PATCH")) {
                requestBuilder.patch(build);
            } else if (requestMethod.toUpperCase().equals("DELETE")) {
                requestBuilder.delete(build);
            } else if (requestMethod.toUpperCase().equals("HEAD")) {
                requestBuilder.head();
            } else {
                requestBuilder.post(build);
            }
            /**
             * 构造请求
             */
            Request request = requestBuilder.build();
            Response execute = client.newCall(request).execute();
            ResultPojo pojo = new ResultPojo();
            pojo.setCode(execute.code());
            pojo.setMessage(execute.message());
            pojo.setType(execute.body().contentType().type());
            pojo.setTypeString(execute.body().contentType().toString());
            pojo.setBody(execute.body().string());
            pojo.setHeaders(execute.headers());
            return pojo;
        } catch (Exception e) {
            e.printStackTrace();
            ResultPojo pojo = new ResultPojo();
            pojo.setMessage(e.getMessage());
            pojo.setCode(5001);
            return pojo;
        }
    }
}
