package com.halohoop.holley.http.download.beans;

/**
 * Created by Pooholah on 2017/5/14.
 */

public enum DownloadStatus {
    waitting(0),

    starting(1),

    downloading(2),

    pause(3),

    finish(4),

    failed(5);//包含取消

    private int value;

    DownloadStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}