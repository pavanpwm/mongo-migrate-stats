package com.mongo.migrate.pdf;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PdfFilesRepo extends MongoRepository<PdfFiles, String>{

}
