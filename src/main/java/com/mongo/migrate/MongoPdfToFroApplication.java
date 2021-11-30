package com.mongo.migrate;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@SpringBootApplication
public class MongoPdfToFroApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(MongoPdfToFroApplication.class, args);
	}
	
	
	@Bean
    public TaskExecutor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(500);
        executor.setMaxPoolSize(5000);
        executor.initialize();
        return executor;
    }
	
	

}
