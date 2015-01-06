package com.concordiatec.vic.helper;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

public class DebugConverter implements Converter {
	@Override
	public String fromBody(TypedInput body, Type arg1) throws ConversionException {
		try {
			return inputStream2String(body.in());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public TypedOutput toBody(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String inputStream2String(InputStream in){
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		try {
			for (int n; (n = in.read(b)) != -1;) {
				out.append(new String(b, 0, n));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return out.toString();
	}
}
