import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.commons.compress.compressors.z.*;

public final class UnzipZFileUtility {

	public static void Unzipfile(String zipFilePath, String outFileName) throws IOException {
		InputStream fin = Files.newInputStream(Paths.get(zipFilePath));
		BufferedInputStream in = new BufferedInputStream(fin);
		OutputStream out = Files.newOutputStream(Paths.get(outFileName));
		ZCompressorInputStream zIn = new ZCompressorInputStream(in);
		final byte[] buffer = new byte[2048];
		int n = 0;
		while (-1 != (n = zIn.read(buffer))) {
			out.write(buffer, 0, n);
		}
		System.out.println("Archivo temporal creado para " + Paths.get(zipFilePath).getFileName());
		fin.close();
		in.close();
		out.close();
		zIn.close();
	}
}
