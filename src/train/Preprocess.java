package train;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Preprocess {
	
	public Preprocess(){
		
	}
	
	private void run(){
		File dir = new File("download");
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
				BufferedImage img = ImageIO.read(file);
				BufferedImage binaryImg = getBinaryImage(img);
				ImageIO.write(binaryImg, "JPG", new File("1_gray/" + file.getName()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 二值化
	 * @param sourceImage
	 * @return 二值化之后的图像
	 */
	public BufferedImage getBinaryImage(BufferedImage sourceImage){
		double Wr = 0.299;
		double Wg = 0.587;
		double Wb = 0.114;
		
		int width = sourceImage.getWidth();
		int height = sourceImage.getHeight();
		int[][] gray = new int[width][height];
		
		//灰度化
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Color color = new Color(sourceImage.getRGB(x, y));
				int rgb = (int) ((color.getRed()*Wr + color.getGreen()*Wg + color.getBlue()*Wb) / 3);
				gray[x][y] = rgb;
			}
		}
		
		BufferedImage binaryBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
		//二值化
		int threshold = getOstu(gray, width, height);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (gray[x][y] > threshold) {
					int max = new Color(255, 255, 255).getRGB();
					gray[x][y] = max;
				}else{
					int min = new Color(0, 0, 0).getRGB();
					gray[x][y] = min;
				}
				
				binaryBufferedImage.setRGB(x, y, gray[x][y]);
			}
		}
		
		return binaryBufferedImage;
	}
	
	/**
	 * 获得二值化图像
	 * 最大类间方差法
	 * @param gray
	 * @param width
	 * @param height
	 */
	private int getOstu(int[][] gray, int width, int height){
		int grayLevel = 256;
		int[] pixelNum = new int[grayLevel];
		//计算所有色阶的直方图
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int color = gray[x][y];
				pixelNum[color] ++;
			}
		}
		
		double sum = 0;
		int total = 0;
		for (int i = 0; i < grayLevel; i++) {
			sum += i*pixelNum[i]; //x*f(x)质量矩，也就是每个灰度的值乘以其点数（归一化后为概率），sum为其总和
			total += pixelNum[i]; //n为图象总的点数，归一化后就是累积概率
		}
		double sumB = 0;//前景色质量矩总和
		int threshold = 0;
		double wF = 0;//前景色权重
		double wB = 0;//背景色权重
		
		double maxFreq = -1.0;//最大类间方差
		
		for (int i = 0; i < grayLevel; i++) {
			wB += pixelNum[i]; //wB为在当前阈值背景图象的点数
			if (wB == 0) { //没有分出前景后景
				continue;
			}
			
			wF = total - wB; //wB为在当前阈值前景图象的点数
			if (wF == 0) {//全是前景图像，则可以直接break
				break;
			}
			
			sumB += (double)(i*pixelNum[i]);
			double meanB = sumB / wB;
			double meanF = (sum - sumB) / wF;
			//freq为类间方差
			double freq = (double)(wF)*(double)(wB)*(meanB - meanF)*(meanB - meanF);
			if (freq > maxFreq) {
				maxFreq = freq;
				threshold = i;
			}
		}
		
		return threshold;
	}
	
	public static void main(String[] args){
		System.out.println("---begin---");
		
		long start = System.currentTimeMillis();
		Preprocess model = new Preprocess();
		model.run();
		long end = System.currentTimeMillis();
		
		System.out.println("耗时：" + (end - start));
		System.out.println("---end----");
	}
	
	
	
	
	
	
	
	
	
	
	

}
