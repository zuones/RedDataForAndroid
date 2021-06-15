package com.zhjaid.reddata.data;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.zhjaid.reddata.pojo.ApiPojo;
import com.zhjaid.reddata.pojo.GroupPojo;
import com.zhjaid.reddata.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据操作基类
 */
public class BaseData {
    private Context context;

    private static final String GROUP_FIEL_NAME = "GroupPojo";

    public BaseData(Context context) {
        this.context = context;
    }


    /**
     * 读取 GroupPojo 配置
     *
     * @return
     */
    public GroupPojo getGroupPojo() {
        String appRootPth = FileUtils.getAppRootPth(context);
        String result = FileUtils.readFileR(appRootPth, GROUP_FIEL_NAME);
        if (result == null) {
            return new GroupPojo();
        }
        return JSON.parseObject(result, GroupPojo.class);
    }

    /**
     * 设置 GroupPojo 配置
     *
     * @return
     */
    public boolean setGroupPojo(GroupPojo groupPojo) {
        String appRootPth = FileUtils.getAppRootPth(context);
        FileUtils.writeFileR(JSON.toJSONString(groupPojo), appRootPth, GROUP_FIEL_NAME);
        return true;
    }

    /**
     * 写组
     *
     * @param group
     * @return
     */
    public boolean putGroup(GroupPojo.Group group) {
        GroupPojo groupPojo = getGroupPojo();
        List<GroupPojo.Group> groups = groupPojo.getGroups();
        if (groups == null) {
            groups = new ArrayList<GroupPojo.Group>();
        }
        groups.add(group);
        groupPojo.setGroups(groups);
        setGroupPojo(groupPojo);
        return true;
    }

    /**
     * 更新组
     *
     * @param group
     * @return
     */
    public boolean updateGroup(GroupPojo.Group group) {
        GroupPojo groupPojo = getGroupPojo();
        List<GroupPojo.Group> groups = groupPojo.getGroups();
        if (groups == null) {
            groups = new ArrayList<GroupPojo.Group>();
        }
        for (int i = 0; i < groups.size(); i++) {
            GroupPojo.Group group1 = groups.get(i);
            if (group1.getFileName().equals(group.getFileName())) {
                groups.remove(i);
                groups.add(i, group);
                break;
            }
        }
        groupPojo.setGroups(groups);
        setGroupPojo(groupPojo);
        return true;
    }

    /**
     * 删除组
     */

    public boolean removeGroup(GroupPojo.Group group) {
        GroupPojo groupPojo = getGroupPojo();
        List<GroupPojo.Group> groups = groupPojo.getGroups();
        if (group == null || groups == null) {
            return false;
        }
        for (GroupPojo.Group group1 : groups) {
            if (group1.getFileName().equals(group.getFileName())) {
                groups.remove(group1);
                groupPojo.setGroups(groups);
                setGroupPojo(groupPojo);
                return true;
            }
        }
        return false;
    }

    /**
     * 通过 Group 获取出 API
     *
     * @param group
     * @return
     */
    public ApiPojo getApis(GroupPojo.Group group) {
        //创建api的文件
        String fileName = group.getFileName();
        String appRootPth = FileUtils.getAppRootPth(context);
        File file = new File(new File(appRootPth, "apis").toString(), fileName);
        if (!file.isFile()) {
            boolean b = FileUtils.createFile(new File(appRootPth, "apis").toString(), fileName);
            if (!b)
                return null;
        }
        //读取api并解析
        String apiContent = FileUtils.readFileR(new File(appRootPth, "apis").toString(), fileName);
        if (apiContent == null)
            apiContent = "";
        return JSON.parseObject(apiContent, ApiPojo.class);
    }

    /**
     * 添加一个请求到 group
     *
     * @param group
     * @param api
     * @return
     */
    public boolean putRequest(GroupPojo.Group group, ApiPojo.Api api) {
        ApiPojo parseObject = getApis(group);
        if (parseObject == null)
            parseObject = new ApiPojo();
        List<ApiPojo.Api> apis = parseObject.getApis();
        if (apis == null)
            apis = new ArrayList<>();
        boolean isexit = false;
        for (ApiPojo.Api api1 : apis) {
            if (api1.getApiId().equals(api.getApiId()))
                isexit = true;
        }
        if (!isexit)
            apis.add(api);
        else
            return false;
        group.setCount(apis.size());
        updateGroup(group);
        parseObject.setApis(apis);
        String appRootPth = FileUtils.getAppRootPth(context);
        FileUtils.writeFileR(JSON.toJSONString(parseObject),
                new File(appRootPth, "apis").toString(),
                group.getFileName());
        return true;
    }


    /**
     * 更新操作
     *
     * @param group
     * @param api
     * @return
     */
    public boolean updateRequest(GroupPojo.Group group, ApiPojo.Api api) {
        ApiPojo parseObject = getApis(group);
        if (parseObject == null)
            parseObject = new ApiPojo();
        List<ApiPojo.Api> apis = parseObject.getApis();
        if (apis == null)
            apis = new ArrayList<>();
        for (int i = 0; i < apis.size(); i++) {
            if (apis.get(i).getApiId().equals(api.getApiId())) {
                apis.remove(i);
                apis.add(i, api);
                break;
            }
        }
        parseObject.setApis(apis);
        String appRootPth = FileUtils.getAppRootPth(context);
        FileUtils.writeFileR(JSON.toJSONString(parseObject),
                new File(appRootPth, "apis").toString(),
                group.getFileName());
        return true;
    }


    /**
     * 删除操作
     *
     * @param group
     * @param api
     * @return
     */
    public boolean removeRequest(GroupPojo.Group group, ApiPojo.Api api) {
        ApiPojo parseObject = getApis(group);
        if (parseObject == null)
            parseObject = new ApiPojo();
        List<ApiPojo.Api> apis = parseObject.getApis();
        if (apis == null)
            apis = new ArrayList<>();
        for (int i = 0; i < apis.size(); i++) {
            if (apis.get(i).getApiId().equals(api.getApiId())) {
                apis.remove(i);
                break;
            }
        }
        parseObject.setApis(apis);
        String appRootPth = FileUtils.getAppRootPth(context);
        FileUtils.writeFileR(JSON.toJSONString(parseObject),
                new File(appRootPth, "apis").toString(),
                group.getFileName());
        return true;
    }


}
