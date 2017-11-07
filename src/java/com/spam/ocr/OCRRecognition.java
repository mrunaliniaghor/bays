/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spam.ocr;

import com.itextpdf.text.pdf.PdfReader; //For this we use a library called iText 
//Add the iText library, by adding iText.jar to the lib folder of the project.
//Use the following code to extract the content
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import java.io.File;
import javax.imageio.ImageIO;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Girase
 */
public class OCRRecognition {

    public String scanDocument(String filename) {
        String content = "";
        filename = filename.toLowerCase();
        try {
            
            System.out.println("FileName Scan document:" + filename);
            if (filename.endsWith(".pdf")) { // For PDF files. . .
                content ="\n PDF Content :"+ getPDFContent(filename);
            }
            
            else if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".bmp") || filename.endsWith(".gif")) {    // For Image Files. . .
                content = getImageContent(filename);
            }
            
            else {        // Just text files. . .
                content = getTextFileContent(filename);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return content;
    }

    public String getPDFContent(String filename) {
        String content = "";
        try {
            PdfReader reader = new PdfReader(filename);
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                content = content + PdfTextExtractor.getTextFromPage(reader, i);
            }
            System.out.println("PdfContent :" + content.toString()); //toString is used to display the contents in variable content
        } catch (Exception e) {
            e.printStackTrace();
        }

        return content.toString();
    }

    public String getImageContent(String filename) {
        String content = "";
        File imageFile = new File(filename);
        Tesseract instance = Tesseract.getInstance(); // JNA Interface Mapping
        //instance.setDatapath("E:\\O_PROJECT\\Project\\Sem 7 project data\\other from Desktop\\Tess4J-1.1-src (1)\\Tess4J");
        // Tesseract1 instance = new Tesseract1(); // JNA Direct Mapping

        try {
            System.out.println("In OCR");
            System.out.println("Imagepath: "+filename);
            ImageIO.scanForPlugins();
            content = instance.doOCR(imageFile);
         //   System.out.println("OCR Content :"+content);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }

        return content;
    }

    public String getTextFileContent(String filename) {
        String content = "";
        try {
            content = FileUtils.readFileToString(new File(filename));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return content;
    }

    public static void main(String[] args) {
        System.out.println("Library Path :"+System.getProperty("java.library.path"));
        OCRRecognition ocrr = new OCRRecognition();
        //String content=ocrr.getImageContent("C:\\Users\\Girase\\Desktop\\spam-new.png");
        //System.out.println("Content :" + content);
        
    }
}