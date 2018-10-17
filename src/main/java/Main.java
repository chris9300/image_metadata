
public class Main {

	public static void main(String[] args) {
		System.out.println("Run started...");
		System.out.println(System.getProperty("os.name"));
		ImageLibExamples imgLibExample = new ImageLibExamples();
		System.out.println("----------- Apache Commons Imaging -------------");
		imgLibExample.apacheCommonsImageExample();
		
		System.out.println("----------- Metadata Extractor -------------");
		imgLibExample.metadataExtractorExample();
	}

}
