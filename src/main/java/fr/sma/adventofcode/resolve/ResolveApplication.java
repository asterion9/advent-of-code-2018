package fr.sma.adventofcode.resolve;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


@SpringBootApplication
public class ResolveApplication implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(ResolveApplication.class, args);
	}
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Override
	public void run(String... args) throws Exception {
		String day = args[0];
		String ex = args[1];
		
		ExSolution exo = applicationContext.getBean("day" + day + "Ex" + ex, ExSolution.class);
		exo.run();
	}
}
