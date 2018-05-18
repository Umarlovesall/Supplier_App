package com.moadd.DemoLockApp;

/**
 * Created by moadd on 28-Oct-17.
 */

public class LockLoginPojo {
    private String userid;
    private String password;
    private String userRole;

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRoleId) {
        this.userRole = userRoleId;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
