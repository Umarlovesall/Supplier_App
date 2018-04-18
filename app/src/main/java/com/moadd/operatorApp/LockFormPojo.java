package com.moadd.operatorApp;

/**
 * Created by moadd on 14-Feb-18.
 */

public class LockFormPojo {
    private String userRoleId,complaint,lockBarcode,complaintSno,status;

    public String getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(String userRoleId) {
        this.userRoleId = userRoleId;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public String getLockBarcode() {
        return lockBarcode;
    }

    public void setLockBarcode(String lockBarcode) {
        this.lockBarcode = lockBarcode;
    }

    public String getComplaintSno() {
        return complaintSno;
    }

    public void setComplaintSno(String complaintSno) {
        this.complaintSno = complaintSno;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
