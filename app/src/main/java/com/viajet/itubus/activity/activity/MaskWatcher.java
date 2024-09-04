package com.viajet.itubus.activity.activity;

import android.text.Editable;
import android.text.TextWatcher;

public class MaskWatcher implements TextWatcher {

    private String mask;

    public MaskWatcher(String mask) {
        this.mask = mask;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // No action needed before text changes
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // No action needed while text changes
    }

    @Override
    public void afterTextChanged(Editable s) {
        // Apply the mask to the text
        String original = s.toString().replaceAll("\\D", "");
        StringBuilder masked = new StringBuilder();
        int j = 0;

        for (int i = 0; i < mask.length(); i++) {
            char c = mask.charAt(i);
            if (c == '#') {
                if (j < original.length()) {
                    masked.append(original.charAt(j++));
                }
            } else {
                masked.append(c);
            }
        }

        // Avoid recursive calls to afterTextChanged
        if (!masked.toString().equals(s.toString())) {
            s.replace(0, s.length(), masked.toString());
        }
    }
}
