package com.laka.live.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

/**
 * Created by luwies on 16/3/29.
 */
public class Product implements Parcelable {
   public static final int PAYTYPE_ALIPAY =1 ,PAYTYPE_WXPAY =2;
    @Expose
    @SerializedName(Common.ID)
    private int id;

    @Expose
    @SerializedName(Common.PAY_TYPE)
    private int payType;

    @Expose
    @SerializedName(Common.NAME)
    private String name;

    @Expose
    @SerializedName(Common.DESCRIPTION)
    private String description;

    @Expose
    @SerializedName(Common.COINS)
    private int coins;

    @Expose
    @SerializedName(Common.EXTRA_COINS)
    private int extraCoins;

    @Expose
    @SerializedName(Common.PRICE)
    private String price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getExtraCoins() {
        return extraCoins;
    }

    public void setExtraCoins(int extraCoins) {
        this.extraCoins = extraCoins;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int describeContents()
    {
        return 0;
    }


    public void writeToParcel(Parcel out, int flags)
    {
        out.writeInt(id);
        out.writeInt(payType);
        out.writeString(name);
        out.writeString(description);
        out.writeInt(coins);
        out.writeInt(extraCoins);
        out.writeString(price);
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>()
    {
        public Product createFromParcel(Parcel in)
        {
            return new Product(in);
        }

        public Product[] newArray(int size)
        {
            return new Product[size];
        }
    };

    private Product(Parcel in)
    {
        id = in.readInt();
        payType = in.readInt();
        name = in.readString();
        description = in.readString();
        coins = in.readInt();
        extraCoins = in.readInt();
        price = in.readString();
    }

}
