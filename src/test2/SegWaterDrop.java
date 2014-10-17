package test2;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SegWaterDrop {
	
	public SegWaterDrop() {
		
		try {
			BufferedImage sourceImage = ImageIO.read(new File("img2/2_1.jpg"));
			drop(sourceImage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void drop(BufferedImage sourceImage){
		
		int width = sourceImage.getWidth();
		int height = sourceImage.getHeight();
		
		
		
		System.out.println(width+","+height);
	}
	
	public static void main(String[] args){
		
		SegWaterDrop model = new SegWaterDrop();
	}

}
