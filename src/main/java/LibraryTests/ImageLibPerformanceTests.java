package LibraryTests;
import java.io.File;

public class ImageLibPerformanceTests {

	/**
	 * Path to the folder with the test Data
	 */
	final String FOLDER_PATH = "src/main/resources/AllTestImages";

	public static final int METADATA_EXTRACTOR_LIBRARY = 1;
	public static final int APACHE_COMMONS_IMAGING_LIBRARY = 2;

	public ImageLibPerformanceTests() {
		FOLDER_PATH.replace("/", File.separator);
	}

	public long startTest(int library, int runs) {
		long startTime = System.currentTimeMillis();

		for(int n = 0; n<runs; n++ ) {
		runTest(library);
		}
		long stopTime = System.currentTimeMillis();
		long avgElapsedTime = (stopTime - startTime)/runs;

		return avgElapsedTime;
	}

	public void runTest(int library) {
		try {
			File folder = new File(FOLDER_PATH);

			if (!folder.isDirectory()) {
				System.err.println("The given path is no folder");
			}

			for (final File imgFile : folder.listFiles()) {
				System.out.println("----- New File: " + imgFile.getName() + "-----");
				System.out.println("");
				switch (library) {
				case METADATA_EXTRACTOR_LIBRARY:
					ImageLibExamples.metadataExtractorOutputMetadata(imgFile);
					break;
				case APACHE_COMMONS_IMAGING_LIBRARY:
					ImageLibExamples.apacheCommonsOutputMetadata(imgFile);
					break;
				}
				System.out.println("");
			}
		} catch (Exception e) {
			System.err.println("Error occured: " + e.getMessage());
		}
	}

	public void startApacheCommonImagingTest() {

	}
}
