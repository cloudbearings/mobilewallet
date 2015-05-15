package com.funnyzone.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

public class StringConverter implements Converter {

	@Override
	public Object fromBody(TypedInput typedInput, Type arg1)
			throws ConversionException {
		InputStreamReader input = null;
		BufferedReader br = null;
		String responseString = null;

		try {
			input = new InputStreamReader(typedInput.in());
			br = new BufferedReader(input);
			StringBuffer sb = new StringBuffer("");
			String output;
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}
			responseString = sb.toString();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (input != null)
					input.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				if (br != null)
					br.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
		return responseString;
	}

	@Override
	public TypedOutput toBody(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
