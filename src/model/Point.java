package model;

/**
 * 像素点
 * @author wangjiewen
 *
 */
public class Point{
	public int x;
	public int y;
	public boolean isBlack;
	
	public Point(int x, int y){
		this(x, y, true);
	}
	
	public Point(int x, int y, boolean isBlack) {
		this.x = x;
		this.y = y;
		this.isBlack = isBlack;
	}
}
