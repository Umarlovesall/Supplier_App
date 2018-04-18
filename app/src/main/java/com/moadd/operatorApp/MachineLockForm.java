package com.moadd.operatorApp;

/**
 * Created by moadd on 06-Mar-18.
 */

public class MachineLockForm {

    private Long[] lockId;
    private Long[] connectedLockId;
    private Long machineId;
    private Long supplierId;

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Long[] getLockId() {
        return lockId;

    }
    public void setLockId(Long[] lockId) {
        this.lockId = lockId;
    }
    public Long[] getConnectedLockId() {
        return connectedLockId;
    }
    public void setConnectedLockId(Long[] connectedLockId) {
        this.connectedLockId = connectedLockId;
    }
    public Long getMachineId() {
        return machineId;
    }
    public void setMachineId(Long machineId) {
        this.machineId = machineId;
    }




}
