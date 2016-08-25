package com.example.dao;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AddressDao {
	public static String getLocation(Context context, String phone) {
		String type = "";
		// 获取到数据库的位置
		File file = new File(context.getFilesDir(), "address.db");
		// 通过下面的这种方式获得db对象
		SQLiteDatabase db = SQLiteDatabase.openDatabase(file.getAbsolutePath(),
				null, SQLiteDatabase.OPEN_READONLY);
		// 判断是否是手机号
		if (phone.matches("1[3578]\\d{9}")) {
			// 数据库中的是手机的前7位,需要将传入的手机号截取一下
			String prefixPhone = phone.substring(0, 7);
			// 查询
			Cursor cursor = db.query("info", new String[] { "cardtype" },
					"mobileprefix = ?", new String[] { prefixPhone }, null,
					null, null);

			if (cursor.moveToNext()) {
				type = cursor.getString(0);
			}
			cursor.close();
		} else if (phone.length() == 3) {
			type = "紧急号码";
		} else if (phone.length() == 4) {
			type = "模拟器";
		} else if (phone.length() == 5) {
			type = "服务号码";
		} else if (phone.length() == 7 || phone.length() == 8) {
			type = "本地号码";
		} else if (phone.length() == 10 || phone.length() == 11
				|| phone.length() == 12) {
			// 取出前4位,适用于4位的区号
			String prefixArea = phone.substring(0, 4);
			// 查询
			Cursor cursor = db.query("info", new String[] { "city" },
					"area = ?", new String[] { prefixArea }, null, null, null);
			if (cursor.moveToNext()) {
				type = cursor.getString(0);
			} else {
				// 3位的区号
				prefixArea = phone.substring(0, 3);
				cursor = db.query("info", new String[] { "city" }, "area = ?",
						new String[] { prefixArea }, null, null, null);
				if (cursor.moveToNext()) {
					type = cursor.getString(0);
				} else {
					type = "未知号码";
				}
			}
			cursor.close();
		} else {
			type = "未知号码";
		}
		// 最后关闭db,返回type
		db.close();
		return type;
	}
}
