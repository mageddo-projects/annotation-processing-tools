package nativeimage.core.domain;

import java.util.List;

public class ReflectionConfig {

	private final String name;
	private final boolean allDeclaredConstructors;
	private final boolean allPublicConstructors;
	private final boolean allDeclaredMethods;
	private final boolean allPublicMethods;
	private final boolean allPublicFields;
	private final boolean allDeclaredFields;
	private final List<Method> methods;

	ReflectionConfig(
		String name, boolean allDeclaredConstructors, boolean allPublicConstructors,
		boolean allDeclaredMethods, boolean allPublicMethods,
		boolean allPublicFields, boolean allDeclaredFields,
		List<Method> methods
	) {
		this.name = name;
		this.allDeclaredConstructors = allDeclaredConstructors;
		this.allPublicConstructors = allPublicConstructors;
		this.allDeclaredMethods = allDeclaredMethods;
		this.allPublicMethods = allPublicMethods;
		this.allPublicFields = allPublicFields;
		this.allDeclaredFields = allDeclaredFields;
		this.methods = methods;
	}

	public static ReflectionConfigBuilder builder() {
		return new ReflectionConfigBuilder();
	}

	public String getName() {
		return this.name;
	}

	public boolean isAllDeclaredConstructors() {
		return this.allDeclaredConstructors;
	}

	public boolean isAllPublicConstructors() {
		return this.allPublicConstructors;
	}

	public boolean isAllDeclaredMethods() {
		return this.allDeclaredMethods;
	}

	public boolean isAllPublicMethods() {
		return this.allPublicMethods;
	}

	public boolean isAllPublicFields() {
		return this.allPublicFields;
	}

	public boolean isAllDeclaredFields() {
		return this.allDeclaredFields;
	}

	public List<Method> getMethods() {
		return methods;
	}

	public boolean equals(final Object o) {
		if (o == this) return true;
		if (!(o instanceof ReflectionConfig)) return false;
		final ReflectionConfig other = (ReflectionConfig) o;
		return this.getName().equals(other.getName());
	}

	public int hashCode() {
		return this.getName().hashCode();
	}

	public String toString() {
		return "ReflectionConfig(type=" + this.getName() + ")";
	}

	public static class ReflectionConfigBuilder {
		private String type;
		private boolean allDeclaredConstructors;
		private boolean allPublicConstructors;
		private boolean allDeclaredMethods;
		private boolean allPublicMethods;
		private boolean allPublicFields;
		private boolean allDeclaredFields;
		private List<Method> methods;

		ReflectionConfigBuilder() {
		}

		public ReflectionConfig.ReflectionConfigBuilder type(String type) {
			this.type = type;
			return this;
		}

		public ReflectionConfig.ReflectionConfigBuilder allDeclaredConstructors(boolean allDeclaredConstructors) {
			this.allDeclaredConstructors = allDeclaredConstructors;
			return this;
		}

		public ReflectionConfig.ReflectionConfigBuilder allPublicConstructors(boolean allPublicConstructors) {
			this.allPublicConstructors = allPublicConstructors;
			return this;
		}

		public ReflectionConfig.ReflectionConfigBuilder allDeclaredMethods(boolean allDeclaredMethods) {
			this.allDeclaredMethods = allDeclaredMethods;
			return this;
		}

		public ReflectionConfig.ReflectionConfigBuilder allPublicMethods(boolean allPublicMethods) {
			this.allPublicMethods = allPublicMethods;
			return this;
		}

		public ReflectionConfig.ReflectionConfigBuilder allPublicFields(boolean allPublicFields) {
			this.allPublicFields = allPublicFields;
			return this;
		}

		public ReflectionConfig.ReflectionConfigBuilder allDeclaredFields(boolean allDeclaredFields) {
			this.allDeclaredFields = allDeclaredFields;
			return this;
		}

		public ReflectionConfigBuilder methods(List<Method> methods) {
			this.methods = methods;
			return this;
		}

		public ReflectionConfig build() {
			return new ReflectionConfig(
				type, allDeclaredConstructors, allPublicConstructors,
				allDeclaredMethods, allPublicMethods, allPublicFields,
				allDeclaredFields, methods
			);
		}

	}
}
