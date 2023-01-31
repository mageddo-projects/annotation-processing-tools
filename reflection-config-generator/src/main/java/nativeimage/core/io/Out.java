package nativeimage.core.io;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.FileObject;

import com.fasterxml.jackson.databind.SequenceWriter;

import static nativeimage.core.io.ReflectionConfigFileCreator.buildFileObject;
import static nativeimage.core.io.ReflectionConfigFileCreator.createReflectionConfigWriter;

public class Out {
	private final SequenceWriter writer;
	private final URI uri;

	public Out(SequenceWriter writer, URI uri) {
		this.writer = writer;
		this.uri = uri;
	}

	public static Out of(ProcessingEnvironment processingEnv, String fileName) {
		try {
			final FileObject fileObject = buildFileObject(processingEnv, fileName);
			return new Out(createReflectionConfigWriter(fileObject.openOutputStream()), fileObject.toUri());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static Out of(Path path) {
		try {
			return new Out(createReflectionConfigWriter(Files.newOutputStream(path)), path.toUri());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public SequenceWriter getWriter() {
		return writer;
	}

	public URI getUri() {
		return uri;
	}
}
