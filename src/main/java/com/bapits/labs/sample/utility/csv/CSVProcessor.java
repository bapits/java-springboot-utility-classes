package com.bapits.labs.sample.utility.csv;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.bapits.labs.sample.utility.FileResourcesUtils;
import com.bapits.labs.sample.utility.MyService;
import com.bapits.labs.sample.utility.csv.model.source.NameMap;
import com.bapits.labs.sample.utility.csv.model.target.ComId;
import com.bapits.labs.sample.utility.csv.model.target.Description;
import com.bapits.labs.sample.utility.csv.model.target.Id;
import com.bapits.labs.sample.utility.csv.model.target.Item;
import com.bapits.labs.sample.utility.csv.model.target.Name;
import com.bapits.labs.sample.utility.csv.model.target.NameDataMap;
import com.bapits.labs.sample.utility.csv.model.target.PutRequest;
import com.bapits.labs.sample.utility.csv.model.target.Reference;
import com.bapits.labs.sample.utility.csv.model.target.Root;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;


public class CSVProcessor implements MyService {

  private static final Logger logger = LogManager.getLogger(CSVProcessor.class);

  private static final String CSV_FILE_PATH = "csv/data_1.csv";
  private static final Character CSV_COLUMN_SEPEARATOR = ';';

  public void process() {
    logger.info("Process Started");

    try {
    // read file from resources
    File fileZip = FileResourcesUtils.getFileFromResource(CSV_FILE_PATH);

    // read all records from csv file
    CsvSchema schema = CsvSchema.emptySchema().withHeader()
        .withColumnSeparator(CSV_COLUMN_SEPEARATOR).withoutQuoteChar();

    ObjectReader objectReader = new CsvMapper().readerFor(NameMap.class).with(schema);


    MappingIterator<NameMap> userMappingIterator = objectReader.readValues(fileZip);
    List<NameMap> nameMaps = userMappingIterator.readAll();


    // map the items
    int index = 1;
    int iFileIndex = 1;
    List<NameDataMap> nameDataMapList = new ArrayList<>();
    for (NameMap nameMap: nameMaps) {
      Item item = new Item();

      Id id = new Id();
      id.setN(nameMap.getId().toString());
      item.setId(id);

      Description description = new Description();
      description.setS(nameMap.getDescription().replaceAll("\"", ""));
      item.setDescription(description);

      Name name = new Name();
      name.setS(nameMap.getName().replaceAll("\"", ""));
      item.setEntity(name);

      Reference ref = new Reference();
      ref.setS(nameMap.getReference().replaceAll("\"", ""));
      item.setReference(ref);

      ComId comId = new ComId();
      comId.setN(String.valueOf(nameMap.getComId()));
      item.setComId(comId);

      PutRequest putRequest = new PutRequest();

      putRequest.setItem(item);

      NameDataMap nameDataMap = new NameDataMap();
      nameDataMap.setPutRequest(putRequest);

      nameDataMapList.add(nameDataMap);

      if (index++ == 23) {// for each 24 create a new File
        Root root = new Root();

        root.setNameDataMap(nameDataMapList);
        writeToFile(root, iFileIndex++);
        index = 0;
        nameDataMapList.clear();

      }
    }

    Root root = new Root();

    root.setNameDataMap(nameDataMapList);

    writeToFile(root, iFileIndex++);

    } catch (Exception excp) {
      logger.error("Error Occurred:{}", excp.getMessage(), excp);
    }
    logger.info("Process Finished");
  }

  private void writeToFile(Root root, int fileIndex) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      objectMapper.writerWithDefaultPrettyPrinter()
          .writeValue(new File("build/target_name_map_items_" + fileIndex + ".json"), root);
    } catch (Exception e) {
      logger.error("error:{}", e.getMessage(), e);
    }
  }
}
