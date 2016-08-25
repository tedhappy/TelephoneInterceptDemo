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
		
		copyDb();//拷贝数据库
		
		initData();
	}

	private void initData() {
		
		//开启监听来电的服务
		startService(new Intent(MainActivity.this, LocationService.class));
	}

	private void copyDb() {
		final File file = new File(getFilesDir(), "address.db");
		//判断文件是否存在
		if(file.exists()){
			LogUtils.logd(TAG, "文件已经存在");
			return;
		}
		LogUtils.logd(TAG, "文件不存在");
		//开启子线程
		new Thread(){
			public void run() {
				//获取到源文件
				try {
					//获取到输入输出流
					InputStream is = getAssets().open("address.zip");
					FileOutputStream fos = new FileOutputStream(file);
					//用解压的工具类
					GZipUtils.unZip(is, fos);
				} catch (IOException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

}
