package com.mobilewallet.utils;

import java.util.HashMap;
import java.util.Map;

public class IncomeInfo {
	public static Map<String, String> incomeMap = new HashMap<String, String>(8);

	static {
		incomeMap.put("Less than Rs 6250", "1");
		incomeMap.put("Rs 6250 - Rs 12500", "2");
		incomeMap.put("Rs 12500 - Rs 25000", "3");
		incomeMap.put("Rs 25000 - Rs 40000", "4");
		incomeMap.put("Rs 40000 - Rs 80000", "5");
		incomeMap.put("Rs 80000- Rs 100,000", "6");
		incomeMap.put("More than 100,000", "7");
		incomeMap.put("No Income", "8");
	}
}
