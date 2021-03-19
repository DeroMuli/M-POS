package com.muli.m_pos.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class ItemCardData implements Parcelable {

    private String imageuri;
    private String name;
    private int unitprice;
    private int itemnumber;

    public ItemCardData(String imageuri, String name, int unitprice){
        this(imageuri,name,unitprice,1);
    }

    public ItemCardData(String imageuri , String name, int unitprice, int itemnumber){
        this.imageuri = imageuri;
        this.name = name;
        this.unitprice = unitprice;
        this.itemnumber = itemnumber;
    }

    protected ItemCardData(Parcel in) {
        imageuri = in.readString();
        name = in.readString();
        unitprice = in.readInt();
        itemnumber = in.readInt();
    }

    public static final Creator<ItemCardData> CREATOR = new Creator<ItemCardData>() {
        @Override
        public ItemCardData createFromParcel(Parcel in) {
            String imageuri = in.readString();
            String name = in.readString();
            int unitprice = in.readInt();
            return new ItemCardData(imageuri,name,unitprice);
        }

        @Override
        public ItemCardData[] newArray(int size) {
            return new ItemCardData[size];
        }
    };

    public String getName(){return name;}

    public String getImageUri(){return imageuri; }

    public int getprice() { return unitprice*itemnumber; }

    public int getItemnumber(){ return  itemnumber; }

    public void addnumofitems(){ itemnumber++; }

    public void removenumofitems() {itemnumber--; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemCardData that = (ItemCardData) o;
        return imageuri.equals(that.imageuri) &&
                name.equals(that.name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageuri);
        dest.writeString(name);
        dest.writeInt(unitprice);
    }
}
