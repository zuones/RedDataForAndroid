package com.zhjaid.reddata.utils;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RequestType {
    public static int getColor(String type) {
        if (type == null) {
            return Color.DKGRAY;
        }
        if (type.toUpperCase().equals("GET")) {
            return Color.parseColor("#8BC34A");
        } else if (type.toUpperCase().equals("POST")) {
            return Color.parseColor("#FF5722");
        } else if (type.toUpperCase().equals("PUT")) {
            return Color.parseColor("#03A9F4");
        } else if (type.toUpperCase().equals("PATCH")) {
            return Color.parseColor("#009688");
        } else if (type.toUpperCase().equals("DELETE")) {
            return Color.parseColor("#FF1100");
        } else if (type.toUpperCase().equals("HEAD")) {
            return Color.parseColor("#00BCD4");
        } else {
            return Color.DKGRAY;
        }
    }

    public static int getHashColor(String str) {
        List<String> item = new ArrayList<>();
        item.add("GET");
        item.add("POST");
        item.add("PUT");
        item.add("PATCH");
        item.add("DELETE");
        item.add("HEAD");
        String s = item.get(str.hashCode() % item.size());
        return getColor(s);
    }
}
