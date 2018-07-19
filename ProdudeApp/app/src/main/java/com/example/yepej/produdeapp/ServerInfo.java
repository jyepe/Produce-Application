package com.example.yepej.produdeapp;

public class ServerInfo{
    private static ServerInfo serverInstance = null;

    // Global variable
    private String serverIP;

    // Restrict the constructor from being instantiated
    private ServerInfo(){}

    public void setServerIP(String val){
        this.serverIP = val;
    }
    public String getServerIP(){
        return this.serverIP;
    }

    public static ServerInfo getInstance(){
        if(serverInstance == null){
            serverInstance = new ServerInfo();
        }
        return serverInstance;
    }
}