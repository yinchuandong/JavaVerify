package train;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import Base.Base;

public class Identy{
	
	private HashMap<Integer, String> labelMap = null;
	
	public Identy(){
		loadLabelMap();
	}
	
	private void loadLabelMap(){
		labelMap = new HashMap<Integer, String>();
		BufferedReader reader = null;
		try {
			 reader = new BufferedReader(new FileReader(new File("svm/label.txt")));
			 String buff = null;
			 while((buff = reader.readLine()) != null){
				 String[] arr = buff.split(" ");
				 labelMap.put(Integer.parseInt(arr[1]), arr[0]);
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
	
	
	private String getClassName(int label){
		return labelMap.get(label);
	}
	
	/**
	 * 具体的预测，返回识别的文字
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public String predict(File file) throws IOException{
		BufferedImage sourceImage = ImageIO.read(file);
		Preprocess preprocess = new Preprocess();
		BufferedImage binaryImage = preprocess.getBinaryImage(sourceImage);
		
		SegCfg segCfg = new SegCfg();
		ArrayList<BufferedImage> interList = segCfg.cfs(binaryImage);
		
		ArrayList<BufferedImage> imageList = new ArrayList<BufferedImage>();
		SegWaterDrop segWaterDrop = new SegWaterDrop();
		for (BufferedImage img : interList) {
			ArrayList<BufferedImage> tmpList = segWaterDrop.drop(img);
			for (BufferedImage sumImg : tmpList) {
				imageList.add(ImageUtil.scaleImage(sumImg));
			}
		}
		
		for (int i = 0; i < imageList.size(); i++) {
			ImageIO.write(imageList.get(i), "JPG", new File("tmp/" + i + ".jpg"));
		}
		
		Predict.run(imageList);
		
		String result = "";
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(new File("svm/result.txt")));
			String buff = "";
			
			while((buff = reader.readLine()) != null){
				int label = (int)Double.parseDouble(buff);
				String className = getClassName(label);
				result += className + " ";
			}
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if (reader != null) {
				reader.close();
			}
		}
		return result;
	}
	
	public static void main(String[] args) throws IOException{
		Identy index = new Identy();
//		index.predict(new File("download/1.jpg"));
		index.predict(new File("tmp/test.jpg"));
	}
}
