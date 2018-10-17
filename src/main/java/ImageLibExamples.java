import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.commons.imaging.*;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.TiffConstants;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

public class ImageLibExamples {
	final String testFilePath = "src/main/resources/test_img1.jpg";

	public void apacheCommonsImageExample() {
		try {
			
				testFilePath.replace("/", File.separator);
				
			// 
			//File.separator

			File imgFile = new File(testFilePath);
			BufferedImage img = ImageIO.read(imgFile);
			System.out.println("image file readed successfully");

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(img, "jpg", baos);
			byte[] imageBytes = baos.toByteArray();

			ImageMetadata metadata = Imaging.getMetadata(imgFile);

			if (metadata != null) {
				if (metadata instanceof JpegImageMetadata) {
					JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
					System.out.println("----------- JPEG Metadata -------");
					printTagValue(jpegMetadata, TiffConstants.TIFF_TAG_DATE_TIME);
					printTagValue(jpegMetadata, ExifTagConstants.EXIF_TAG_EXIF_IMAGE_WIDTH);
					printTagValue(jpegMetadata, TiffConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
				}

				System.out.println("Meta:" + metadata);
			} else {
				System.out.println("No Metadata available");
			}

		} catch (final Exception e) {
			System.out.println("Cannot read image file: " + e.getMessage());
		}
	}
	

	private static void printTagValue(final JpegImageMetadata jpegMetadata, final TagInfo tagInfo) {
		final TiffField field = jpegMetadata.findEXIFValueWithExactMatch(tagInfo);
		if (field == null) {
			System.out.println(tagInfo.name + ": " + "Not Found.");
		} else {
			System.out.println(tagInfo.name + ": " + field.getValueDescription());
		}
	}
	
	public void metadataExtractorExample() {
		File imgFile = new File(testFilePath);
		try {
		Metadata metadata = ImageMetadataReader.readMetadata(imgFile);
		
		for (Directory directory : metadata.getDirectories()) {
		    for (Tag tag : directory.getTags()) {
		        System.out.format("[%s] - %s = %s",
		            directory.getName(), tag.getTagName(), tag.getDescription());
		        System.out.println("");
		    }
		    if (directory.hasErrors()) {
		        for (String error : directory.getErrors()) {
		            System.err.format("ERROR: %s", error);
		        }
		    }
		}
		
		} catch(Exception e) {
			System.out.println("Error with Metadata Extractor: " + e.getMessage());
		}
	}
}
