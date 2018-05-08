import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.text.AbstractDocument.BranchElement;



public class Bwtsearch {
	/*
	 * BWT�ı���
	 * ����Ϊԭʼ�ִ������Ϊ�������ִ�
	 */
	static public String encodeCore(String input)
	{
		StringBuilder tmpResult = new StringBuilder();
		int length = input.length();
		//����һ������Ϊlength������
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
	 * �������
	 * ����Ϊ�������ִ�
	 * ���Ϊԭʼ�ַ���
	 */
	static public String decodeCore(String encode, int begin, int[] lIndex)
	{
		String F = getFrist(encode);
		//ͳ���ַ���,�洢�ַ��͵�һ�γ��ֵ�ʱ��
		HashMap<Character, Integer> fristMap = new HashMap<>();
		//�����ȡ��һ��
		char tmp = F.charAt(0);
		fristMap.put(tmp, 0);
		int Frisrindex = 1;
		//�������ĵ�һ�е��ַ�������ͳ��
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
	 * ��ȡ�����Ӧ�ĵ�һ�е�����
	 */
	static public String getFrist(String encode)
	{
		char[] charOfEncode = encode.toCharArray();
		Arrays.sort(charOfEncode);
		return new String(charOfEncode);
	}
	/*
	 * ��ñ�������index
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
	 * �ڱ����ļ�encode��Ѱ��P
	 */
	static public int bwtSearch(String encode, String p, int[] lIndex)
	{
		
		String F = getFrist(encode);
		//ͳ���ַ���,�洢�ַ��͵�һ�γ��ֵ�ʱ��
		HashMap<Character, Integer> fristMap = new HashMap<>();
		char tmp = F.charAt(0);
		fristMap.put(tmp, 0);
		int Frisrindex = 1;
		//�������ĵ�һ�е��ַ�������ͳ��
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
		//��һ�����Ƚ����е��ַ��������һλȫ������
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
			//��������Ǳ�ڵ�Ŀ�꣬Ѱ�ҿ��ܵ���һ��
			Vector<Integer> nextTarget = new Vector<>();
			target = p.charAt(Plength);
			for(int chooseIndex : targetRows)
			{
				if (encode.charAt(chooseIndex) == target)
				{
					nextTarget.add(fristMap.get(encode.charAt(chooseIndex))+lIndex[chooseIndex]);
				}
			}
			//���½��и���
			targetRows = nextTarget;
			Plength--;
		}
		return targetRows.size();
	}
	
	static ArrayList<String> ReadFile(String inputPath)
	{
		ArrayList<String> result = new ArrayList<>();
		try {
			FileReader fr = new FileReader(inputPath);
			BufferedReader br = new BufferedReader(fr);
			String data = br.readLine();
			while(data != null)
			{
				result.add(data);
				data = br.readLine();
			}
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;	
	}
	
	static void writeFile(String outputPath, ArrayList<String> outputData)
	{
		FileWriter fWriter;
		try {
			fWriter = new FileWriter(outputPath);
			BufferedWriter  bw=new BufferedWriter(fWriter);
			for(String arr: outputData){
	            bw.write(arr+"\r\n");
	        }
			bw.close();
			fWriter.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void encode(String inputPath, String outputPath)
	{
		ArrayList<String> read = ReadFile(inputPath);
		ArrayList<String> readData = new ArrayList<>();
		for (String data: read) {
			StringBuilder withEnd = new StringBuilder();
			withEnd.append(data);
			withEnd.append("#");
			readData.add(withEnd.toString());
		}
		ArrayList<String> encodeResult = new ArrayList<>();
		for(String data : readData)
		{
			encodeResult.add(encodeCore(data));
		}
		writeFile(outputPath, encodeResult);
	}
	
	public static void decode(String inputPath)
	{
		ArrayList<String> readData = ReadFile(inputPath);
		for (String data : readData) {
			int begin = data.indexOf("#");
			int[] lIndex = getIndex(data);
			System.out.println(decodeCore(data, begin, lIndex));
		}
	}
	
	public static void search(String inputPath, String P)
	{
		ArrayList<String> readData = ReadFile(inputPath);
		for (int i =0; i < readData.size(); i++) {
			String data = readData.get(i);
			int[] lIndex = getIndex(data);
			int num = bwtSearch(data, P, lIndex);
			if (num > 0)
			{
				System.out.printf("��%d����%d����Ҫ�������ַ�\n", i+1, num);
			}
		}
	}
	public static void main(String[] args) {
		encode("Input/input.txt","Encoded/encoded.txt");
		decode("Encoded/encoded.txt");
		search("Encoded/encoded.txt", "I");
	}
}
