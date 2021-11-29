package com.mongo.migrate;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.mongo.migrate.pdf.PdfFilesController;


@SpringBootApplication
public class MongoPdfToFroApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(MongoPdfToFroApplication.class, args);
	}
	
	
	@Bean
    public TaskExecutor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1000);
        executor.setMaxPoolSize(2000);
        executor.setQueueCapacity(0);  // to mimic cachedThreadPool
        executor.initialize();
        return executor;
    }

}
