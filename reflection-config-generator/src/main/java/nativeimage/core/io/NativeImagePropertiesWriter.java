package nativeimage.core.io;

import java.io.IOException;
import java.io.Writer;
import java.net.URI;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import com.mageddo.aptools.IoUtils;

import nativeimage.core.NativeImages;

public class NativeImagePropertiesWriter {
	public static URI write(
			ProcessingEnvironment processingEnv, String packageName, String... reflectionResources
	) {
		Writer nativeImagePropsWriter = null;
		try {
			final FileObject fileObject = processingEnv
					.getFiler()
					.createResource(
							StandardLocation.CLASS_OUTPUT, "",
							NativeImages.solvePath(packageName, "native-image.properties")
					);
			nativeImagePropsWriter = fileObject .openWriter();
			nativeImagePropsWriter.append(String.format(
					String.format(
							"Args=-H:ReflectionConfigurationResources=%s\n",
							buildResourcePaths(reflectionResources)
					)
			));
			return fileObject.toUri();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			IoUtils.safeClose(nativeImagePropsWriter);
		}
	}

	private static String buildResourcePaths(String[] resources) {
		if(resources.length == 0){
			return "";
		}
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < resources.length - 1; i++) {
			sb.append("${.}/");
			sb.append(resources[i]);
			sb.append('|');
		}
		sb.append("${.}/");
		sb.append(resources[resources.length - 1]);
		return sb.toString();
	}
}
