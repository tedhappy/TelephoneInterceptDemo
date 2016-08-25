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
 * 压缩和解压数据库的工具类
 */
public class GZipUtils {

	
	/**
	 * 解压缩数据库
	 * @param src	源文件的目录
	 * @param target	目标文件的目录
	 */
	public static void unZip(String src,String target) {
		
		GZIPInputStream gis = null;
		FileOutputStream out = null;
		try {
			//输入流
			FileInputStream fis = new FileInputStream(src);
			gis = new GZIPInputStream(fis);
			//输出流
			out = new FileOutputStream(target);
			
			//开始写数据
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
	 * 解压缩数据库,重载
	 * @param src	源文件的目录
	 * @param target	目标文件的目录
	 */
	public static void unZip(InputStream src,OutputStream target) {
		
		GZIPInputStream gis = null;
		try {
			//输入流
			gis = new GZIPInputStream(src);
			
			//开始写数据
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
	 * 压缩数据库
	 * @param src	源文件的目录
	 * @param target	目标文件的目录
	 */
	public static void toZip(String src,String target) {
		
		FileInputStream fis = null;
		GZIPOutputStream gos = null;
		try {
			//输入流
			fis = new FileInputStream(src);
			//输出流
			FileOutputStream out = new FileOutputStream(target);
			//压缩流
			gos = new GZIPOutputStream(out);
			
			//开始写数据
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
	
	
	//关流
	public static void close(Closeable close){
		try {
			close.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
