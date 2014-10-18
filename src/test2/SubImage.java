package test2;

import java.util.ArrayList;


/**
 * 字符块图片
 * @author wangjiewen
 *
 */
public class SubImage{
	ArrayList<Point> pixelList;
	int left;
	int top;
	int right;
	int bottom;
	int width;
	int height;
	
	public SubImage() {
		this.pixelList = new ArrayList<Point>();
		this.left = 0;
		this.top = 0;
		this.right = 0;
		this.bottom = 0;
		this.width = 0;
		this.height = 0;
	}
}
