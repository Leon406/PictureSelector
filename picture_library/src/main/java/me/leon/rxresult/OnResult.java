package me.leon.rxresult;

import android.content.Intent;
import android.os.Parcelable;

import androidx.annotation.Nullable;

interface OnResult extends Parcelable {
   void response(int requestCode, int resultCode, @Nullable Intent data);
   void error(Throwable throwable);
}
