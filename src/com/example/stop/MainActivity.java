package com.example.stop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.service.LocationService;
import com.example.utils.GZipUtils;
import com.example.utils.LogUtils;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		copyDb();//�������ݿ�
		
		initData();
	}

	private void initData() {
		
		//������������ķ���
		startService(new Intent(MainActivity.this, LocationService.class));
	}

	private void copyDb() {
		final File file = new File(getFilesDir(), "address.db");
		//�ж��ļ��Ƿ����
		if(file.exists()){
			LogUtils.logd(TAG, "�ļ��Ѿ�����");
			return;
		}
		LogUtils.logd(TAG, "�ļ�������");
		//�������߳�
		new Thread(){
			public void run() {
				//��ȡ��Դ�ļ�
				try {
					//��ȡ�����������
					InputStream is = getAssets().open("address.zip");
					FileOutputStream fos = new FileOutputStream(file);
					//�ý�ѹ�Ĺ�����
					GZipUtils.unZip(is, fos);
				} catch (IOException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

}
