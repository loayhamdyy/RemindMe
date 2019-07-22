package com.android.RemindMePlease.Utils;

import android.view.View;

/**
 * Created by Khairy on 4/25/2018.
 */

public interface ClickListener {
    void onClick(View view, int position);
    void onLongClick(View view, int position);
}