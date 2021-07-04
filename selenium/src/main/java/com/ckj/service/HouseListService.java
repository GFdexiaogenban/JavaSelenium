package com.ckj.service;

public interface HouseListService {
    public void addAllPageUrl() throws InterruptedException;
    public int getFirstId();
    public String getFirstUrl();
    public void deleteFirst();
    public int getUrlSum();
}
