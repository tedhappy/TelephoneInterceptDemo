package com.example.dao;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AddressDao {
	public static String getLocation(Context context, String phone) {
		String type = "";
		// ��ȡ�����ݿ��λ��
		File file = new File(context.getFilesDir(), "address.db");
		// ͨ����������ַ�ʽ���db����
		SQLiteDatabase db = SQLiteDatabase.openDatabase(file.getAbsolutePath(),
				null, SQLiteDatabase.OPEN_READONLY);
		// �ж��Ƿ����ֻ���
		if (phone.matches("1[3578]\\d{9}")) {
			// ���ݿ��е����ֻ���ǰ7λ,��Ҫ��������ֻ��Ž�ȡһ��
			String prefixPhone = phone.substring(0, 7);
			// ��ѯ
			Cursor cursor = db.query("info", new String[] { "cardtype" },
					"mobileprefix = ?", new String[] { prefixPhone }, null,
					null, null);

			if (cursor.moveToNext()) {
				type = cursor.getString(0);
			}
			cursor.close();
		} else if (phone.length() == 3) {
			type = "��������";
		} else if (phone.length() == 4) {
			type = "ģ����";
		} else if (phone.length() == 5) {
			type = "�������";
		} else if (phone.length() == 7 || phone.length() == 8) {
			type = "���غ���";
		} else if (phone.length() == 10 || phone.length() == 11
				|| phone.length() == 12) {
			// ȡ��ǰ4λ,������4λ������
			String prefixArea = phone.substring(0, 4);
			// ��ѯ
			Cursor cursor = db.query("info", new String[] { "city" },
					"area = ?", new String[] { prefixArea }, null, null, null);
			if (cursor.moveToNext()) {
				type = cursor.getString(0);
			} else {
				// 3λ������
				prefixArea = phone.substring(0, 3);
				cursor = db.query("info", new String[] { "city" }, "area = ?",
						new String[] { prefixArea }, null, null, null);
				if (cursor.moveToNext()) {
					type = cursor.getString(0);
				} else {
					type = "δ֪����";
				}
			}
			cursor.close();
		} else {
			type = "δ֪����";
		}
		// ���ر�db,����type
		db.close();
		return type;
	}
}
