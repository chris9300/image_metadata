
public class Main {
	
	static final int TEST_RUNS = 25;

	public static void main(String[] args) {
		System.out.println("Run started...");
		performanceTest();
		
	}
	
	private static void compareLibrariesWithExampleImage() {	
		System.out.println("");
		System.out.println("=================================================");
		System.out.println("=============== Performance Test ================");
		System.out.println("=================================================");
		System.out.println("");
		
		ImageLibExamples imgLibExample = new ImageLibExamples();
		
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
		long apacheCommonsImagingTime = performTest.startTest(ImageLibPerformanceTests.APACHE_COMMONS_IMAGING_LIBRARY, TEST_RUNS);
		System.out.println("");
		
		System.out.println("----------- Metadata Extractor -------------");
		long metadataExtractorTime = performTest.startTest(ImageLibPerformanceTests.METADATA_EXTRACTOR_LIBRARY, TEST_RUNS);
		System.out.println("");
		
		
		
		System.out.println("----------- Result after " + TEST_RUNS + " runs-------------");
		System.out.println("Metadata Extractor Library: " + metadataExtractorTime + "ms");
		System.out.println("Apache Commons Imaging Library: " + apacheCommonsImagingTime + "ms");
		
		
	}
}
