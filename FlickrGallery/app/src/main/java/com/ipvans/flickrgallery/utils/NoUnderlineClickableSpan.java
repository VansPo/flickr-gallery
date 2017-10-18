package com.ipvans.flickrgallery.utils;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class NoUnderlineClickableSpan extends ClickableSpan {

    private final View.OnClickListener listener;

    public NoUnderlineClickableSpan(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View widget) {
        listener.onClick(widget);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
    }
}
