package com.gistandard.androidbase.utils;

import android.os.Handler;
import android.view.View;
/**
 * 防止连续点击
 * @author shenwh
 *
 */
 public class OnDoubleClickUtil {
	
	/**
	 * 防止连续点击的方法
	 * @param view
	 */
	public static void confiltClick(final View view) {
		view.setEnabled(false);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				view.setEnabled(true);
			}
		}, 1000);
	}

}
