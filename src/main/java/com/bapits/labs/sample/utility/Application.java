package com.bapits.labs.sample.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import com.bapits.labs.sample.utility.auth.certif.CertificateAuthenticatorService;



@SpringBootApplication()
@EnableScheduling
public class Application implements CommandLineRunner {

  private static final Logger logger = LogManager.getLogger(Application.class);


  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Override
  public void run(String... args) throws Exception {

    try {
      logger.info("Application started");


      // MyService myService = new CSVProcessor();
      MyService myService = new CertificateAuthenticatorService();
      //MyService myService = new StringLengthTester();

      //MyService myService = new StringFinder();
      
      myService.process();

      logger.info("Application finished");
      
      
    } catch (Exception e) {
      logger.error("------- Error:{}-------", e.getMessage(), e);
    }
    
    System.exit(0);
    
  }
}
