package com.mageddo.aptools.log;

import javax.annotation.processing.Messager;

import org.apache.commons.lang3.Validate;

public class LoggerFactory {

  private static ApLogger instance;

  private LoggerFactory() {
  }

  public static Logger getLogger() {
    return Validate.notNull(instance, "Logger not found");
  }

  public static Logger bindLogger(Messager messager) {
    return instance = new ApLogger(messager);
  }
}
