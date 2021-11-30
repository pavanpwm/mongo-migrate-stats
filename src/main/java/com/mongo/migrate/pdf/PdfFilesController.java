package com.mongo.migrate.pdf;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.mongo.migrate.ClonePdfFiles;

@CrossOrigin("*")
@RestController
public class PdfFilesController {
	
	@Autowired
	PdfFilesService serv;
	
	String filesFolder = "D:\\Users\\pavan BTD\\Desktop\\pdf-clones\\";  // pdffiles //pdf-cloness
	String downloadsFolder = "D:\\Users\\pavan BTD\\Desktop\\pdfDownload\\";
	
	
	
	@GetMapping("/getst/{fileSize}")
	public void getStats(@PathVariable("fileSize") int fileSize) throws Exception{
		delAll();
		serv.getStats(fileSize, filesFolder, downloadsFolder);
	}
	
	
	@GetMapping("/del")
	public String delAll() throws IOException {
		return serv.clearDbAndDownloadFolder(downloadsFolder);
	}
	
	
	@GetMapping("/getSingle")
	public void getStatForSinle() throws IOException {
		serv.getStatsForSigle(filesFolder, downloadsFolder);
	}
	
	
	

	
	
//	@GetMapping("/getStats")
//	public List<PdfFiles> getStats() throws IOException {
//		System.out.println("Below results are valid only for ST");
//		double up = 0.0;
//		double down = 0.0;
//		List<PdfFiles> pdfList =  serv.getFileDetails();
//		for (PdfFiles pdf : pdfList) {
//			up += pdf.getUploadTime();
//			down += pdf.getDownloadTime();
//		}
//		System.out.println("Total ST upload time for 1000 60kb files : " + up + ", " + "Total ST download time for 1000 60kb files : " + down);
//		
//		System.out.println("Below Results are valid only for MT");
//		Comparator<PdfFiles> upCmp = Comparator.comparing(PdfFiles::getUploadTime);
//		Comparator<PdfFiles> downCmp = Comparator.comparing(PdfFiles::getDownloadTime);
//		System.out.println("Total MT upload time for 1000 60kb files : " + Collections.max(pdfList, upCmp).getUploadTime() + ", " + "Total MT download time for 1000 60kb files : " + Collections.max(pdfList, downCmp).getDownloadTime());
//
//		return pdfList;
//	}
//	
	
	

}
