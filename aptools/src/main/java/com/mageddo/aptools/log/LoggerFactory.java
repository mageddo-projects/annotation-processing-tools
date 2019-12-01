package com.mageddo.aptools.log;

import javax.annotation.processing.Messager;

import org.apache.commons.lang3.Validate;

public class LoggerFactory {

	private LoggerFactory() {
	}

	public static Logger getLogger(){
		return Validate.notNull((Logger) System.getProperties().get("aptools.logger"), "Logger not found");
	}

	public static Logger bindLogger(Messager messager) {
		Logger logger = new ApLogger(messager);
		System.getProperties().put("aptools.logger", logger);
		return logger;
	}
}
