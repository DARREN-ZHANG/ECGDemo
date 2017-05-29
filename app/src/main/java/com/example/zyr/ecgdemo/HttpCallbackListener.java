package com.example.zyr.ecgdemo;

import android.os.Message;

public interface HttpCallbackListener {
    void onFinish(String response,Message message);
    void onError(Exception e);
}
