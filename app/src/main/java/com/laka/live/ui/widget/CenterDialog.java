package com.laka.live.ui.widget;

import com.laka.live.R;
import com.laka.live.application.LiveApplication;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout.LayoutParams;

/**
 * dialog（居中于屏幕）
 * 
 * @author yulongxiao
 * @date 2015-1-5
 */
public class CenterDialog extends Dialog {

	public CenterDialog(Context context, int theme) {
		super(context, theme);
	}

	public CenterDialog(Context context) {
		super(context, R.style.mydialog);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// try {
		// getWindow().addFlags(
		// WindowManager.LayoutParams.class.getField("FLAG_NEEDS_MENU_KEY").getInt(null));
		// setCanceledOnTouchOutside(true);
		Window dialogWindow = this.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		// 屏幕中间显示
		int screenWidth = LiveApplication.screenWidth;
		lp.width = (int) (screenWidth * 0.8); // 宽度占屏幕80%，高度自适应
		lp.height = LayoutParams.WRAP_CONTENT;
		dialogWindow.setGravity(Gravity.CENTER);
		dialogWindow.setAttributes(lp);
		// } catch (NoSuchFieldException e) {
		// e.printStackTrace();
		// } catch (IllegalAccessException e) {
		// e.printStackTrace();
		// }
	}

	/**
	 * 生成一个弹出框(居中于屏幕)
	 * 
	 * @param context
	 *            上下文
	 * @param layout
	 *            xml布局
	 * @return
	 */
	public static CenterDialog makeNewDialog(Context context, View layout) {
		CenterDialog dialog = new CenterDialog(context, R.style.mydialog);
		dialog.setContentView(layout);
		return dialog;
	}
}
