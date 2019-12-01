package com.mageddo.aptools.log;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic.Kind;

public class ApLogger implements Logger {

	private final Messager messager;

	public ApLogger(Messager messager) {
		this.messager = messager;
	}

	@Override
	public void error(String format, Object... args) {
		this.log(Kind.ERROR, format, args);
	}

	@Override
	public void warn(String format, Object... args) {
		this.log(Kind.WARNING, format, args);
	}

	@Override
	public void info(String format, Object... args) {
		this.log(Kind.NOTE, format, args);
	}

	@Override
	public void debug(String format, Object... args) {
		this.log(Kind.OTHER, format, args);
	}

	private void log(Kind level, String format, Object[] args) {
		if(level.ordinal() > Kind.NOTE.ordinal()){
			return;
		}
		messager.printMessage(level, String.format(format, args));
	}
}
