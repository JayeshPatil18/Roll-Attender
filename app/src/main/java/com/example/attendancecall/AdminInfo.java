package com.example.attendancecall;

public class AdminInfo {
    public String getAdminEmailId() {
        return AdminEmailId;
    }

    public void setAdminEmailId(String adminEmailId) {
        AdminEmailId = adminEmailId;
    }

    public String getAdminPassword() {
        return AdminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        AdminPassword = adminPassword;
    }

    String AdminEmailId,AdminPassword;
}
