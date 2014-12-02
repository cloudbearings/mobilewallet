package com.mobilewallet.recharge;

import java.util.HashMap;
import java.util.Map;

public class OperatorInfo {
	public static Map<String, String> operatorMap = new HashMap<String, String>(
			17);
	static {
		operatorMap.put("Airtel", "1");
		operatorMap.put("Vodafone", "2");
		operatorMap.put("BSNL", "3");
		operatorMap.put("Reliance(CDMA)", "4");
		operatorMap.put("Reliance(GSM)", "5");
		operatorMap.put("AirCel", "6");
		operatorMap.put("Idea", "8");
		operatorMap.put("T24", "19");
		operatorMap.put("Tata Indicom", "9");
		operatorMap.put("Tata Docomo", "13");
		operatorMap.put("LOOP(BPL)Mumbai", "10");
		operatorMap.put("MTS", "16");
		operatorMap.put("MTNL", "20");
		operatorMap.put("Uninor", "15");
		operatorMap.put("Videocon", "18");
		operatorMap.put("Virgin CDMA", "12");
		operatorMap.put("Virgin GSM", "14");
		operatorMap.put("Airtel PostPaid", "22");
		operatorMap.put("Vodafone PostPaid", "21");
		operatorMap.put("Reliance(CDMA) PostPaid", "24");
		operatorMap.put("Reliance(GSM) PostPaid", "25");
		operatorMap.put("Idea PostPaid", "23");
	}

}
