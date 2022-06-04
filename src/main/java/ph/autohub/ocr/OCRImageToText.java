/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package ph.autohub.ocr;

import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import javax.imageio.ImageIO; 
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author candaya
 */
public class OCRImageToText {

    public static void main(String[] args) throws Exception {
        
        
//        System.out.println("Hello World!");

while(true){
    

System.out.println("=====OCR Test=====");
System.out.println("Author: Clrkz");
System.out.println("Date: 06/04/2022");
System.out.println();

System.out.println("Files:");
File folder = new File("assets");
File[] listOfFiles = folder.listFiles();
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                System.out.println("" + listOfFile.getName());
            } else if (listOfFile.isDirectory()) {
                System.out.println("Directory: " + listOfFile.getName());
            }
        }
    
System.out.println();    
Scanner sc = new Scanner(System.in);
System.out.print("Enter filename:"); 
String filename = sc.next();
String inputFile = "assets\\"+filename;
String outputFile = "assets\\output.jpg";

System.out.println("Status: Reading the file...");

String ext = FilenameUtils.getExtension(inputFile); // returns "exe"
ext = ext.toLowerCase();
String[] imageExt = new String[]{"jpg", "jpeg", "png", "bmp", "gif"};

List<String> extList = new ArrayList<>(Arrays.asList(imageExt));
if(extList.contains(ext)/*file is image*/){
  
   try {
        BufferedImage originalImage = ImageIO.read(new File(inputFile)); 
System.out.println("Status: Scaling the file...");
        int width  = originalImage.getWidth();
        int height = originalImage.getHeight();
        
        BufferedImage outputImage = resizeImage(originalImage, width, height);
//        BufferedImage outputImage = getScaledImage(originalImage, width, height);
        ImageIO.write(outputImage, "jpg", new File(outputFile));
         } catch (IOException e) {
                System.err.println(e.getMessage());
         }
   
//        outputFile = inputFile;
}else{ 
        outputFile = inputFile;
 }
   

//image orientation
//hand wrtitten

System.out.println("Status: Converting file to text...");
    File imageFile = new File(outputFile);
            ITesseract instance = new Tesseract();  // JNA Interface Mapping
            // ITesseract instance = new Tesseract1(); // JNA Direct Mapping
            instance.setDatapath("tessdata"); // path to tessdata directory

            try {
                String result = instance.doOCR(imageFile);
                 
System.out.println();
System.out.println("Output:");
                System.out.println(result);
        
try (PrintWriter out = new PrintWriter("output.txt")) {
    out.println(result);
System.out.println("-Output save as output.txt");
}
            } catch (TesseractException e) {
                System.err.println(e.getMessage());
            }
            System.out.println();
            }
    }
   
   public static  BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
    Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
    BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
    outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
    return outputImage;
}
    
  public static BufferedImage getScaledImage(BufferedImage image, int width, int height) throws IOException {
    int imageWidth  = image.getWidth();
    int imageHeight = image.getHeight();

    double scaleX = (double)width/imageWidth;
    double scaleY = (double)height/imageHeight;
    AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
    AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);

    return bilinearScaleOp.filter(
        image,
        new BufferedImage(width, height, image.getType()));
}
}
