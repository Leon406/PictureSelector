package me.leon.rxresult;

import android.content.Intent;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import io.reactivex.Observable;

public interface OnPreResult<T> extends Parcelable {
    Observable<T> response(int requestCode, int resultCode, @Nullable Intent data);
}
