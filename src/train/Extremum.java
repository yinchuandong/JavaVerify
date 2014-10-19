package train;

import java.util.ArrayList;

public class Extremum {

	public static ArrayList<Integer> getMinExtrem(int[] data){
		
		int min = data[0];
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		int miniSpan = 5; //规定最小跨度
		int lastEqIndex = 0;
		
		for(int i=1; i<data.length - 1; i++){
			if (data[i] > data[i+1]) {
				min = data[i+1];
			}
			
			// 保存101的这种类型的极值
			if (data[i] < data[i+1] && data[i] <= min) {
				//判断两个极小值之间的距离是否满足最小跨度
				if ((list.size() != 0 && (i - list.get(list.size() - 1) >= miniSpan)) || (list.size() == 0)) {
					list.add(i);
				}
			}
			
			// 保存100000001这种类型的极值，只取第一个0，和最后一个0为极值点
			if (data[i] == data[i+1]) {
				if (lastEqIndex + 1 != i) {
					if ((list.size() != 0 && (i - list.get(list.size() - 1) >= miniSpan)) || (list.size() == 0)) {
						list.add(i);
					}
				}
				lastEqIndex = i;
			}
		}
		
		for (Integer integer : list) {
			System.out.print(integer+" ");
		}
		
		return list;
	}
	
	public static void main(String[] args){
		int[] data = {5,4,3,2,3,4,5,1,1,1,1,1,5,2,2,2,2,2,2,6};
		getMinExtrem(data);
	}
}
