package nativeimage.core.io;

import java.io.IOException;
import java.io.OutputStream;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;

public class ReflectionConfigFileCreator {

	public static FileObject buildFileObject(ProcessingEnvironment processingEnv, String fileName) {
		try {
			return processingEnv
					.getFiler()
					.createResource(
							StandardLocation.CLASS_OUTPUT, "",
							fileName
					);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static SequenceWriter createReflectionConfigWriter(final OutputStream out) {
		final DefaultPrettyPrinter.Indenter indenter = new DefaultIndenter(
				"  ", DefaultIndenter.SYS_LF
		);
		final DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
		printer.indentObjectsWith(indenter);
		printer.indentArraysWith(indenter);
		try {
			return new ObjectMapper()
					.setSerializationInclusion(JsonInclude.Include.NON_NULL)
					.writer(printer)
					.writeValuesAsArray(out)
					;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
