import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.commons.imaging.*;
import org.apache.commons.imaging.common.ImageMetadata;

public class ImageLibExamples {
	final String testFilePath = "src\\main\\resources\\test_img1.jpg";

	public void loadImage() {
		try {

			File imgFile = new File(testFilePath);
			BufferedImage img = ImageIO.read(imgFile);
			System.out.println("image file readed successfully");
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(img, "jpg", baos);
			byte[] imageBytes = baos.toByteArray();
			
			ImageMetadata metadata = Imaging.getMetadata(imgFile);
			
			if(metadata != null) {
			  System.out.println("Meta:" + metadata);
			} else {
				System.out.println("No Metadata available");
			}
			
		} catch (final Exception e) {
			System.out.println("Cannot read image file:" + e.getMessage());
		}
	}
}
