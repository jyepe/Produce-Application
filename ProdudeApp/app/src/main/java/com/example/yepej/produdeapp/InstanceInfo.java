package com.example.yepej.produdeapp;

public class InstanceInfo
{
    private static InstanceInfo info = null;

    // Global variable
    private String serverIP;
    private String user_ID;
    private String contactName;
    private String companyId;

    // Restrict the constructor from being instantiated
    private InstanceInfo(){}

    public static InstanceInfo getInstance()
    {
        if(info == null)
        {
            info = new InstanceInfo();
        }
        return info;
    }

    public String getServerIP(){ return serverIP; }
    public void setServerIP(String serverIP){
        this.serverIP = serverIP;
    }
    public String getUser_ID() { return user_ID; }
    public void setUser_ID(String userID) { this.user_ID = userID; }
    public String getContactName() { return contactName; }
    public void setContactName(String contactName) { this.contactName = contactName; }
}