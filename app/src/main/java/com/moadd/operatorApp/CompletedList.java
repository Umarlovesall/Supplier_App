package com.moadd.operatorApp;

/**
 * Created by moadd on 08-Jan-18.
 */

public class CompletedList {
    private String lockBarcode,itemBarcode,quantity,price;

    public String getLockBarcode() {
        return lockBarcode;
    }

    public void setLockBarcode(String lockBarcode) {
        this.lockBarcode = lockBarcode;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
