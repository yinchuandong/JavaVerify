package train;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.imageio.ImageIO;

import svmHelper.svm_predict;
import svmHelper.svm_train;


public class Predict {
	
	/**
	 * 类标号map，如：a=>1 b=>2
	 */
	private HashMap<String, Integer> labelMap = null;
	
	private HashMap<String, Integer[][]> imageMap = null;
	
	public Predict(){
		init();
	}
	
	private void init(){
		labelMap = new HashMap<String, Integer>();
		imageMap = new HashMap<String, Integer[][]>();
		
		loadImageLabel();
		loadImage();
	}
	
	/**
	 * 加载类标号
	 */
	private void loadImageLabel(){
		BufferedReader reader = null;
		try {
			 reader = new BufferedReader(new FileReader(new File("svm/label.txt")));
			 String buff = null;
			 while((buff = reader.readLine()) != null){
				 String[] arr = buff.split(" ");
				 labelMap.put(arr[0], Integer.parseInt(arr[1]));
			 }
			 
			 System.out.println("load image label finish!");
			 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	private void loadImage(){
		File dir = new File("4_scale/");
		//只列出jpg
		File[] files = dir.listFiles(new FilenameFilter() {
			
			public boolean isJpg(String file){   
			    if (file.toLowerCase().endsWith(".jpg")){   
			      return true;   
			    }else{   
			      return false;   
			    }   
			}
			
			@Override
			public boolean accept(File dir, String name) {
				// TODO Auto-generated method stub
				return isJpg(name);
			}
		});
		
		for (File file : files) {
			try {
				transferToMap(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("load mage end");
		
	}
	
	/**
	 * 获得类标号
	 * @param className
	 * @return
	 */
	private int getClassLabel(String className){
		if(labelMap.containsKey(className)){
			return labelMap.get(className);
		}else{
			return -1;
		}
	}
	
	
	/**
	 * 将image 转换到 map中
	 * @param file
	 * @throws IOException
	 */
	private void transferToMap(File file) throws IOException{
		BufferedImage image = ImageIO.read(file);
		int width = image.getWidth();
		int height = image.getHeight();
		Integer[][] imgArr = new Integer[height][width];
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				//黑色点标记为1
				int value = ImageUtil.isBlack(image.getRGB(x, y)) ? 1 : 0;
				imgArr[y][x] = value;
			}
		}
		
		this.imageMap.put(file.getName(), imgArr);
	}
	
	/**
	 * 转成svm 测试集的格式
	 */
	public void svmFormat(){
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new File("svm/svm.test"));
			Iterator<String> iterator = this.imageMap.keySet().iterator();
			
			while (iterator.hasNext()) {
				String fileName = (String) iterator.next();
				String className = ImageUtil.getImgClass(fileName);
				int classLabel = getClassLabel(className);
				
				String tmpLine = classLabel + " ";
				Integer[][] imageArr = this.imageMap.get(fileName);
				
				int index = 1;
				for (int i = 0; i < imageArr.length; i++) {
					for (int j = 0; j < imageArr[i].length; j++) {
						tmpLine += index + ":" + imageArr[i][j] + " ";
						index ++;
					}
				}
				writer.write(tmpLine + "\r\n");
				writer.flush();
//				System.out.println(tmpLine);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if (writer != null) {
				writer.close();
			}
		}
		
	}
	
	public static void run() throws IOException{
		//predict参数
		String[] parg = {"svm/svm.test","svm/svm.model","svm/result.txt"};
		
		System.out.println("训练开始");
		svm_predict.main(parg);
		System.out.println("训练结束");
	}
	
	public static void main(String[] args){
//		Predict model = new Predict();
//		model.svmFormat();
		try {
			run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
