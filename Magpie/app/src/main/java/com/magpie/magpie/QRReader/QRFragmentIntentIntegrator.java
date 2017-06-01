package com.magpie.magpie.QRReader;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.google.zxing.integration.android.IntentIntegrator;

public final class QRFragmentIntentIntegrator extends IntentIntegrator {
    private final Fragment fragment;

    public QRFragmentIntentIntegrator(Fragment fragment){
        super(fragment.getActivity());
        this.fragment = fragment;
    }

    @Override
    protected void startActivityForResult(Intent intent, int code){
        fragment.startActivityForResult(intent, code);
    }
}
