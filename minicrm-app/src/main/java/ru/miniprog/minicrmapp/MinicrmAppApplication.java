package ru.miniprog.minicrmapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.modulith.Modulithic;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.modulith.docs.Documenter;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@Modulithic
public class MinicrmAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MinicrmAppApplication.class, args);
	}

}

@RestController
@RequestMapping("/sys")
class MinicrmAppApplicationController {

	private final ApplicationModules modules;

	public MinicrmAppApplicationController() {
		this.modules = ApplicationModules.of(MinicrmAppApplication.class);
	}

	@GetMapping("/gen-doc")
	public ResponseEntity<String> generateDocumentation() {
		try {
			new Documenter(modules).writeDocumentation();
			return ResponseEntity.ok("Documentation generated!");
		} catch (Exception e) {
			return ResponseEntity.internalServerError()
					.body("Generation failed: " + e.getMessage());
		}
	}

	@GetMapping("/moduleth")
	public String getMethodName() {
		final ApplicationModules modules = ApplicationModules.of(MinicrmAppApplication.class);
		return modules.toString();
	}

}
