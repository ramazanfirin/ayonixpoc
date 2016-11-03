package guru.springframework;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class SpringBootAwtApplication {
	 public static void main(String[] args) throws InterruptedException {
//		 SpringApplication.run(SpringBootWebApplication.class, args);
		 
		 new SpringApplicationBuilder(SpringBootAwtApplication.class)
         		.headless(false)
         		//.web(false)
         		.run(args);   
		
		
	        
	    }
}
