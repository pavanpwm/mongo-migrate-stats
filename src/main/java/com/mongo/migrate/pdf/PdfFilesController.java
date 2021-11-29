package com.mongo.migrate.pdf;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
public class PdfFilesController {
	
	@Autowired
	PdfFilesService serv;
	
	String filesFolder = "D:\\Users\\pavan BTD\\Desktop\\pdf-clones\\";  // pdffiles //pdf-cloness
	String downloadsFolder = "D:\\Users\\pavan BTD\\Desktop\\pdfDownload\\";
	
	
	
	@GetMapping("/upmt")
	public void uploadFilesMT() throws IOException {
		serv.uploadMT(filesFolder);
	}
	
	
	@GetMapping("/downmt")
	public void downloadFilesMT() throws IOException {
		serv.downloadMT(downloadsFolder);
	}
	
	@GetMapping("/upst")
	public void uploadFilesST() throws IOException {
		serv.uploadST(filesFolder);
	}
	
	
	@GetMapping("/downst")
	public void downloadFilesST() throws IOException {
		serv.downloadST(downloadsFolder);
	}
	
	@GetMapping("/bulkupst")
	public void bulkUploadST() throws IOException {
		serv.bulkUploadST(filesFolder);
	}
	
	
	
	@GetMapping("/getStats")
	public List<PdfFiles> getStats() throws IOException {
		System.out.println("Below results are valid only for ST");
		double up = 0.0;
		double down = 0.0;
		List<PdfFiles> pdfList =  serv.getFileDetails();
		for (PdfFiles pdf : pdfList) {
			up += pdf.getUploadTime();
			down += pdf.getDownloadTime();
		}
		System.out.println("Total ST upload time for 1000 60kb files : " + up + ", " + "Total ST download time for 1000 60kb files : " + down);
		
		System.out.println("Below Results are valid only for MT");
		Comparator<PdfFiles> upCmp = Comparator.comparing(PdfFiles::getUploadTime);
		Comparator<PdfFiles> downCmp = Comparator.comparing(PdfFiles::getDownloadTime);
		System.out.println("Total MT upload time for 1000 60kb files : " + Collections.max(pdfList, upCmp).getUploadTime() + ", " + "Total MT download time for 1000 60kb files : " + Collections.max(pdfList, downCmp).getDownloadTime());

		return pdfList;
	}
	

	
	
	
	@GetMapping("/del")
	public String delAll() throws IOException {
		return serv.clearDbAndDownloadFolder(downloadsFolder);
	}
	
	
	
	
	

}
