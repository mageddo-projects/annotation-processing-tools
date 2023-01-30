package nativeimage.core.io;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.FileObject;

import com.fasterxml.jackson.databind.SequenceWriter;

import static nativeimage.core.io.ReflectionConfigFileCreator.buildFileObject;
import static nativeimage.core.io.ReflectionConfigFileCreator.createReflectionConfigWriter;
import nativeimage.core.ReflectionConfigAppender;
import nativeimage.core.domain.ReflectionConfig;

public class ReflectionConfigWriter implements ReflectionConfigAppender, Closeable {
	private final SequenceWriter writer;
	private final URI fileUri;

	public ReflectionConfigWriter(ProcessingEnvironment processingEnv, String fileName) {
		final FileObject fileObject = buildFileObject(processingEnv, fileName);
		this.writer = createReflectionConfigWriter(fileObject);
		this.fileUri = fileObject.toUri();
	}

	@Override
	public void append(ReflectionConfig reflectionConfig) {
		try {
			this.writer.write(reflectionConfig);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void close() throws IOException {
		this.writer.close();
	}


	public void writeAll(Set<ReflectionConfig> classes) {
		for (ReflectionConfig config : classes) {
			this.append(config);
		}
	}

	public URI getFileUri() {
		return fileUri;
	}
}
