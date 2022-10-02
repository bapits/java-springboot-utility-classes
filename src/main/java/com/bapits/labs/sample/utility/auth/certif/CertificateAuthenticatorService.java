package com.bapits.labs.sample.utility.auth.certif;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.bapits.labs.sample.utility.MyService;

public class CertificateAuthenticatorService implements MyService {
  private static final Logger logger = LogManager.getLogger(CertificateAuthenticatorService.class);

  public void process() {
    logger.info("started processing");

    try {

      new HttpClient().sendRequest();
      
    } catch (Exception e) {
      logger.error("error:{}", e.getMessage(), e);

    }
    logger.info("finished processing");
  }
}
