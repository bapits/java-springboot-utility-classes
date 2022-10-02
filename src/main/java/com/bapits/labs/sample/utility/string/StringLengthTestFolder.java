package com.bapits.labs.sample.utility.string;

import java.io.File;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.bapits.labs.sample.utility.MyService;

public class StringLengthTestFolder implements MyService {

  private static final Logger logger = LogManager.getLogger(StringLengthTestFolder.class);

  @Override
  public void process() {

    logger.info("Process Started");

    // identify the file with special character in name present inside a directory in linux

    String sPath = "LINUX_ABSOLUTE_PATH_TO_FILE";

    try {
      logger.info("Reading files from:{}", sPath);
      File folder = new File(sPath);
      File[] directorFiles = folder.listFiles();

      logger.info("Total files found:{}", directorFiles.length);
      assert directorFiles != null;
      for (File pdfFile : directorFiles) {
        logger.info("processing file:{}", pdfFile.getAbsolutePath().toLowerCase());
        if (FilenameUtils.getExtension(pdfFile.getAbsolutePath().toLowerCase())
            .equalsIgnoreCase("pdf")) {

          String name = pdfFile.getName();

          logger.info("processing file with name:{}", name);

          // get file name without extension
          String sTest = name.replaceFirst("[.][^.]+$", "");

          logger.info("sTest:{}", sTest);

          logger.info("sTest subStr:{}", sTest.substring(0, 5));

        }
      }

    } catch (Exception e) {
      logger.error("Error:{}", e.getMessage(), e);

    }
    logger.info("Process Finished, processed:{} files in dir", sPath);
  }

}
