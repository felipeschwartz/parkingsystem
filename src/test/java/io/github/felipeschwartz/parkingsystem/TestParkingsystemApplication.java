package io.github.felipeschwartz.parkingsystem;

import org.springframework.boot.SpringApplication;

public class TestParkingsystemApplication {

	public static void main(String[] args) {
		SpringApplication.from(ParkingsystemApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
