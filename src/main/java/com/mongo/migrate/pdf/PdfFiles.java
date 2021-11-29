package com.mongo.migrate.pdf;

import java.time.Duration;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Document("files")
public class PdfFiles {
	
	@Id
	private String id;
	@JsonIgnore
	private Binary file;
	private String name;
	private double size;
	private Double uploadTime;
	private Double downloadTime;
	
	
	
	public PdfFiles() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PdfFiles(Binary file, String name, double size) {
		super();
		this.file = file;
		this.name = name;
		this.size = size;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Binary getFile() {
		return file;
	}
	public void setFile(Binary file) {
		this.file = file;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getSize() {
		return size;
	}
	public void setSize(double size) {
		this.size = size;
	}
	public Double getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(Duration uploadTime) {
		this.uploadTime = Double.parseDouble(uploadTime.toString().replace("PT", "").replace("S", ""));
	}
	
	public Double getDownloadTime() {
		return downloadTime;
	}
	public void setDownloadTime(Duration downloadTime) {
		this.downloadTime = Double.parseDouble(downloadTime.toString().replace("PT", "").replace("S", ""));
	}
	
	
	
	
	
}
