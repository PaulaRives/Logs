import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Main {

	public static void main(String[] args) {

		long startTime = System.nanoTime();

		Path searchDir = Paths.get("E:\\");
		final PathMatcher matcher = searchDir.getFileSystem().getPathMatcher("glob:**/*.Z");
		System.out.println("Iniciando.....");
		try (final Stream<Path> stream = Files.list(searchDir)) {
			stream.filter(matcher::matches).forEach(zipFile -> {
				try {
					File temp = new File("temp.txt");
					UnzipZFileUtility.Unzipfile(zipFile.toString(), temp.getName());
					ProcessLogFile.Write("temp.txt");
					System.out.println(temp.delete() ? "Archivo extraido temporalmente borrado"
							: "ERROR: Archivo extraido temporalmente no pudo ser borrado");
					System.out.println("Siguiente.....");
				} catch (IOException e) {
					e.printStackTrace();
				}

			});
			System.out.println("Fin.....");
			try (FileReader input = new FileReader("result1.txt");
					LineNumberReader count = new LineNumberReader(input);) {
				while (count.skip(Long.MAX_VALUE) > 0) {
					// Loop just in case the file is > Long.MAX_VALUE or skip() decides to not read
					// the entire file
				}

				System.out.println(count.getLineNumber());
			}

		} catch (IOException e) {
			System.out.println("Se ha producido un error: " + e.getMessage());
		} finally {
			long endTime = System.nanoTime();
			System.out.println("Duración: " + (double) (endTime - startTime) / 1000000000.0 + " segundos");
		}
	}

}
