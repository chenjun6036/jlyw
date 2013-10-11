package com.jlyw.util;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;

public class MergeFile {
	public static void main(String[] args) {
		
	} 
	/**
	 * PDFºÏ²¢
	 * @param files  
	 * @param newfile
	 * @return
	 */
	public static boolean mergePdfFiles(PdfReader[] files, FileOutputStream newfile) {
		boolean retValue = false;
		Document document = null;
		try {
			document = new Document(files[0].getPageSize(1));
			//PdfCopy copy = new PdfCopy(document, new FileOutputStream(newfile));
			PdfCopy copy = new PdfCopy(document, newfile);
			document.open();
			for (int i = 0; i < files.length; i++) {
				PdfReader reader = files[i];
				int n = reader.getNumberOfPages();
				for (int j = 1; j <= n; j++) {
					document.newPage();
					PdfImportedPage page = copy.getImportedPage(reader, j);
					copy.addPage(page);
				}
			}
			retValue = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			document.close();
		}
		return retValue;
	}
}
