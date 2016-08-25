package com.example.utils;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * ѹ���ͽ�ѹ���ݿ�Ĺ�����
 */
public class GZipUtils {

	
	/**
	 * ��ѹ�����ݿ�
	 * @param src	Դ�ļ���Ŀ¼
	 * @param target	Ŀ���ļ���Ŀ¼
	 */
	public static void unZip(String src,String target) {
		
		GZIPInputStream gis = null;
		FileOutputStream out = null;
		try {
			//������
			FileInputStream fis = new FileInputStream(src);
			gis = new GZIPInputStream(fis);
			//�����
			out = new FileOutputStream(target);
			
			//��ʼд����
			byte[] buffer = new byte[1024];
			int len;
			while((len = gis.read(buffer))!= -1){
				out.write(buffer, 0, len);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			close(gis);
			close(out);
		}
	}
	/**
	 * ��ѹ�����ݿ�,����
	 * @param src	Դ�ļ���Ŀ¼
	 * @param target	Ŀ���ļ���Ŀ¼
	 */
	public static void unZip(InputStream src,OutputStream target) {
		
		GZIPInputStream gis = null;
		try {
			//������
			gis = new GZIPInputStream(src);
			
			//��ʼд����
			byte[] buffer = new byte[1024];
			int len;
			while((len = gis.read(buffer))!= -1){
				target.write(buffer, 0, len);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			close(gis);
			close(target);
		}
	}
	
	
	/**
	 * ѹ�����ݿ�
	 * @param src	Դ�ļ���Ŀ¼
	 * @param target	Ŀ���ļ���Ŀ¼
	 */
	public static void toZip(String src,String target) {
		
		FileInputStream fis = null;
		GZIPOutputStream gos = null;
		try {
			//������
			fis = new FileInputStream(src);
			//�����
			FileOutputStream out = new FileOutputStream(target);
			//ѹ����
			gos = new GZIPOutputStream(out);
			
			//��ʼд����
			byte[] buffer = new byte[1024];
			int len;
			while((len = fis.read(buffer))!= -1){
				gos.write(buffer, 0, len);
			}
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			close(fis);
			close(gos);
		}
	}
	
	
	//����
	public static void close(Closeable close){
		try {
			close.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
