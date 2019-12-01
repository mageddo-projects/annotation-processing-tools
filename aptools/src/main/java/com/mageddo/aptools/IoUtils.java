package com.mageddo.aptools;

import java.io.Closeable;
import java.io.IOException;

public class IoUtils {
	private IoUtils() {
	}

	/**
	 * A null safe close method
	 */
	public static void safeClose(Closeable c){
		if(c == null){
			return;
		}
		try {
			c.close();
		} catch (IOException e) {}
	}
}
