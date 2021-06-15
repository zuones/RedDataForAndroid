package com.zhjaid.reddata.pojo;


import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class GroupPojo implements Serializable {
    private String username;
    private String showView;
    public List<Group> groups;

    /**
     * 组的
     */
    @Data
    public static class Group implements Serializable {
        private String name;
        private String label;
        private String fileName;
        private Integer count;
        @JSONField(format = "yyyy-MM-dd HH:mm:ss.SSS")
        private Date createTime;
        @JSONField(format = "yyyy-MM-dd HH:mm:ss.SSS")
        private Date updateTime;
    }
}
