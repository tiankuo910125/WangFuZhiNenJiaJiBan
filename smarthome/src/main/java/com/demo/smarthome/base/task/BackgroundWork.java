package com.demo.smarthome.base.task;

public interface BackgroundWork<T> {
    T doInBackground() throws Exception;
}
