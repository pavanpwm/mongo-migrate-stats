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
import java.util.stream.Collectors;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.mongo.migrate.ClonePdfFiles;

@Service
public class PdfFilesService {

	@Autowired
	PdfFilesRepo repo;

	@Autowired
	private TaskExecutor executor;
	
	ArrayList<String> results = new ArrayList<String>();

	public void getStats(int fileSize, String filesFolder, String downloadsFolder) throws Exception {
		File pdfFile = null;
		int sampleSize = 0;
		for (int i = 0; i < 6; i++) {
			if (i == 0) {
				sampleSize = 500;
			} else if (i == 1) {
				sampleSize = 1000;
			} else if (i == 2) {
				sampleSize = 2000;
			}else if (i == 3) {
				sampleSize = 3000;
			}else if (i == 4) {
				sampleSize = 4000;
			}else if (i == 5) {
				sampleSize = 5000;
			}
			if (fileSize == 60) {
				pdfFile = ClonePdfFiles.sixtyKBFile;
			} else if (fileSize == 500) {
				pdfFile = ClonePdfFiles.fiveHunderedKBFile;
			} else if (fileSize == 1000) {
				pdfFile = ClonePdfFiles.oneMBFile;
			}
			//ST here
			ClonePdfFiles.generatePdfs(pdfFile, sampleSize); // generate
			Instant upStart = Instant.now();
			uploadST(filesFolder); // upload
			Instant upEnd = Instant.now();
			results.add("Single Threaded Upload time for " + sampleSize + " files " + fileSize + "KB : "
					+ Duration.between(upStart, upEnd));
			results.forEach(res->{
				System.out.println(res);
			});
			Instant downStart = Instant.now();
			downloadST(downloadsFolder); // download
			Instant downEnd = Instant.now();
			results.add("Single Threaded Download time for " + sampleSize + " files " + fileSize + "KB : "
					+ Duration.between(downStart, downEnd));
			clearDbAndDownloadFolder(downloadsFolder);
			//Bulk Here
			Instant start = Instant.now(); // start timer from here
			bulkUploadST(filesFolder);
			Instant end = Instant.now();
			results.add("Single Threaded BULK INSERT time for " + sampleSize + " files " + fileSize + "KB : "
					+ Duration.between(start, end));
			results.forEach(res->{
				System.out.println(res);
			});
			clearDbAndDownloadFolder(downloadsFolder);
			//MT here
			Instant ini = Instant.now();
			uploadMT(filesFolder, sampleSize);
			while (sampleSize > repo.count()) {
			}
			Instant fini = Instant.now();
			results.add("Multi Threaded Upload time for " + sampleSize + " files " + fileSize + "KB : "
					+ Duration.between(ini, fini));
			results.forEach(res->{
				System.out.println(res);
			});
			ini = Instant.now();
			downloadMT(downloadsFolder, sampleSize);
			while (sampleSize > new File(downloadsFolder).list().length) {
			}
			fini = Instant.now();
			results.add("Multi Threaded Download time for " + sampleSize + " files " + fileSize + "KB : "
					+ Duration.between(ini, fini));
			results.forEach(res->{
				System.out.println(res);
			});
			
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	public List<PdfFiles> getAllPdfsFromDB() {
		return repo.findAll();
	}

	public void uploadMT(String uploadFilesPath, int sampleSize) throws IOException {
		List<Path> paths = Files.walk(Paths.get(uploadFilesPath)).collect(Collectors.toList());
		for (Path path : paths) {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						PdfFiles uPdf = new PdfFiles();
						uPdf.setName(path.getFileName().toString());
						uPdf.setFile(new Binary(Files.readAllBytes(path)));
						uPdf = repo.insert(uPdf);
					} catch (Exception e) {
					}
				}
			});
		}
	}

	public void downloadMT(String downloadPath, int sampleSize) throws Exception {
		List<PdfFiles> pdfList = repo.findAll();
		for (PdfFiles pdf : pdfList) {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						PdfFiles dPdf = repo.findById(pdf.getId()).get(); // I had to do this again coz, we will be
																			// retreiving wih given id
						Files.write(Paths.get(downloadPath + "/" + pdf.getName()), dPdf.getFile().getData());
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
				uPdf.setFile(new Binary(Files.readAllBytes(path)));
				uPdf = repo.insert(uPdf);
			} catch (Exception e) {
			}
		}
	}

	public void downloadST(String downloadPath) throws IOException {
		List<PdfFiles> pdfList = repo.findAll();
		pdfList.forEach(pdf -> {
			try {
				PdfFiles dPdf = repo.findById(pdf.getId()).get();
				Files.write(Paths.get(downloadPath + "/" + pdf.getName()), dPdf.getFile().getData());
			} catch (Exception e) {
			}
		});
	}

	public void bulkUploadST(String uploadFilesPath) throws IOException {
		List<Path> paths = Files.walk(Paths.get(uploadFilesPath)).collect(Collectors.toList());
		List<PdfFiles> pdfList = new ArrayList<PdfFiles>(); // first we will create a list of pdf objects and then
															// upload it.
		for (Path path : paths) {
			try {
				PdfFiles uPdf = new PdfFiles();
				uPdf.setFile(new Binary(Files.readAllBytes(path)));
				pdfList.add(uPdf);
			} catch (Exception e) {
			}
		}
		repo.insert(pdfList);
	}

	// bulk download is of no use

	public String clearDbAndDownloadFolder(String downloadsFolder) throws IOException {
		repo.deleteAll();
		FileUtils.cleanDirectory(new File(downloadsFolder));
		return "Mongo Files : " + repo.count() + ", Local Downloaded Files : "
				+ new File(downloadsFolder).list().length;
	}

	public void getStatsForSigle(String uploadFilesPath, String downloadPath) throws IOException {
		clearDbAndDownloadFolder(downloadPath);
		Instant start = Instant.now();
		PdfFiles uPdf = new PdfFiles();
		uPdf.setFile(
				new Binary(Files.readAllBytes(Paths.get("D:\\Users\\pavan BTD\\Desktop\\pdf-clones\\pdfClone1.pdf"))));
		uPdf.setName("singleTestFile.pdf");
		uPdf = repo.insert(uPdf);
		Instant end = Instant.now();
		System.out.println("Upload time for single file : " + Duration.between(start, end));
		start = Instant.now();
		PdfFiles dPdf = repo.findById(uPdf.getId()).get();
		Files.write(Paths.get(downloadPath + "/" + dPdf.getName()), dPdf.getFile().getData());
		end = Instant.now();
		System.out.println("Download time for single file : " + Duration.between(start, end));
	}

}
