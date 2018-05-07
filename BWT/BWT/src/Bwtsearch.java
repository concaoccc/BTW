import java.awt.event.ItemEvent;
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;



public class Bwtsearch {
	/*
	 * BWT的编码
	 * 输入为原始字串，输出为编码后的字串
	 */
	static public String encodeCore(String input)
	{
		StringBuilder tmpResult = new StringBuilder();
		int length = input.length();
		//创建一个长度为length的数组
		ArrayList<String> table = new ArrayList<>(length);
		for (int i = 0; i < length; i++) {
			String tmp = input.substring(i)+input.substring(0,i);
			table.add(tmp);
		} 
		Collections.sort(table);
		for (int i = 0; i < length; i++) {
			tmpResult.append(table.get(i).charAt(length-1));
		}
		return tmpResult.toString();
	}
	
	/*
	 * 解码程序
	 * 输入为编码后的字串
	 * 输出为原始字符串
	 */
	static public String decodeCore(String encode, int begin, int[] lIndex)
	{
		String F = getFrist(encode);
		//统计字符串,存储字符和第一次出现的时间
		HashMap<Character, Integer> fristMap = new HashMap<>();
		char tmp = F.charAt(0);
		fristMap.put(tmp, 0);
		int Frisrindex = 1;
		//对排序后的第一行的字符串进行统计
		while (Frisrindex < F.length()) {
			if (tmp == F.charAt(Frisrindex))
			{
				Frisrindex++;
				continue;
			}
			else {
				fristMap.put(F.charAt(Frisrindex), Frisrindex);
				tmp = F.charAt(Frisrindex);
				Frisrindex++;
			}
		}
		
		StringBuilder result = new StringBuilder();
		int num = 0;
		int tmpIndex = begin;
		while(num < encode.length())
		{
			result.append(encode.charAt(tmpIndex));
			tmpIndex = fristMap.get(encode.charAt(tmpIndex))+lIndex[tmpIndex];
			num++;
		}
		result = result.reverse();
		return result.toString();
		
	}
	/*
	 * 获取编码对应的第一列的数据
	 */
	static public String getFrist(String encode)
	{
		char[] charOfEncode = encode.toCharArray();
		Arrays.sort(charOfEncode);
		return new String(charOfEncode);
	}
	/*
	 * 获得编码结果的index
	 */
	static public int[] getIndex(String encode)
	{
		HashMap<Character, Integer> tmp = new HashMap<>();
		int[] index = new int[encode.length()];
		for (int i = 0; i < encode.length(); i++) {
			if(tmp.containsKey(encode.charAt(i)))
			{
				index[i] = tmp.get(encode.charAt(i));
				tmp.put(encode.charAt(i), index[i]+1);
			}
			else {
				index[i] = 0;
				tmp.put(encode.charAt(i), 1);
			}
		}
		return index;
	}
	
	/*
	 * 在编码文件encode中寻找P
	 */
	static public int bwtSearch(String encode, String p, int[] lIndex)
	{
		String F = getFrist(encode);
		//统计字符串,存储字符和第一次出现的时间
		HashMap<Character, Integer> fristMap = new HashMap<>();
		char tmp = F.charAt(0);
		fristMap.put(tmp, 0);
		int Frisrindex = 1;
		//对排序后的第一行的字符串进行统计
		while (Frisrindex < F.length()) {
			if (tmp == F.charAt(Frisrindex))
			{
				Frisrindex++;
				continue;
			}
			else {
				fristMap.put(F.charAt(Frisrindex), Frisrindex);
				tmp = F.charAt(Frisrindex);
				Frisrindex++;
			}
		}
		int Plength = p.length()-1;
		Vector<Integer> targetRows = new Vector<>();
		//第一次首先将所有的字符串的最后一位全部加入
		if (p.length()==0)
		{
			return 0;
		}
		char target = p.charAt(Plength);
		if(!fristMap.containsKey(target))
		{
			return 0;
		}
		int initIndex = fristMap.get(target);
		while(initIndex < encode.length())
		{
			if (F.charAt(initIndex) == target)
			{
				targetRows.add(initIndex);
				initIndex++;
			}
			else {
				break;
			}
		}
		Plength--;
		
		while (Plength >= 0 && targetRows.size() > 0) {
			//对于所有潜在的目标，寻找可能的下一行
			Vector<Integer> nextTarget = new Vector<>();
			target = p.charAt(Plength);
			for(int chooseIndex : targetRows)
			{
				if (encode.charAt(chooseIndex) == target)
				{
					nextTarget.add(fristMap.get(encode.charAt(chooseIndex))+lIndex[chooseIndex]);
				}
			}
			//重新进行覆盖
			targetRows = nextTarget;
			Plength--;
		}
		return targetRows.size();
	}
	public static void main(String[] args) {
		String test = "Tomorrow and tomorrow and tomorrow#";
//		String test = "abaaba#";
		String encode = encodeCore(test);
		System.out.println(encode);
		int begin = encode.indexOf("#");
		System.out.printf("#所在的index:%d\n",begin);
		//获得最后一行的index
		int[] lIndex = getIndex(encode);
		String decode = decodeCore(encode, begin, lIndex);
		System.out.println(decode);
//		String p = "aba";
		String p = " ";
		int num = bwtSearch(encode, p, lIndex);
		System.out.println(num);
	}
}
