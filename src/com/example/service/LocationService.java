package com.example.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.example.dao.AddressDao;
import com.example.utils.LogUtils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class LocationService extends Service {

	private static final String TAG = "LocationService";
	private TelephonyManager tm;
	private MyPhoneStateListener listener;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		LogUtils.logd(TAG, "开启了服务");

		// 监听来电的状态
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

		listener = new MyPhoneStateListener();

		// 注册来电的监听
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LogUtils.logd(TAG, "取消了服务");

		// 取消电话的监听
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
	}

	private class MyPhoneStateListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:// 空闲状态

				break;
			case TelephonyManager.CALL_STATE_RINGING:// 响铃状态
				// 获取到来电号码的归属地
				String location = AddressDao.getLocation(
						getApplicationContext(), incomingNumber);

				System.out.println(location);

				// 判断location的位置
				isHeBei(location,incomingNumber);
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:// 通话状态

				break;

			default:
				break;
			}
		}
	}

	public void isHeBei(String location, String incomingNumber) {
		System.out.println(".."+location);
		
		String location2 = location.substring(0, 2);
		System.out.println(location2);
		if ("河北".equals(location2)) {
			stopPhone(incomingNumber);
		}
	}

	/**
	 * 拦截电话
	 * @param incomingNumber 
	 */
	private void stopPhone(final String incomingNumber) {
		// 通过反射获取ServiceManager
		try {
			// 反射
			Class clazz = Class.forName("android.os.ServiceManager");
			Method method = clazz.getMethod("getService", String.class);
			IBinder iBinder = (IBinder) method.invoke(null,
					Context.TELEPHONY_SERVICE);
			// 通过aidl获取
			ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);
			// 调用挂断电话的方法
			iTelephony.endCall();
			
			Toast.makeText(getApplicationContext(), "拦截了", 0).show();

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//删除通话记录日志
		
		final Uri uri = Uri.parse("content://" + CallLog.AUTHORITY + "/calls");
		
		//通过内容观察者,观察通话记录日志的变化,如果变化,就删除日志
		getContentResolver().registerContentObserver(uri, true, new ContentObserver(new Handler()) {

			//重写onChange方法
			@Override
			public void onChange(boolean selfChange) {
				super.onChange(selfChange);
				
				//删除通话记录日志
				getContentResolver().delete(uri, "number=?", new String[]{incomingNumber});
				
				//反注册,以防删除后面打进的电话
				getContentResolver().unregisterContentObserver(this);
			}
			
		});
	}
}
