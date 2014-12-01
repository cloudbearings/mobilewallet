package com.mobilewallet.utils;

import java.util.HashMap;
import java.util.Map;

public class OccupationInfo {
	public static Map<String, String> occupationMap = new HashMap<String, String>(8);

	static {

		occupationMap.put("Artist", "1");
		occupationMap.put("Business / Self employed", "2");
		occupationMap.put("Consultant", "3");
		occupationMap.put("Director / Senior Management", "4");
		occupationMap.put("Doctor", "5");
		occupationMap.put("Engineer", "6");
		occupationMap.put("Executive / Middle level", "7");
		occupationMap.put("Housewife", "8");
		occupationMap.put("Manager", "9");
		occupationMap.put("Retired", "10");
		occupationMap.put("Social worker", "11");
		occupationMap.put("Student", "12");
		occupationMap.put("Teacher / Administrator", "13");
		occupationMap.put("Unemployed / Between jobs", "14");
		occupationMap.put("Other / None", "15");
	}
}
