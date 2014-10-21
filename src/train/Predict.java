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
	
	
	/**
	 * 加载图片，供测试用
	 */
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
	private void svmFormat(){
		
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
	
	/**
	 * 转换为svm的格式
	 * @param imageList
	 */
	private void svmFormat(ArrayList<BufferedImage> imageList){
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new File("svm/svm.test"));
			for (BufferedImage image : imageList) {
				int width = image.getWidth();
				int height = image.getHeight();
				int index = 1;
				
				String tmpLine = "-1 ";//默认无标号，则为-1
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						//黑色点标记为1
						int value = ImageUtil.isBlack(image.getRGB(x, y)) ? 1 : 0;
						tmpLine += index + ":" + value + " ";
						index ++;
					}
				}
				writer.write(tmpLine + "\r\n");
				writer.flush();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if (writer != null) {
				writer.close();
			}
		}
	}
	
	/**
	 * 公共接口
	 * @param imageList
	 * @throws IOException
	 */
	public static void run(ArrayList<BufferedImage> imageList) throws IOException{
		Predict model = new Predict();
		model.svmFormat(imageList);
		
		//predict参数
		String[] parg = {"svm/svm.test","svm/svm.model","svm/result.txt"};
		
		System.out.println("预测开始");
		svm_predict.main(parg);
		System.out.println("预测结束");
	}
	
	public static void main(String[] args){
//		Predict model = new Predict();
//		model.loadImage();
//		model.svmFormat();
		
		try {
			//predict参数
			BufferedImage image = ImageIO.read(new File("4_scale/2-223.jpg"));
			ArrayList<BufferedImage> list = new ArrayList<BufferedImage>();
			list.add(image);
			run(list);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	
	
}
