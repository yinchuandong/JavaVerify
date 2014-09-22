package Widget;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Icon;
import javax.swing.JLabel;

public class AlphaLabel extends JLabel{
	private float alpha = 0.4f;
	private Color alphaColor;

	public AlphaLabel(){
		super();
	}
	public AlphaLabel(String text) {
		super(text);
	}
	
	public AlphaLabel(Icon icon){
		super(icon);
	}

	public void setAlpha(float alpha){
		this.alpha = alpha;
		this.invalidate();
	}

	public void setAlphaColor(Color alphaColor){
		this.alphaColor = alphaColor;
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;  
        //设置透明度为0.4f  
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);  
        g2.setComposite(ac);
        alphaColor = (alphaColor == null) ? Color.WHITE : alphaColor;
        g2.setColor(alphaColor);  
        //绘制一个与输入域等大小的填充矩形框来形成其半透明效果  
        g2.fillRect(0, 0, getWidth(), getHeight());  
	}

	
}
