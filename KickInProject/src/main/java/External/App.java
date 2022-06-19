package External;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.poi.xwpf.usermodel.XWPFDocument;


import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;

public class App {

	public void wordToPdf(String title) {
		  //String docPath = "D:/Documents/doegroepouders.docx";
		  //String pdfPath = "D:/Documents/doegroepouders.pdf";
		  
		  URL url = getClass().getClassLoader().getResource("documents/");
		  
		  String docxPath = url.toString() + title + ".docx";
		  String pdfPath = url.toString() + title + ".pdf";

		  URL urlInput = getClass().getClassLoader().getResource("documents/" + title + ".docx");
		  
		  docxPath = docxPath.replace("file:/", "");
		  pdfPath = pdfPath.replace("file:/", "");
		  
		  try {
			  System.out.println(docxPath);
			  System.out.println(pdfPath);
			  
			  InputStream in = new FileInputStream(new File(docxPath));
			  XWPFDocument document = new XWPFDocument(in);
			  PdfOptions options = PdfOptions.create();
			  
			  OutputStream out = new FileOutputStream(new File(pdfPath));
			  
			  PdfConverter.getInstance().convert(document, out, options);
				
				document.close();
				out.close();
			
			} catch (Exception e) {
				e.printStackTrace();
			}
			  
			System.out.println("Done!");
	}
	
	/**
	public  String parseToStringExample() throws IOException, TikaException, URISyntaxException {
		URL url = getClass().getClassLoader().getResource("documents/");
		String docxPath = url.toString() + "doegroepouders.docx";
		URL urlInput = getClass().getClassLoader().getResource("documents/doegroepouders.docx");
		
		URI uri = urlInput.toURI();
		
		docxPath = docxPath.replace("file:/", "");
		
    	Tika tika = new Tika();
    	try (InputStream in = new FileInputStream(new File(uri));) {
       		return tika.parseToString(in);
    }
}
	**/
	
 public static void main(String[] args) throws Exception {
 }
}