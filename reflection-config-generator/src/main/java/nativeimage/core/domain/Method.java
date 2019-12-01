package nativeimage.core.domain;

import java.util.Arrays;
import java.util.List;

public class Method {

	private final String name;
	private final List<String> parameterTypes;

	public Method(String name, List<String> parameterTypes) {
		this.name = name;
		this.parameterTypes = parameterTypes;
	}

	public static Method of(String name, String ... type){
		return new Method(name, Arrays.asList(type));
	}

	public String getName() {
		return name;
	}

	public List<String> getParameterTypes() {
		return parameterTypes;
	}
}
