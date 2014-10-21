package Base;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;


public class Base {
	
	
	/**
	 * 类标号map，如：a=>1 b=>2
	 */
	protected HashMap<String, Integer> labelMap = null;
	
	public Base(){
		labelMap = new HashMap<String, Integer>();
		loadImageLabel();
	}
	
	/**
	 * 加载类标号
	 */
	protected void loadImageLabel(){
		BufferedReader reader = null;
		try {
			 reader = new BufferedReader(new FileReader(new File("svm/label.txt")));
			 String buff = null;
			 while((buff = reader.readLine()) != null){
				 String[] arr = buff.split(" ");
				 labelMap.put(arr[0], Integer.parseInt(arr[1]));
			 }
			 
			 System.out.println("load image label finish!");
			 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
