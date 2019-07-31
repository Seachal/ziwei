
package com.laka.live.ui.widget.dialog;

import android.view.KeyEvent;

public interface IDialogOnKeyListener {
    void onDialogKey(GenericDialog dialog, int viewId, KeyEvent event,
                     Object extra);
}
