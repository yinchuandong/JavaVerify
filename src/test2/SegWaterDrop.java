package test2;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class SegWaterDrop {
	
	private int minD = 8;//最小字符宽度
	private int maxD = 15;
	private int meanD = 12;//平均字符宽度
	
	private ArrayList<BufferedImage> imageList;
	
	
	public SegWaterDrop() {
		imageList = new ArrayList<BufferedImage>();
		
	}
	
	
	public void run(){
		try {
			File file = new File("2_cfs/97-0.jpg");
			BufferedImage sourceImage = ImageIO.read(file);
			ArrayList<BufferedImage> list = drop(sourceImage);
			for(int j=0; j<list.size(); j++){
				BufferedImage subImg = list.get(j);
				String prex = file.getName().split("\\.")[0];
				String filename = "3_drop/" + prex + "-" + j + ".jpg";
				ImageIO.write(subImg, "JPG", new File(filename));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 滴水法入口
	 * @param sourceImage
	 * @return 切割完图片的数组
	 */
	public ArrayList<BufferedImage> drop(BufferedImage sourceImage){
		imageList.clear();
		int width = sourceImage.getWidth();
		int height = sourceImage.getHeight();
		
		//在x轴的投影
		int[] histData = new int[width];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (isBlack(sourceImage.getRGB(x, y))) {
					histData[x]++;
				}
			}
		}
		
		ArrayList<Integer> extrems = Extremum.getMinExtrem(histData);
	
		Point[] startRoute = new Point[height];
		Point[] endRoute = null;
		
		for(int y=0; y < height; y++){
			startRoute[y] = new Point(0, y);
		}
		
		int num = (int)Math.round((double)width/meanD);//字符的个数
		int lastP = 0; //上一次分割的位置
		int curSplit = 1;//分割点的个数，小于等于 num - 1;
		for (int i = 0; i < extrems.size(); i++) {
			if (curSplit > num - 1) {
				break;
			}
			
			//判断两个分割点之间的距离是否合法
			int curP = extrems.get(i);
			int dBetween = curP - lastP + 1;
			if (dBetween < minD || dBetween > maxD) {
				continue;
			}
			
//			//判断当前分割点与末尾结束点的位置是否合法
//			int dAll = width - curP + 1;
//			if (dAll < minD*(num - curSplit) || dAll > maxD*(num - curSplit)) {
//				continue;
//			}
			endRoute = getEndRoute(sourceImage, new Point(curP, 0), height);
			doSplit(sourceImage, startRoute, endRoute);
			startRoute = endRoute;
			lastP = curP;
			curSplit ++;
		}
		
		endRoute = new Point[height];
		for(int y=0; y < height; y++){
			endRoute[y] = new Point(width - 1, y);
		}
		doSplit(sourceImage, startRoute, endRoute);
		
		System.out.println("=================");
		System.out.println(width+","+height);
		
		return this.imageList;
	}
	
	/**
	 * 获得滴水的路径
	 * @param startP
	 * @param height
	 * @return
	 */
	private Point[] getEndRoute(BufferedImage sourceImage, Point startP, int height){
		
		//获得分割的路径
		Point[] endRoute = new Point[height];
		Point curP = new Point(startP.x, startP.y);
		Point lastP = curP;
		endRoute[0] = curP;
		while(curP.y < height - 1){
			int maxW = 0;
			int sum = 0;
			int nextX = curP.x;
			int nextY = curP.y;
			
			int[][] orderArr = {	{5, 0, 4},
									{1, 2, 3}};
			for(int y = curP.y, i = 0; y <= curP.y + 1 && i < 2; y++, i++){
				for(int x = curP.x - 1, j = 0; x <= curP.x + 1 && j < 3; x++, j++){
					if (orderArr[i][j] == 0) {
						continue;
					}
					int curW = getPixelValue(sourceImage.getRGB(x, y)) * (6 - orderArr[i][j]);
					sum += curW;
					if (curW > maxW) {
						maxW = curW;
						nextX = x;
						nextY = y;
					}
				}
			}

			if (lastP.x == nextX && lastP.y == nextY) {//如果出现重复运动
				if (nextX < curP.x) {//向左重复
					maxW = 5;
					nextX = curP.x - 1;
					nextY = curP.y + 1;
				}else{//向右重复
					maxW = 3;
					nextX = curP.x + 1;
					nextY = curP.y + 1;
				}
			}else{
				if (sum == 0 ) {
					maxW = 4;
				}
				//如果周围全白，则默认垂直下落
				if (sum == 15) {
					maxW = 6;
					nextX = curP.x;
					nextY = curP.y + 1;
				}
			}
			
			switch (maxW) {
			case 4:
				if (nextX > curP.x) {//具有向右的惯性
					nextX = curP.x + 1;
					nextY = curP.y + 1;
				}
				
				if (nextX < curP.x) {//向左的惯性或者sum = 0
					nextX = curP.x;
					nextY = curP.y + 1;
				}
				
				if (sum == 0) {
					nextX = curP.x;
					nextY = curP.y + 1;
				}
				break;

			default:
				
				break;
			}
			lastP = curP;
			curP = new Point(nextX, nextY);
			
			endRoute[curP.y] = curP;
		}
	
		return endRoute;
	}
	
	/**
	 * 具体实行切割
	 * @param sourceImage
	 * @param starts
	 * @param ends
	 */
	private void doSplit(BufferedImage sourceImage, Point[] starts, Point[] ends){
		int left = starts[0].x;
		int top = starts[0].y;
		int right = ends[0].x;
		int bottom = ends[0].y;
		
		for (int i = 0; i < starts.length; i++) {
			left = Math.min(starts[i].x, left);
			top = Math.min(starts[i].y, top);
			right = Math.max(ends[i].x, right);
			bottom = Math.max(ends[i].y, bottom);
		}
		
		int width = right - left + 1;
		int height = bottom - top + 1;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, new Color(255, 255, 255).getRGB());
			}
		}
		for (int i = 0; i < ends.length; i++) {
			Point start = starts[i];
			Point end = ends[i];
			for (int x = start.x; x < end.x; x++) {
				if (isBlack(sourceImage.getRGB(x, i))) {
					System.out.println((x - left) + ", " + (start.y - top));
					image.setRGB(x - left, start.y - top, new Color(0, 0, 0).getRGB());
				}
			}
			
		}
		this.imageList.add(image);
		
		System.out.println("-----------------------");
		
	}
	
	
	public boolean isBlack(int rgb) {
		Color color = new Color(rgb);
		if (color.getRed() + color.getGreen() + color.getBlue() <= 300) {
			return true;
		}
		return false;
	}
	
	public int getPixelValue(int rgb){
		if (isBlack(rgb)) {
			return 0;
		}else{
			return 1;
		}
	}
	
	
	public static void main(String[] args){
		
		SegWaterDrop model = new SegWaterDrop();
		model.run();
	}

}
