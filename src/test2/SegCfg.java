package test2;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import javax.imageio.ImageIO;

public class SegCfg {
	
	/**
	 * cfg 分割出来的结果
	 */
	private ArrayList<BufferedImage> cfgList;
	
	public SegCfg(){
		this.init();
		
		try {
			BufferedImage sourceImage = ImageIO.read(new File("img2/1_13.jpg"));
			cfs(sourceImage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void init(){
		cfgList = new ArrayList<BufferedImage>();
	}
	
	public void cfs(BufferedImage sourceImage){
		int width = sourceImage.getWidth();
		int height = sourceImage.getHeight();
		
		ArrayList<SubImage> subImgList = new ArrayList<SubImage>(); //保存子图像
		HashMap<String, Boolean> trackMap = new HashMap<String, Boolean>(); //已经访问过的点
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int rgb = sourceImage.getRGB(x, y);
				String key = x + "-" + y;
				//如果不是黑色，或者已经被访问过，则跳过cfg
				if (!isBlack(rgb) || trackMap.containsKey(key)) {
					continue;
				}
				
				//如果黑色，且没有访问，则以此点开始进行连通域探索
				SubImage subImage = new SubImage();//保存当前字符块的坐标点
				LinkedList<Point> queue = new LinkedList<Point>();//保存当前字符块的访问队列
				queue.offer(new Point(x, y, true));
				trackMap.put(key, true);
				subImage.pixelList.add(new Point(x, y, true));
				subImage.left = x;
				subImage.top = y;
				subImage.right = x;
				subImage.bottom = y;
				
				while(queue.size() != 0){
					Point tmp = queue.poll();
					
					//搜寻目标的八个方向
					int startX = (tmp.x - 1 < 0) ? 0 : tmp.x-1;
					int startY = (tmp.y - 1 < 0) ? 0 : tmp.y-1;
					int endX = (tmp.x + 1 > width) ? width : tmp.x+1;
					int endY = (tmp.y + 1 > height) ? height : tmp.y+1;
					
					for (int tx = startX; tx <= endX; tx++) {
						for (int ty = startY; ty <= endY; ty++) {
//							if (tx == tmp.x && ty == tmp.y) {
//								continue;
//							}
							
							key = tx + "-" + ty;
							System.out.println(key);
							if (isBlack(sourceImage.getRGB(tx, ty)) && !trackMap.containsKey(key)) {
								queue.offer(new Point(tx, ty, true));
								trackMap.put(key, true);
								subImage.pixelList.add(new Point(tx, ty, true)); //加入到路径中
								
								//更新边界区域
								subImage.left = Math.min(subImage.left, tx);
								subImage.top = Math.min(subImage.top, ty);
								subImage.right = Math.max(subImage.right, tx);
								subImage.bottom = Math.max(subImage.bottom, ty);
							}
						}
					}
				}//end of while
				
				subImage.width = subImage.right - subImage.left + 1;
				subImage.height = subImage.bottom - subImage.top + 1;
				subImgList.add(subImage);
			}
		}
		
		System.out.println();
		cfsToImage(subImgList);
	}
	
	
	private void cfsToImage(ArrayList<SubImage> subImgList){
		for (int i = 0; i < subImgList.size(); i++) {
			SubImage subImage = subImgList.get(i);
			BufferedImage image = new BufferedImage(subImage.width, subImage.height, BufferedImage.TYPE_BYTE_BINARY);
			for (int x = 0; x < subImage.width; x++) {
				for (int y = 0; y < subImage.height; y++) {
					image.setRGB(x, y, new Color(255,255,255).getRGB());
				}
			}
			ArrayList<Point> pixeList = subImage.pixelList;
			for (Point point : pixeList) {
//				System.out.println("(" + (point.x - subImage.left) + "," + (point.y - subImage.top) + ")");
				image.setRGB(point.x - subImage.left, point.y - subImage.top, new Color(0, 0, 0).getRGB());
			}
			
			//将切割的中间图片加入到cfgList中
			this.cfgList.add(image);
			try {
				ImageIO.write(image, "JPG", new File("img2/13_"+i+".jpg"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean isBlack(int rgb) {
		Color color = new Color(rgb);
		if (color.getRed() + color.getGreen() + color.getBlue() <= 300) {
			return true;
		}
		return false;
	}
	
	
	
	public static void main(String[] args){
		SegCfg model = new SegCfg();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
