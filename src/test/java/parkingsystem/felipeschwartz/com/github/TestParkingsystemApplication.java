package parkingsystem.felipeschwartz.com.github;

import org.springframework.boot.SpringApplication;

public class TestParkingsystemApplication {

	public static void main(String[] args) {
		SpringApplication.from(ParkingsystemApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
