package train;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;

import com.mysql.jdbc.Field;

public class Crawl {
	
	private String urlStr = "https://kyfw.12306.cn/otn/passcodeNew/getPassCodeNew?module=login&rand=sjrand&0.8316939938813448";
	
	private int maxNum = 100;
	private int curNum = 1;
	
	
	/**
	 * 下载图片
	 * @param dir 需要保存的目录
	 * @return 下载的图片的文件
	 */
	public File download(File dir){
		File file = new File( dir.getName() + "/" + System.currentTimeMillis() + ".jpg");
		try {
			if (!dir.exists()) {
				dir.mkdir();
			}
			URL url = new URL(urlStr);
			HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
			
			InputStream inputStream = connection.getInputStream();
			FileOutputStream outputStream = new FileOutputStream(file);
			byte[] buff = new byte[1024];
			int len = 0;
			while((len = inputStream.read(buff, 0, 1024)) != -1){
				outputStream.write(buff, 0, len);
			}
			inputStream.close();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		curNum ++;
		return file;
	}
	
	public void download(){
		try {
			File dir = new File("download");
			if (!dir.exists()) {
				dir.mkdir();
			}
			URL url = new URL(urlStr);
			HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
			
			InputStream inputStream = connection.getInputStream();
			FileOutputStream outputStream = new FileOutputStream(new File("download/" + curNum + ".jpg"));
			byte[] buff = new byte[1024];
			int len = 0;
			while((len = inputStream.read(buff, 0, 1024)) != -1){
				outputStream.write(buff, 0, len);
			}
			inputStream.close();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("end");
	}
	
	public void run(){
		while(curNum < 100){
			download();
			curNum ++;
		}
	}
	
	public static void main(String[] args){
//		new Crawl().run();
		
	}

}
