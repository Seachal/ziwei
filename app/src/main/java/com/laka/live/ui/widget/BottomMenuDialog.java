package com.laka.live.ui.widget;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout.LayoutParams;

import com.laka.live.R;

/**
 * 底部上滑弹出框
 * 
 * @author xiaoyulong
 * @version 创建时间：2015-5-6 上午11:40:38
 */
public class BottomMenuDialog extends Dialog {

	public BottomMenuDialog(Context context, int theme) {
		super(context, theme);
	}

	public BottomMenuDialog(Context context) {
		super(context, R.style.mydialog);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// try {
		// getWindow().addFlags(
		// WindowManager.LayoutParams.class.getField("FLAG_NEEDS_MENU_KEY").getInt(null));
		setCanceledOnTouchOutside(true);
		Window dialogWindow = this.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.WRAP_CONTENT;
		dialogWindow.setWindowAnimations(R.style.mydialogAnimation);
		dialogWindow.setAttributes(lp);
		// } catch (NoSuchFieldException e) {
		// e.printStackTrace();
		// } catch (IllegalAccessException e) {
		// e.printStackTrace();
		// }
	}

	/**
	 * 创建一个弹出框
	 * 
	 * @param context
	 *            上下文
	 * @param layout
	 *            layout布局
	 * @return
	 */
	public static BottomMenuDialog createNewDialog(Context context, View layout) {
		BottomMenuDialog dialog = new BottomMenuDialog(context, R.style.mydialog);
		dialog.setContentView(layout);
		return dialog;
	}
}
