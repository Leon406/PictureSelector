package me.leon.rxresult;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;



public class HolderActivity extends Activity {
    private static Request request;
    private OnPreResult onPreResult;
    private me.leon.rxresult.OnResult onResult;
    private int resultCode;
    private int requestCode;
    private Intent data;
    private static int FAILED_REQUEST_CODE = -909;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("leon66", "HolderActivity onCreate");
         request = (Request) getIntent().getParcelableExtra("request");
        if (HolderActivity.request == null) {
            finish();
            return;
        }
        Log.i("leon66", "HolderActivity onCreate in");
        onPreResult = HolderActivity.request.onPreResult();
        onResult = HolderActivity.request.onResult();

        if (savedInstanceState != null) return;

        if (HolderActivity.request instanceof RequestIntentSender) {
            RequestIntentSender requestIntentSender = (RequestIntentSender) HolderActivity.request;

            if (requestIntentSender.getOptions() == null) startIntentSender(requestIntentSender);
            else startIntentSenderWithOptions(requestIntentSender);
        } else {
            try {
                Log.i("leon66", "HolderActivity  startActivityForResult" + HolderActivity.request.intent());
                startActivityForResult(HolderActivity.request.intent(), 0);
            } catch (ActivityNotFoundException e) {
                if (onResult != null) {
                    onResult.error(e);
                }
            }
        }
    }

    private void startIntentSender(RequestIntentSender requestIntentSender) {
        try {
            startIntentSenderForResult(requestIntentSender.getIntentSender(), 0,
                    requestIntentSender.getFillInIntent(), requestIntentSender.getFlagsMask(),
                    requestIntentSender.getFlagsValues(), requestIntentSender.getExtraFlags());
        } catch (IntentSender.SendIntentException exception) {
            exception.printStackTrace();
            onResult.response(FAILED_REQUEST_CODE, RESULT_CANCELED, null);
        }
    }

    private void startIntentSenderWithOptions(RequestIntentSender requestIntentSender) {
        try {
            startIntentSenderForResult(requestIntentSender.getIntentSender(), 0,
                    requestIntentSender.getFillInIntent(), requestIntentSender.getFlagsMask(),
                    requestIntentSender.getFlagsValues(), requestIntentSender.getExtraFlags(), requestIntentSender.getOptions());
        } catch (IntentSender.SendIntentException exception) {
            exception.printStackTrace();
            onResult.response(FAILED_REQUEST_CODE, RESULT_CANCELED, null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.resultCode = resultCode;
        this.requestCode = requestCode;
        this.data = data;

        if (this.onPreResult != null) {
            this.onPreResult.response(requestCode, resultCode, data)
                    .doOnComplete(this::finish)
                    .subscribe();
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (onResult != null)
            onResult.response(requestCode, resultCode, data);
    }

    static void setRequest(Request aRequest) {
        request = aRequest;
    }
}
