package com.mongo.migrate.pdf;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;



@Service
public class PdfFilesService {

	@Autowired
	PdfFilesRepo repo;
	
	@Autowired
    private TaskExecutor executor;
	

	
	
	public List<PdfFiles> getFileDetails(){
		return repo.findAll();
	}

	public void uploadMT(String uploadFilesPath) throws IOException {
		List<Path> paths = Files.walk(Paths.get(uploadFilesPath)).collect(Collectors.toList());
		for (Path path : paths) {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						PdfFiles uPdf = new PdfFiles();
						uPdf.setName(path.getFileName().toString());
						uPdf.setSize(Files.size(path));
						Instant start = Instant.now(); // start timer from here
						uPdf.setFile(new Binary(Files.readAllBytes(path)));
						uPdf = repo.insert(uPdf);
						Instant end = Instant.now();
						Duration timeElapsed = Duration.between(start, end);
						uPdf.setUploadTime(timeElapsed);
						repo.save(uPdf);
					} catch (Exception e) {
					}
				}
			});
		}
		//Manually look if db has all the files stored and then move to downloading
		//or repo findall size but this will cost a db connections
	}

	public void downloadMT(String downloadPath) throws IOException {
		List<PdfFiles> pdfList = repo.findAll();
		for (PdfFiles pdf : pdfList) {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						// two ops: get the pdf by id into obj and save it to local
						Instant start = Instant.now();
						PdfFiles dPdf = repo.findById(pdf.getId()).get(); // I had to do this again coz, we will be retreiving wih given id
						Files.write(Paths.get(downloadPath + "/" + pdf.getName()), dPdf.getFile().getData());
						Instant end = Instant.now();
						Duration timeElapsed = Duration.between(start, end);
						pdf.setDownloadTime(timeElapsed);
						repo.save(pdf);
					} catch (Exception e) {
					}
				}
			});
		}
	}
	
	
	public void uploadST(String uploadFilesPath) throws IOException {
		List<Path> paths = Files.walk(Paths.get(uploadFilesPath)).collect(Collectors.toList());
		for (Path path : paths) {
			try {
				PdfFiles uPdf = new PdfFiles();
				uPdf.setName(path.getFileName().toString());
				uPdf.setSize(Files.size(path));
				Instant start = Instant.now(); // start timer from here
				uPdf.setFile(new Binary(Files.readAllBytes(path)));
				uPdf = repo.insert(uPdf);
				Instant end = Instant.now();
				Duration timeElapsed = Duration.between(start, end);
				uPdf.setUploadTime(timeElapsed);
				repo.save(uPdf);
			} catch (Exception e) {
			}
		}
	}
	
	public void downloadST(String downloadPath) throws IOException {
		List<PdfFiles> pdfList = repo.findAll();
		pdfList.forEach(pdf->{
			try {
				Instant start = Instant.now();
				PdfFiles dPdf = repo.findById(pdf.getId()).get(); // I had to do this again coz, we will be retreiving wih given id
				Files.write(Paths.get(downloadPath + "/" + pdf.getName()), dPdf.getFile().getData());
				Instant end = Instant.now();
				Duration timeElapsed = Duration.between(start, end);
				pdf.setDownloadTime(timeElapsed);
				repo.save(pdf);
			} catch (Exception e) {
			}
			
		});
	}
	
	
	public void bulkUploadST(String uploadFilesPath) throws IOException {
		List<Path> paths = Files.walk(Paths.get(uploadFilesPath)).collect(Collectors.toList());
		List<PdfFiles> pdfList = new ArrayList<PdfFiles>(); // first we will create a list of pdf objects and then upload it.
		Instant start = Instant.now(); // start timer from here
		for (Path path : paths) {
			try {
				PdfFiles uPdf = new PdfFiles();
				uPdf.setFile(new Binary(Files.readAllBytes(path)));
				pdfList.add(uPdf);
			} catch (Exception e) {
			}
		}
		repo.insert(pdfList);
		Instant end = Instant.now();
		Duration timeElapsed = Duration.between(start, end);
		System.out.println("Bulk upload time : " + timeElapsed);
	}
	
	
	// bulk download is of no use
	
	
	

	public String clearDbAndDownloadFolder(String downloadsFolder) throws IOException {
		repo.deleteAll();
		FileUtils.cleanDirectory(new File(downloadsFolder));
		return "Mongo Files : " + repo.count() + ", Local Downloaded Files : "
				+ new File(downloadsFolder).list().length;
	}

}
