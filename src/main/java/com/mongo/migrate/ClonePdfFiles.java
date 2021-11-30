package com.mongo.migrate;

import java.io.File;
import java.io.IOException;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.util.FileCopyUtils;

public class ClonePdfFiles {
	
	public static final File sixtyKBFile = new File("D:\\Users\\pavan BTD\\Desktop\\pdffiles\\0.06 MB.pdf");
	public static final File fiveHunderedKBFile = new File("D:\\Users\\pavan BTD\\Desktop\\pdffiles\\0.5 MB.pdf");
	public static final File oneMBFile = new File("D:\\Users\\pavan BTD\\Desktop\\pdffiles\\1 MB.pdf");
	public static final File twoPointFiveMBFile = new File("D:\\Users\\pavan BTD\\Desktop\\pdffiles\\2.5 MB.pdf");
	public static final File fiveMBFile = new File("D:\\Users\\pavan BTD\\Desktop\\pdffiles\\5MB.pdf");
	public static final File tenMBFile = new File("D:\\Users\\pavan BTD\\Desktop\\pdffiles\\10MB.pdf");
	
	
	public static void generatePdfs(File sourceFile, int cloneCount) throws IOException {
		FileUtils.cleanDirectory(new File("D:\\Users\\pavan BTD\\Desktop\\pdf-clones\\"));
		for (int i = 1; i <= cloneCount; i++) {
			FileCopyUtils.copy(sourceFile, new File("D:\\Users\\pavan BTD\\Desktop\\pdf-clones\\pdfClone" + i +".pdf"));
		}
	}

}
