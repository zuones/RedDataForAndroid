package com.zhjaid.reddata.pojo;

import lombok.Data;
import okhttp3.Headers;

@Data
public class ResultPojo {
    private int code;
    private String message;
    private String type;
    private String typeString;
    private String body;
    private Headers headers;
}
