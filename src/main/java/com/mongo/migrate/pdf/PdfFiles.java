package com.mongo.migrate.pdf;


import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document("files")
public class PdfFiles {
	
	@Id
	private String id;
	private Binary file;
	private String name;
	
	
	
	public PdfFiles() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PdfFiles(Binary file, String name) {
		super();
		this.file = file;
		this.name = name;
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
	
	
}
