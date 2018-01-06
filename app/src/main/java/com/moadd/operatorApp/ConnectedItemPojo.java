package com.moadd.operatorApp;

/**
 * Created by moadd on 05-Jan-18.
 */

public class ConnectedItemPojo {
    private String quantity,itemBarcode,lockBarcode,price;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public String getLockBarcode() {
        return lockBarcode;
    }

    public void setLockBarcode(String lockBarcode) {
        this.lockBarcode = lockBarcode;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
