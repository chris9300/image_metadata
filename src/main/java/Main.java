import LibraryTests.ImageLibExamples;
import LibraryTests.ImageLibPerformanceTests;

public class Main {

	static final int TEST_RUNS = 25;

	static final String RUN_EXAMPLES = "example";
	static final String RUN_PERFORMANCE_TEST = "performTest";

	static final String DEFAULT_EXAMPLE_FILE = "test_img1.jpg";

	public static void main(String[] args) {
		System.out.println("Run started...");

		if (0 < args.length) {
			switch (args[0]) {
			case RUN_EXAMPLES:
				String filename = DEFAULT_EXAMPLE_FILE;
				if (1 < args.length) {
					filename = args[1];
				}
				compareLibrariesWithExampleImage(filename);
				break;
			case RUN_PERFORMANCE_TEST:
				performanceTest();
				break;
			}
			System.out.println("finished");
		} else {
			System.out.println("No paramters submitted. Use:");
			System.out.println("example to get the metadata of an example file");
			System.out.println("performTest to run a test to compare the performance of the Metadata Extractor and the Apache Commons Imaging Library");
			
			System.out.println("---------- Default: Run Json Example -----------");
			ImageMetadataParser.writeExampleJson();
		}
	}

	private static void compareLibrariesWithExampleImage(String filename) {
		System.out.println("");
		System.out.println("========================================================");
		System.out.println("=============== Compare Library Results ================");
		System.out.println("========================================================");
		System.out.println("");

		ImageLibExamples imgLibExample = new ImageLibExamples(filename);

		System.out.println("----------- Apache Commons Imaging -------------");
		imgLibExample.apacheCommonsImageExample();
		System.out.println("");

		System.out.println("----------- Metadata Extractor -------------");
		imgLibExample.metadataExtractorExample();
	}

	private static void performanceTest() {
		System.out.println("");
		System.out.println("=================================================");
		System.out.println("=============== Performance Test ================");
		System.out.println("=================================================");
		System.out.println("");

		ImageLibPerformanceTests performTest = new ImageLibPerformanceTests();

		System.out.println("----------- Apache Commons Imaging -------------");
		long apacheCommonsImagingTime = performTest.startTest(ImageLibPerformanceTests.APACHE_COMMONS_IMAGING_LIBRARY,
				TEST_RUNS);
		System.out.println("");

		System.out.println("----------- Metadata Extractor -------------");
		long metadataExtractorTime = performTest.startTest(ImageLibPerformanceTests.METADATA_EXTRACTOR_LIBRARY,
				TEST_RUNS);
		System.out.println("");

		System.out.println("----------- Result after " + TEST_RUNS + " runs-------------");
		System.out.println("Metadata Extractor Library: " + metadataExtractorTime + "ms");
		System.out.println("Apache Commons Imaging Library: " + apacheCommonsImagingTime + "ms");

	}
}
