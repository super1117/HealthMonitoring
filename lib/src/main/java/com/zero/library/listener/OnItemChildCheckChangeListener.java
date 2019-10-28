package com.zero.library.listener;

import android.view.ViewGroup;
import android.widget.CompoundButton;

public interface OnItemChildCheckChangeListener {

    void onItemChildCheckChange(ViewGroup parent, CompoundButton button, int position, boolean checked);

}
