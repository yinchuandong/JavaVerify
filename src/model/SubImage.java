package model;

import java.util.ArrayList;


/**
 * 字符块图片
 * @author wangjiewen
 *
 */
public class SubImage{
	public ArrayList<Point> pixelList;
	public int left;
	public int top;
	public int right;
	public int bottom;
	public int width;
	public int height;
	
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
