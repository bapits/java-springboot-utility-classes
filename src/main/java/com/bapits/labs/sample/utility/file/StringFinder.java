package com.bapits.labs.sample.utility.file;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.bapits.labs.sample.utility.MyService;

public class StringFinder implements MyService {

  private static final Logger logger = LogManager.getLogger(StringFinder.class);

  @Override
  public void process() {
    logger.info("Process Started");
    this.findStringBetweenTwoStrings();
    // this.compareTwoLists();

    logger.info("Process Finished");
  }

  private void findStringBetweenTwoStrings() {

    // extract string between 2 strings e.g. extract test between Hello test world from following
    // example "This is Hello test world. This is test world"
    String sFileToProcess = "ABSOLUTE_PATH_TO_FILE";

    String sFirstString = "------- Found zip file : ";
    String sLastString = "has been deleted. -------";
    String sIntermediateStringToFind = "STRING_TO_FIND";

    // should not contain following strings between First and Last, if following strings are found
    // then the match is ignored
    List<String> sFileNamesNotIn = new ArrayList<>(
        Arrays.asList("STRING_1", "STRING_2", "STRING_3", "STRING_4", "STRING_5", "STRING_6"));


    logger.info("itemsNotFound before processing:{}", sFileNamesNotIn.size());

    // reference of file where the string was found
    List<String> sFileNamesList = new ArrayList<String>();

    // some result counts
    int enteredInIntermLoop = 0;
    int enteredInfirstStringFound = 0;
    int enteredInLastStringFound = 0;

    Set<String> duplicatedItems = null;
    try {

      boolean firstStringFound = false;
      String sFileNameLine = "";
      BufferedReader br = Files.newBufferedReader(Paths.get(sFileToProcess));

      String line;
      while ((line = br.readLine()) != null) {

        if (firstStringFound) {

          if (line.contains(sIntermediateStringToFind)) {
            enteredInIntermLoop++;
            sFileNamesList.add(sFileNameLine);
            firstStringFound = false;
            sFileNameLine = "";
          } else if (line.contains(sLastString)) {
            enteredInLastStringFound++;
            firstStringFound = false;
            sFileNameLine = "";
          }


        } else if (line.contains(sFirstString)) {
          enteredInfirstStringFound++;

          boolean bfound = false;
          String itemToRemove = "";
          for (String s : sFileNamesNotIn) {

            if (line.contains(s)) {
              bfound = true;
              itemToRemove = s;
              break; // Break out of the loop to skip the remaining items
            }
          }
          if (bfound) {
            sFileNamesNotIn.remove(itemToRemove);

          }
          if (!bfound) {
            firstStringFound = true;
            sFileNameLine = line;
          }

          // firstStringFound = true;
          // sFileNameLine = line;

        }
      }

      duplicatedItems = sFileNamesList.stream()
          .filter(i -> Collections.frequency(sFileNamesList, i) > 1).collect(Collectors.toSet());



    } catch (Exception e) {
      logger.error("Error:{}", e.getMessage(), e);

    }

    sFileNamesList.forEach(System.out::println);
    logger.info("items not found");
    sFileNamesNotIn.forEach(System.out::println);


    logger.info("duplicated Items:");
    duplicatedItems.forEach(System.out::println);
    logger.info(
        "sFileNameList:{}, itemsNotFound:{}, enteredInIntermLoop:{}, enteredInfirstStringFound:{}, enteredInLastStringFound:{}",
        sFileNamesList.size(), sFileNamesNotIn.size(), enteredInIntermLoop,
        enteredInfirstStringFound, enteredInLastStringFound);

  }

  private void compareTwoLists() {

    List<String> sFileNamesNotIn = new ArrayList<>(
        Arrays.asList("STRING_1", "STRING_2", "STRING_3", "STRING_4", "STRING_5", "STRING_6"));


    List<String> sFileitemList = new ArrayList<>(
        Arrays.asList("STRING_7", "STRING_8", "STRING_3", "STRING_9", "STRING_10", "STRING_1"));


    int foundItems = 0;

    // for(String s:sFileitemList) {
    for (String v : sFileNamesNotIn) {

      if (sFileitemList.contains(v)) {
        logger.info("Equal:{}", v);
        foundItems++;
      } else {
        logger.info("Not Equal:{}", v);
      }
    }

    logger.info("foundItems:{}", foundItems);

    /*
     * // or java streams List<String> listCommon = sFileitemList.stream() .filter(e ->
     * sFileNamesNotIn.contains(e)) .collect(Collectors.toList());
     * 
     * logger.info("foundItems:{}", listCommon);
     * 
     */

    // }


  }
}
