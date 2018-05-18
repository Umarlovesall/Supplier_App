package com.moadd.DemoLockApp;

/**
 * Created by moadd on 28-Oct-17.
 */

public class BarcodeResultSend {
    private String userid;
    private String barcode;
    private String userRoleId;

    public String getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(String userRoleId) {
        this.userRoleId = userRoleId;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
