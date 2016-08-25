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
		LogUtils.logd(TAG, "�����˷���");

		// ���������״̬
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

		listener = new MyPhoneStateListener();

		// ע������ļ���
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LogUtils.logd(TAG, "ȡ���˷���");

		// ȡ���绰�ļ���
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
	}

	private class MyPhoneStateListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:// ����״̬

				break;
			case TelephonyManager.CALL_STATE_RINGING:// ����״̬
				// ��ȡ���������Ĺ�����
				String location = AddressDao.getLocation(
						getApplicationContext(), incomingNumber);

				System.out.println(location);

				// �ж�location��λ��
				isHeBei(location,incomingNumber);
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:// ͨ��״̬

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
		if ("�ӱ�".equals(location2)) {
			stopPhone(incomingNumber);
		}
	}

	/**
	 * ���ص绰
	 * @param incomingNumber 
	 */
	private void stopPhone(final String incomingNumber) {
		// ͨ�������ȡServiceManager
		try {
			// ����
			Class clazz = Class.forName("android.os.ServiceManager");
			Method method = clazz.getMethod("getService", String.class);
			IBinder iBinder = (IBinder) method.invoke(null,
					Context.TELEPHONY_SERVICE);
			// ͨ��aidl��ȡ
			ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);
			// ���ùҶϵ绰�ķ���
			iTelephony.endCall();
			
			Toast.makeText(getApplicationContext(), "������", 0).show();

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
		
		//ɾ��ͨ����¼��־
		
		final Uri uri = Uri.parse("content://" + CallLog.AUTHORITY + "/calls");
		
		//ͨ�����ݹ۲���,�۲�ͨ����¼��־�ı仯,����仯,��ɾ����־
		getContentResolver().registerContentObserver(uri, true, new ContentObserver(new Handler()) {

			//��дonChange����
			@Override
			public void onChange(boolean selfChange) {
				super.onChange(selfChange);
				
				//ɾ��ͨ����¼��־
				getContentResolver().delete(uri, "number=?", new String[]{incomingNumber});
				
				//��ע��,�Է�ɾ���������ĵ绰
				getContentResolver().unregisterContentObserver(this);
			}
			
		});
	}
}
