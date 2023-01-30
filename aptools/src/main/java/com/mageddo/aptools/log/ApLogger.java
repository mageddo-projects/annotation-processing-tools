package com.mageddo.aptools.log;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic.Kind;

import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 */
public class ApLogger implements Logger {

	public static final Kind LOG_LEVEL = Kind.OTHER;
	private final Messager messager;

	public ApLogger(Messager messager) {
		this.messager = messager;
	}

	@Override
	public void error(String format, Object... args) {
		if (args.length > 0 && args[args.length - 1] instanceof Throwable) {
			args[args.length - 1] = ExceptionUtils.getStackTrace((Throwable) args[args.length - 1]);
			this.log(
					Kind.ERROR,
					format + "\n%s",
					args
			);
		}
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
		if (level.ordinal() > LOG_LEVEL.ordinal()) {
			return;
		}
		messager.printMessage(level, String.format(format, args));
	}
}
