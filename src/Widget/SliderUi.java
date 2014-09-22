package Widget;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JSlider;

public class SliderUi extends javax.swing.plaf.metal.MetalSliderUI {
	
	private String ballColor;
	private String fillColor;
	private String unFillColor;
	
	public SliderUi(){
		this("#75A101", "#75A101", "#737373");
	}
	
	/**
	 * 带颜色的填充
	 * @param ballColor 如：#282828
	 * @param fillColor 如：#75A101
	 * @param unFillColor 如：#737373
	 */
	public SliderUi(String ballColor, String fillColor, String unFillColor){
		this.ballColor = ballColor;
		this.fillColor = fillColor;
		this.unFillColor = unFillColor;
	}
    /**
     * 绘制指示物
     */
    public void paintThumb(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //填充椭圆框为当前thumb位置
        g2d.fillOval(thumbRect.x, thumbRect.y, thumbRect.width/2, thumbRect.height/2);
        //也可以帖图(利用鼠标事件转换image即可体现不同状态)
        //g2d.drawImage(image, thumbRect.x, thumbRect.y, thumbRect.width,thumbRect.height,null);
    }

    /** 
     * 绘制刻度轨迹
     */
    public void paintTrack(Graphics g) {
       
        if (slider.getOrientation() == JSlider.HORIZONTAL) {
        	drawHorizontal(g);
        } else {
        	drawVertical(g);
        }
    }
    
    private void drawHorizontal(Graphics g){
    	 int ch, cw;
         Rectangle trackBounds = trackRect;
         Graphics2D g2 = (Graphics2D) g;
         ch = 3;
         cw = trackBounds.width;
         
         //内部整个view的偏移
         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         g2.translate(trackBounds.x, trackBounds.y + ch + 3);

         // 背景设为灰色
         g2.setPaint(Color.decode(unFillColor));
         g2.fillRect(0, -ch + 1 , cw + thumbRect.width/2, ch );

         int trackLeft = 0;
         int trackRight = 0;
         trackRight = trackRect.width;

         int middleOfThumb = 0;

         int fillLeft = 0;

         int fillRight = 0;

         //坐标换算
         middleOfThumb = thumbRect.x + (thumbRect.width / 2);
         middleOfThumb -= trackRect.x;

         if (!drawInverted()) {
            fillLeft = !slider.isEnabled() ? trackLeft : trackLeft + 1;
            fillRight = middleOfThumb;
         } else {
            fillLeft = middleOfThumb;
            fillRight = !slider.isEnabled() ? trackRight - 1 : trackRight - 2;
         }
         // 设定左边背景
         g2.setPaint(Color.decode(fillColor));
         g2.fillRect(0, -ch + 1, fillRight - fillLeft + thumbRect.width/2, ch );

         //圆点的背景
         g2.setPaint(Color.decode(ballColor));
         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
         g2.translate(-trackBounds.x + thumbRect.width/2, -(trackBounds.y + ch + 1));
    }
    
    private void drawVertical(Graphics g){
    	 int ch, cw;
         Rectangle trackBounds = trackRect;
    	 Graphics2D g2 = (Graphics2D) g;
         ch = trackBounds.height;
         cw = 3;

         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                 RenderingHints.VALUE_ANTIALIAS_ON);
         g2.translate(trackBounds.x, trackBounds.y + ch);

         // 背景设为灰色
         g2.setPaint(Color.decode(fillColor));
         g2.fillRect(cw, -ch , cw, ch);

         int trackTop = 0;
         int trackBottom = 0;
         trackBottom = trackRect.height;

         int middleOfThumb = 0;
         int fillTop = 0;
         int fillBottom = 0;
         //坐标换算
         middleOfThumb = thumbRect.y + (thumbRect.height / 2);
         middleOfThumb -= trackRect.y;

         if (!drawInverted()) {
             fillTop = !slider.isEnabled() ? trackTop : trackTop + 1;
             fillBottom = middleOfThumb;
         } else {
             fillTop = middleOfThumb;
             fillBottom = !slider.isEnabled() ? trackBottom - 1 : trackBottom - 2;
         }
         // 设定上边背景
         g2.setPaint(Color.decode(unFillColor));
         g2.fillRect(cw, -ch, cw ,fillBottom - fillTop);

         //圆点的背景
         g2.setPaint(Color.decode(ballColor));
         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
         g2.translate(-trackBounds.x + 1, -(trackBounds.y + ch));
    }
}