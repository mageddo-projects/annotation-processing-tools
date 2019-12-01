package nativeimage.core;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.mageddo.aptools.IoUtils;
import nativeimage.core.domain.ReflectionConfig;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;

public class ReflectionConfigAppenderAnnotationProcessing implements ReflectionConfigAppender, Closeable {

	private final FileObject fileObject;
	private final SequenceWriter writer;

	public ReflectionConfigAppenderAnnotationProcessing(ProcessingEnvironment processingEnv, String classPackage) throws IOException {
		this.fileObject = processingEnv
			.getFiler()
			.createResource(
				StandardLocation.CLASS_OUTPUT, "",
				getFile(classPackage, "reflect.json")
			)
		;
		this.writer = createReflectionConfigWriter();
		this.createNativeImageProperties(processingEnv, classPackage);
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

	private SequenceWriter createReflectionConfigWriter() throws IOException {
		final DefaultPrettyPrinter.Indenter indenter = new DefaultIndenter(
			"  ", DefaultIndenter.SYS_LF
		);
		final DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
		printer.indentObjectsWith(indenter);
		printer.indentArraysWith(indenter);

		return new ObjectMapper()
			.setSerializationInclusion(Include.NON_NULL)
			.writer(printer)
			.writeValuesAsArray(fileObject.openOutputStream())
			;
	}

	private void createNativeImageProperties(ProcessingEnvironment processingEnv, String classPackage) throws IOException {
		final Writer nativeImagePropsWriter = processingEnv
			.getFiler()
			.createResource(
				StandardLocation.CLASS_OUTPUT, "",
				getFile(classPackage, "native-image.properties")
			)
			.openWriter();
		try {
			nativeImagePropsWriter.append("Args=-H:ReflectionConfigurationResources=${.}/reflect.json\n");
		} finally {
			IoUtils.safeClose(nativeImagePropsWriter);
		}
	}

	private String getFile(String classPackage, final String fileName) {
		return String.format("META-INF/native-image/%s/%s", classPackage, fileName);
	}

}
