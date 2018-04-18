package com.moadd.operatorApp;

public class ItemLockform1 {

    private String priceString,itemIdString,lockIdString;
    private String password;
    private String expireDate;

    public String getLockIdString() {
        return lockIdString;
    }

    public void setLockIdString(String lockIdString) {
        this.lockIdString = lockIdString;
    }

    private String productionDate;
    private long iquantity;
    private long itemId;

    public String getPriceString() {
        return priceString;
    }

    public void setPriceString(String priceString) {
        this.priceString = priceString;
    }

    public String getItemIdString() {
        return itemIdString;
    }

    public void setItemIdString(String itemIdString) {
        this.itemIdString = itemIdString;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(String productionDate) {
        this.productionDate = productionDate;
    }

    public long getIquantity() {
        return iquantity;
    }

    public void setIquantity(long iquantity) {
        this.iquantity = iquantity;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public long getLockId() {
        return lockId;
    }

    public void setLockId(long lockId) {
        this.lockId = lockId;
    }

    public long getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(long userRoleId) {
        this.userRoleId = userRoleId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    private long lockId;
    private long userRoleId;
    private double price;




}