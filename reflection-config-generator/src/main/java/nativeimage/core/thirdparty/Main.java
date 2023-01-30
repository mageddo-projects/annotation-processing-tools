package nativeimage.core.thirdparty;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nativeimage.core.domain.ReflectionConfig;
import static nativeimage.core.domain.ReflectionConfig.withConstructors;
import nativeimage.core.io.Out;
import nativeimage.core.io.ReflectionConfigWriter;
import static nativeimage.core.thirdparty.ThirdPartyPackageScanner.findPackageClasses;

public class Main {

	public static final String HELP = "--help";
	public static final String CONSTRUCTORS = "--constructors";
	public static final String DECLARED_CONSTRUCTORS = "--declaredConstructors";
	public static final String PUBLIC_CONSTRUCTORS = "--publicConstructors";
	public static final String DECLARED_METHODS = "--declaredMethods";
	public static final String PUBLIC_METHODS = "--publicMethods";
	public static final String PUBLIC_FIELDS = "--publicFields";
	public static final String DECLARED_FIELDS = "--declaredFields";
	public static final Set<String> KNOWN_FLAGS = new HashSet<>(Arrays.asList(
			HELP,
			CONSTRUCTORS,
			DECLARED_CONSTRUCTORS,
			PUBLIC_CONSTRUCTORS,
			DECLARED_METHODS,
			PUBLIC_METHODS,
			PUBLIC_FIELDS,
			DECLARED_FIELDS
	));

	public static Map<String, Boolean> FLAGS = new HashMap<>();

	public static void main(String[] args) throws IOException {

		processArgs(args);

		final int from = args.length - 2;
		final String packageName = args[from];
		final Path outFilePath = Paths.get(args[from + 1]);

		System.out.printf(
				"generating reflect..., package=%s,  options=%s%n",
				packageName, Arrays.toString(args)
		);

		try (ReflectionConfigWriter writer = new ReflectionConfigWriter(Out.of(outFilePath))) {
			final Set<Class<?>> classes = findPackageClasses(packageName);
			for (Class<?> clazz : classes) {
				writer.write(toReflectionConfig(clazz));
			}
			System.out.printf(
					"package=%s, objects=%d, writtenTo=%s%n",
					packageName, classes.size(), outFilePath
			);
		}

	}

	private static void processArgs(String[] args) {
		for (int i = 0; i < args.length - 2; i++) {
			final String arg = args[i];
			if (KNOWN_FLAGS.contains(arg)) {
				if (arg.equals("--help")) {
					System.out.println(KNOWN_FLAGS);
					System.exit(0);
				}
				FLAGS.put(arg, true);
			} else {
				System.err.println("unknown option: " + arg);
				System.exit(1);
			}
		}
	}

	static ReflectionConfig toReflectionConfig(Class<?> clazz) {
		final ReflectionConfig.ReflectionConfigBuilder builder = ReflectionConfig
				.builder()
				.type(clazz.getName())
				.allDeclaredConstructors(getFlag(DECLARED_CONSTRUCTORS))
				.allDeclaredFields(getFlag(DECLARED_FIELDS))
				.allDeclaredMethods(getFlag(DECLARED_METHODS))
				.allPublicFields(getFlag(PUBLIC_FIELDS))
				.allPublicMethods(getFlag(PUBLIC_METHODS));
		if (getFlag(CONSTRUCTORS)) {
			withConstructors(builder);
		}
		return builder.build();
	}

	private static boolean getFlag(String k) {
		return FLAGS.get(k) != null ? FLAGS.get(k) : false;
	}
}
