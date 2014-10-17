package test1;

import java.awt.Color;
import java.awt.Label;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageUtil {
	
	private HashMap<BufferedImage, String> trainMap;
	
	public ImageUtil(){
		try {
			loadTrain();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private int isBlack(int colorInt) {
		Color color = new Color(colorInt);
		if (color.getRed() + color.getGreen() + color.getBlue() <= 100) {
			return 1;
		}
		return 0;
	}
	
	private int isWhite(int colorInt) {
		Color color = new Color(colorInt);
		if (color.getRed() + color.getGreen() + color.getBlue() > 100) {
			return 1;
		}
		return 0;
	}
	
	
	/**
	 * remove background color, only white or black
	 * @param imgPath
	 * @return
	 * @throws IOException
	 */
	private BufferedImage removeBackground(String imgPath) throws IOException{
		BufferedImage image = ImageIO.read(new File(imgPath));
		int width = image.getWidth();
		int height = image.getHeight();
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (isWhite(image.getRGB(x, y)) == 1) {
					image.setRGB(x, y, Color.WHITE.getRGB());
				}else {
					image.setRGB(x, y, Color.BLACK.getRGB());
				}
			}
		}
		return image;
	}
	
	/**
	 * split the original image into 4 small images and add them to the list
	 * @param image
	 * @return
	 */
	private ArrayList<BufferedImage> splitImage(BufferedImage image){
//		System.out.println("splitImage:" + image.getWidth() + "-" + image.getHeight());
		ArrayList<BufferedImage> list = new ArrayList<>();
		list.add(image.getSubimage(10, 6, 8, 10));
		list.add(image.getSubimage(19, 6, 8, 10));
		list.add(image.getSubimage(28, 6, 8, 10));
		list.add(image.getSubimage(37, 6, 8, 10));
		return list;
	}
	
	/**
	 * load the train image set from file and put them into a hashMap
	 * @throws IOException
	 */
	private void loadTrain() throws IOException{
		trainMap = new HashMap<BufferedImage, String>();
		File dir = new File("train");
		File[] files = dir.listFiles();
		for (File file : files) {
			trainMap.put(ImageIO.read(file), file.getName().charAt(0)+"");
		}
	}
	
	/**
	 * process the single char, by comparing to the train files
	 * @param subImg
	 * @return
	 */
	private String getOneChar(BufferedImage subImg){
		String result = "-1";
		int width = subImg.getWidth();
		int height = subImg.getHeight();
		int minDiff = width*height;
		
		for (BufferedImage image : trainMap.keySet()) {
			int count = 0; //record the current difference
			
			Label1: for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					if (isWhite(subImg.getRGB(x, y)) != isWhite(image.getRGB(x, y))) {
						count++;
						if (count >= minDiff) {
							break Label1;
						}
					}
				}
			}
			
			if (count < minDiff) {
				minDiff = count;
				result = trainMap.get(image);
			}
		}
		return result;
	}
	
	/**
	 * really do the work to identify the number of image
	 * @param imgPath
	 * @return
	 * @throws IOException
	 */
	public String identify(String imgPath) throws IOException{
		BufferedImage image = removeBackground(imgPath);
		ArrayList<BufferedImage> list = splitImage(image);
		String result = "";
		for (int i = 0; i < list.size(); i++) {
			result += getOneChar(list.get(i));
		}
		return result;
	}
	
	public static void saveImage(BufferedImage image, String filePath){
		try {
			ImageIO.write(image, "JPG", new File(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args){
		try {
			ImageUtil model = new ImageUtil();
			File dir = new File("img1");
			File[] files = dir.listFiles();
			for (File file : files) {
				String number = model.identify(file.getPath());
				
				System.out.println(file.getName() + " --- " + number);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	

}
