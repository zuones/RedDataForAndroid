package com.zhjaid.reddata.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ApiPojo implements Serializable {

    public String fileName;
    public Integer count;
    public GroupPojo.Group group;
    public List<Api> apis;
    public Date createTime;
    public Date updateTime;

    @Data
    public static class Api implements Serializable {
        public String apiId;
        public String name;
        private Request request;
        private Response response;

        @Data
        public static class Request implements Serializable {
            private String method;
            private List<Header> header;
            private Url url;

            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            public static class Header implements Serializable {
                private String key;
                private String value;
                private String description;
            }

            @Data
            public static class Url implements Serializable {
                private String raw;
                private List<Query> query;

                @Data
                @AllArgsConstructor
                @NoArgsConstructor
                public static class Query implements Serializable {
                    private String key;
                    private String value;
                    private String description;
                }
            }
        }

        @Data
        public static class Response implements Serializable{

        }

    }
}
