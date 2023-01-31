package nativeimage.core.io;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.util.Set;

import nativeimage.core.ReflectionConfigAppender;
import nativeimage.core.domain.ReflectionConfig;

public class ReflectionConfigWriter implements ReflectionConfigAppender, Closeable {
	private final Out out;

	public ReflectionConfigWriter(Out out) {
		this.out = out;
	}

	@Override
	public void write(ReflectionConfig reflectionConfig) {
		try {
			this.out.getWriter().write(reflectionConfig);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void close() throws IOException {
		this.out.getWriter().close();
	}


	public void writeAll(Set<ReflectionConfig> classes) {
		for (ReflectionConfig config : classes) {
			this.write(config);
		}
	}

	public URI getFileUri() {
		return this.out.getUri();
	}
}
