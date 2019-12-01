package com.mageddo.aptools.log;

public interface Logger {

	void error(String format, Object... args);

	void warn(String format, Object... args);

	void info(String format, Object... args);

	void debug(String format, Object... args);

}
