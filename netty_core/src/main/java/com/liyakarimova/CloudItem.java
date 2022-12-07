package com.liyakarimova;

import lombok.Data;

import java.io.Serializable;

@Data
public class CloudItem implements Serializable {


    private String name;

    private boolean isDir;

    public CloudItem(String name, boolean isDir) {
        this.name = name;
        this.isDir = isDir;
    }
}
