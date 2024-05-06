package com.example.wallpapers;

import com.example.wallpapers.service.HighResolutionStorage;
import com.example.wallpapers.service.LowResolutionStorage;
import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WallpapersApplication implements CommandLineRunner {
	@Resource
	HighResolutionStorage highResolutionStorage;
	@Resource
	LowResolutionStorage lowResolutionStorage;

	public static void main(String[] args) {
		SpringApplication.run(WallpapersApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		highResolutionStorage.init();
		lowResolutionStorage.init();
	}
}
