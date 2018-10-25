package com.ahf.inventoryapp.Data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ahmed Hassan on 26/08/2018.
 */

public class InventoryItem implements Parcelable {
    private final String productName;
    private  int currentQuantity;
    private final String price;
    private final String supplierName;
    private final String supplierEmail;
    private final String supplierPhone;
    private final String image;

    public void setCurrentQuantity(int currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    String id;

    public InventoryItem(String productName, int currentQuantity, String price,
                         String supplierName, String supplierEmail
            , String supplierPhone, String image) {
        this.productName = productName;
        this.currentQuantity = currentQuantity;
        this.price = price;
        this.supplierName = supplierName;
        this.supplierEmail = supplierEmail;
        this.supplierPhone = supplierPhone;
        this.image = image;
    }

    protected InventoryItem(Parcel in) {
        productName = in.readString();
        currentQuantity = in.readInt();
        price = in.readString();
        supplierName = in.readString();
        supplierEmail = in.readString();
        supplierPhone = in.readString();
        image = in.readString();
        id = in.readString();
    }

    public InventoryItem(String productName, int currentQuantity, String price, String supplierName, String supplierEmail, String supplierPhone, String image, String id) {
        this.productName = productName;
        this.currentQuantity = currentQuantity;
        this.price = price;
        this.supplierName = supplierName;
        this.supplierEmail = supplierEmail;
        this.supplierPhone = supplierPhone;
        this.image = image;
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public static final Creator<InventoryItem> CREATOR = new Creator<InventoryItem>() {
        @Override
        public InventoryItem createFromParcel(Parcel in) {
            return new InventoryItem(in);
        }

        @Override
        public InventoryItem[] newArray(int size) {
            return new InventoryItem[size];
        }
    };

    public String getProductName() {
        return productName;
    }

    public int getCurrentQuantity() {
        return currentQuantity;
    }

    public String getPrice() {
        return price;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getSupplierEmail() {
        return supplierEmail;
    }

    public String getSupplierPhone() {
        return supplierPhone;
    }




    @Override
    public int describeContents() {
        return 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productName);
        dest.writeInt(currentQuantity);
        dest.writeString(price);
        dest.writeString(supplierName);
        dest.writeString(supplierEmail);
        dest.writeString(supplierPhone);
        dest.writeString(image);
        dest.writeString(id);
    }
}
