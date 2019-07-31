package framework.utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import framework.ioc.Ioc;

public class FileUtil {
	public static String getExtension(String fileName) {
		if (null == fileName) {
			return null;
		}

		int dotIndex = fileName.lastIndexOf('.');
		// 如果.在第一位或者最后一位，则返回NULL
		if (dotIndex <= 0 || dotIndex == fileName.length() - 1) {
			return null;
		}

		return fileName.substring(dotIndex + 1);
	}

	/***
	 * 获取项目文件夹
	 * @return
	 */
	public static File getDir(){
		Context context = Ioc.getApplication();
		String packname=context.getPackageName();
		String name=packname.substring(packname.lastIndexOf(".")+1, packname.length());
		File dir=null;
		if((!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))){
			dir = context.getCacheDir();
		}else{
			dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
		}
		dir.mkdirs();
		return dir;
	}


	/**
	 * 获取更新文件的缓存目录
	 */
	public static String getUpdateFilePath(){

		File file = new File(getDir().getAbsolutePath() + "/updateFile");

		if (!file.exists()) {
			file.mkdirs();
		}
		file = null;

		return getDir().getAbsolutePath() + "/updateFile";
	}
	/**
	 * 获取项目缓存文件
	 * @return
	 */
	public static File getCacheDir(){
		File file=new File(getDir().getAbsolutePath());
		if(!file.exists()){
			file.mkdirs();
		}
		return file;
	}

	/**
	 * 获取图片的缓存文件
	 */
	public static File getAppDir(){
		File file=new File(getCacheDir()+"/BeiKe");
		if(!file.exists()){
			file.mkdirs();
		}
		return file;
	}


	/**
	 * 获取图片的缓存文件
	 */
	public static File getCachePhotoDir(){
		File file=new File(getAppDir()+"/Cache");
		if(!file.exists()){
			file.mkdirs();
		}
		return file;
	}

	/**
	 * 获取压缩的临时图片的地址
     */
	public static File getCompressPhotoDir(){
		File file=new File(getCacheDir()+"/CompressPhoto");
		if(!file.exists()){
			file.mkdirs();
		}
		return file;
	}
	/**
	 * 获取压缩的临时图片的地址
	 */
	public static File getCompressAlbumPhotoDir(){
		File file=new File(getCacheDir()+"/CompressAlbum");
		if(!file.exists()){
			file.mkdirs();
		}
		return file;
	}
	public static File getDir(String name){
		File file=new File(getDir().getAbsolutePath()+"/"+name);
		if(!file.exists()){
			file.mkdirs();
		}
		return file;
	}
	
	
	
	
	/**
	 * 获取项目使用过程中产生的图片文件夹
	 * @return
	 */
	public static File getImageDir(){
		File file=new File(getDir().getAbsolutePath()+"/image");
		file.mkdirs();
		return file;
	}
	/**
	 * 删除文件夹
	 * @param dirf
	 */
	public static void deleteDir(File dirf){
		if(dirf.isDirectory()){
			File[] childs=dirf.listFiles();
			for (int i = 0; i < childs.length; i++) {
				deleteDir(childs[i]);
			}
		}
		dirf.delete();
	}
	
	/**
	 * uri装换文件
	 * @param context
	 * @param uri
	 * @return
	 */
	public static File uriToFile(Activity context, Uri uri){
	    String[] proj = { MediaStore.Images.Media.DATA };
	    Cursor actualimagecursor =context.managedQuery(uri,proj,null,null,null);
	    int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	    actualimagecursor.moveToFirst();  
	    String img_path = actualimagecursor.getString(actual_image_column_index);
	    File file = new File(img_path);
	    return file;
	}
	/**
	 * 写入文件
	 * @param in
	 * @param file
	 */
	public static void write(InputStream in, File file){
		if(file.exists()){
			file.delete();
		}
		try {
			file.createNewFile();
			FileOutputStream out=new FileOutputStream(file);
			byte[] buffer=new byte[1024];
			while (in.read(buffer)>-1) {
				out.write(buffer);
			}
			out.flush();
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 写入文件
	 * @param in
	 * @param file
	 */
	public static void write(String in, File file, boolean append){
		if(file.exists()){
			file.delete();
		}
		try {
			file.createNewFile();
			FileWriter fw = new FileWriter(file, append);
			fw.write(in);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 读文件
	 * @param file
	 * @return
	 */
	public static String read(File file){
		if(!file.exists()){
			return "";
		}
		try {
			FileReader reader=new FileReader(file);
			BufferedReader br = new BufferedReader(reader);
			StringBuffer buffer=new StringBuffer();
			String s;
			while ((s = br.readLine()) != null) {
				buffer.append(s);
			}
			return buffer.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取压缩的临时图片的地址
	 */
	public static File getRegionsDir(){
		File file=new File(getCacheDir()+"/Regions.txt");
		if(!file.exists()){
			file.mkdirs();
		}
		return file;
	}

	
}
