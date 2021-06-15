package com.zhjaid.reddata.pojo;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class ImportPojo implements Serializable {

    private Info info;
    private List<ApiPojo.Api> item;

    @Data
    public static class Info {
        private String _postman_id;
        private String name;
        private String schema;
        private String info;
        private String date;
        @JSONField(format = "yyyy-MM-dd HH:mm:ss.SSS")
        private Date time;
    }
}
