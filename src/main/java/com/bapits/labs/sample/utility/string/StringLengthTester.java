package com.bapits.labs.sample.utility.string;

import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.bapits.labs.sample.utility.MyService;

public class StringLengthTester implements MyService {

  private static final Logger logger = LogManager.getLogger(StringLengthTester.class);

  @Override
  public void process() {
    logger.info("Process Started");
    
    // test if there is an item in list which causes teh substring to crash  
    
    List<String> fileNameList =
        Arrays.asList("TEST1_MYTEST_FILE.pdf", "TEST2_MYTEST_FILE.pdf", "TEST33_MYTEST_FILE.pdf",
            "TEST4_MYTEST_FILE.pdf", "TEST5_MYTEST_FILE.pdf", "TEST.pdf");


    try {
      for (String filName : fileNameList) {

        // remove file extension
        String sTest = filName.replaceFirst("[.][^.]+$", "");

        // file name 
        logger.info("sTest:{}", sTest);

        // test if substring works
        logger.info("sTest subStr:{}", sTest.substring(0, 5));
      }
    } catch (Exception e) {
      logger.error("Error:{}", e.getMessage(), e);

    }
    logger.info("Process Finished, processed:{} files", fileNameList.size());
  }

}
