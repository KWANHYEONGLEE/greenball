package com.google.ar.core.examples.java.itemdata;

import android.os.Parcel;
import android.os.Parcelable;

public class PersonChoiceInfo implements Parcelable {
    // 누구랑 왔는지
    private String fromWho;
    // 무엇을 하기를 원하는지
    private String wantSomthing;
    // 현재 스토리 페이지 위치
    private int storypage;

    public PersonChoiceInfo() {}

    public PersonChoiceInfo(int storypage) {
        this.storypage = storypage;
    }

    protected PersonChoiceInfo(Parcel in) {
        fromWho = in.readString();
        wantSomthing = in.readString();
        storypage = in.readInt();
    }

    public static final Creator<PersonChoiceInfo> CREATOR = new Creator<PersonChoiceInfo>() {
        @Override
        public PersonChoiceInfo createFromParcel(Parcel in) {
            return new PersonChoiceInfo(in);
        }

        @Override
        public PersonChoiceInfo[] newArray(int size) {
            return new PersonChoiceInfo[size];
        }
    };

    public int getStorypage() {
        return storypage;
    }

    public void setStorypage(int storypage) {
        this.storypage = storypage;
    }

    public String getFromWho() {
        return fromWho;
    }

    public void setFromWho(String fromWho) {
        this.fromWho = fromWho;
    }

    public String getWantSomthing() {
        return wantSomthing;
    }

    public void setWantSomthing(String wantSomthing) {
        this.wantSomthing = wantSomthing;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(fromWho);
        parcel.writeString(wantSomthing);
        parcel.writeInt(storypage);
    }
}
