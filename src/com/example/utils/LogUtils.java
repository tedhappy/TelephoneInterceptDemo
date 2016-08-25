package com.example.utils;

import android.util.Log;

public class LogUtils {
	public static boolean isOpenLog = true;

	public static void logd(String tag, String msg) {

		if (isOpenLog) {
			Log.d(tag, msg);
		}
	}
}
