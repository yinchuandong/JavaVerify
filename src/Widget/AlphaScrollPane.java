package Widget;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class AlphaScrollPane extends JScrollPane{
	
	private float alpha = 0.8f;
	
	public AlphaScrollPane(){
		this.setBorder(null);//去边框
		this.setOpaque(false);//将输入域设置成透明
		this.getViewport().setOpaque(false);
	}
	
	public void setAlpha(float alpha){
		this.alpha = alpha;
		this.invalidate();
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;  
        //设置透明度为0.4f  
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);  
        g2.setComposite(ac);  
        g2.setColor(Color.black);  
        //绘制一个与输入域等大小的填充矩形框来形成其半透明效果  
        g2.fillRect(0, 0, getWidth(), getHeight());  
	}

	
}
