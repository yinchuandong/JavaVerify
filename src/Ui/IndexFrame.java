package Ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;

import train.Crawl;
import train.Identy;
import Widget.ScaleIcon;

public class IndexFrame extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JButton chooseBtn;
	private JLabel imgLabel;
	
	private Identy identy;
	private JButton downloadBtn;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IndexFrame frame = new IndexFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public IndexFrame() {
		this.identy = new Identy();
		this.initComponents();
		this.bindEvent();
		
	}
	
	private void bindEvent(){
		this.downloadBtn.setVisible(false);
		//绑定下载按钮的事件
		downloadBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					File file = new Crawl().download(new File("download2"));
					imgLabel.setIcon(new ScaleIcon(new ImageIcon(file.getPath())));
					String reuslt = identy.predict(file);
					textField.setText(reuslt);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
		
		//绑定选择文件按钮的事件
		chooseBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser("download");
				fileChooser.setMultiSelectionEnabled(false);
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int result = fileChooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					try {
						imgLabel.setIcon(new ScaleIcon(new ImageIcon(file.getPath())));
						String reuslt = identy.predict(file);
						textField.setText(reuslt);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
	}
	
	private void initComponents(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 167);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		chooseBtn = new JButton("选择图片");
		chooseBtn.setBounds(194, 63, 96, 29);
		
		imgLabel = new JLabel("");
		imgLabel.setBounds(162, 6, 102, 40);
		
		textField = new JTextField();
		textField.setBounds(11, 100, 286, 28);
		textField.setColumns(10);
		contentPane.setLayout(null);
		
		downloadBtn = new JButton("下载图片");
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, 256, GroupLayout.PREFERRED_SIZE)
						.addComponent(imgLabel, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(downloadBtn)
						.addComponent(chooseBtn))
					.addContainerGap(55, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(26)
					.addComponent(imgLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(23, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(39, Short.MAX_VALUE)
					.addComponent(downloadBtn)
					.addGap(18)
					.addComponent(chooseBtn)
					.addGap(20))
		);
		getContentPane().setLayout(groupLayout);
		
		
	}
}
