package me.leon.rxresult;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import java.io.Serializable;

class Request implements Parcelable {
    private final Intent intent;
    OnPreResult onPreResult;
    private OnResult onResult;

    public Request(@Nullable Intent intent) {
        this.intent = intent;
    }

    protected Request(Parcel in) {
        intent = in.readParcelable(Intent.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(intent, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Request> CREATOR = new Creator<Request>() {
        @Override
        public Request createFromParcel(Parcel in) {
            return new Request(in);
        }

        @Override
        public Request[] newArray(int size) {
            return new Request[size];
        }
    };

    public void setOnPreResult(@Nullable OnPreResult onPreResult) {
        this.onPreResult = onPreResult;
    }

    public OnPreResult onPreResult() {
        return onPreResult;
    }

    public void setOnResult(OnResult onResult) {
        this.onResult = onResult;
    }

    public OnResult onResult() {
        return onResult;
    }

    @Nullable
    public Intent intent() {
        return intent;
    }


}
