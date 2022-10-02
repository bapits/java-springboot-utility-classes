package com.bapits.labs.sample.utility.auth.certif;

import static java.util.Map.entry;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.bapits.labs.sample.utility.FileResourcesUtils;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;

public class HttpClient {

  private static final Logger logger = LogManager.getLogger(HttpClient.class);

  private static OkHttpClient httpClient;

  private static final int CLIENT_TIMEOUT = 10;
  private static final int CLIENT_CONNECT_RETRY = 1;

  // test if TLS works with this url
  private static final String URL_TO_CALL = "https://test-url.com";


  private static final String CUSTOM_STORE_NAME = "CustomTruststore";

  private static final String CERT_STORE_PASS = "store-pass-word";

  // list of certificates to be added in truststore for TLS, pem file which contains the full chain
  // of certificates
  private final List<String> certificateList = Arrays.asList("cert/cert_to_load.pem");


  private final String MY_CERT_PRIVATE_KEY_FILE = "pk/my-cert-private-key.p12";

  private static final String TEMP_DIR = "tmp/";

  private static final boolean DISABLE_SSL = false;

  private static final boolean ADD_CERTIFICATES = true;

  private static final boolean USE_PROXY = true;
  private static final String PROXY_HOST = "my-corp-proxy";
  private static final int PROXY_PORT = 9999;
  private static final String PROXY_USER = "my-prox-user";
  private static final String PROXY_PASS = "my-proxy-pass";


  public HttpClient() {
    logger.info("Default value of javax.net.ssl.keyStore:{}",
        System.getProperty("javax.net.ssl.keyStore"));
    logger.info("Default value of javax.net.ssl.keyStorePassword:{}",
        System.getProperty("javax.net.ssl.keyStorePassword"));
    logger.info("Default value of javax.net.ssl.keyStoreType:{}",
        System.getProperty("javax.net.ssl.keyStoreType"));

    Builder httpBuilder =
        new OkHttpClient.Builder().connectTimeout(CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(CLIENT_TIMEOUT, TimeUnit.SECONDS);

    if (USE_PROXY) {
      logger.info("Proxy will be used to connect with client. Proxy: {}", PROXY_HOST);
      httpBuilder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_HOST, PROXY_PORT)))
          .proxyAuthenticator(new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
              // workaround to block the request, if authentication was not successful, otherwise it
              // will be stuck here in infinite loop, which will lead to the proxy-account
              // temporarily blocked by the proxy authentication server
              if (response != null && response.code() == 407
                  && response.message().equals("authenticationrequired")) {
                return null;
              }
              // it is Preemptive Authenticate, let it pass
              String credential = Credentials.basic(PROXY_USER, PROXY_PASS);
              return response.request().newBuilder().header("Proxy-Authorization", credential)
                  .build();
            }
          });
    }

    // locate the default truststore
    String jreKeyStorePath =
        System.getProperty("java.home") + "/lib/security/cacerts".replace('/', File.separatorChar);

    // new location to store modified keystore
    String newKeyStorePath = TEMP_DIR + CUSTOM_STORE_NAME;

    logger.info("jreKeyStorePath:{}", jreKeyStorePath);
    logger.info("certPath:{}", newKeyStorePath);


    try {
      logger.info("deleting cert Store certPath:{}", newKeyStorePath);
      boolean result = Files.deleteIfExists(Paths.get(newKeyStorePath));
      if (result) {
        logger.info("Existing File <{}> deleted.", newKeyStorePath);
      }

    } catch (IOException e) {
      logger.error("Error Occurred:{}", e.getMessage(), e);
    }


    if (ADD_CERTIFICATES) {

      logger.info("Adding Certificates");

      logger.info("javax.net.ssl.trustStore:{}", System.getProperty("javax.net.ssl.trustStore"));

      logger.debug("https.protocols:{}", System.getProperty("https.protocols"));

      try {

        // copy jre keystore to new location
        Files.copy(Paths.get(jreKeyStorePath), Paths.get(newKeyStorePath),
            StandardCopyOption.REPLACE_EXISTING);


        // add all certificates to new keystore
        CertificatesUtils.addCertificates(this.certificateList, newKeyStorePath, CERT_STORE_PASS);

        // now add private key to kyestore
        File pkcsFile = FileResourcesUtils.getFileFromResource(MY_CERT_PRIVATE_KEY_FILE);

        // add certificate private key to new keystore, new store must have already been created
        CertificatesUtils.importP12Certificate(pkcsFile.getAbsolutePath(), CERT_STORE_PASS,
            newKeyStorePath, CERT_STORE_PASS);

        // apply custom key manager, as the default key manager could not be able to find the
        // correct certificate set by client, which could cause handshake_failure
        CertificatesUtils.addCustomKeyManager(httpBuilder, newKeyStorePath, CERT_STORE_PASS);

      } catch (Exception e) {
        logger.error("Exception Occurred:{}", e.getMessage(), e);
      }

      System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,TLSv1.3");

      logger.debug("Local Trust Store Path, javax.net.ssl.trustStore:{}",
          System.getProperty("javax.net.ssl.trustStore"));
      logger.debug("Updated https.protocols:{}", System.getProperty("https.protocols"));

      logger.info("After update value of javax.net.ssl.keyStore:{}",
          System.getProperty("javax.net.ssl.keyStore"));
      logger.info("After update value of javax.net.ssl.keyStorePassword:{}",
          System.getProperty("javax.net.ssl.keyStorePassword"));
    }

    if (DISABLE_SSL) {
      logger.warn("Certificates will be ignored");
      CertificatesUtils.ignoreCertificates(httpBuilder);
    }

    logger.info("Activiating debug for: javax.net.debug=all");
    System.setProperty("javax.net.debug", "all");

    // logger.info("Activiating debug for: javax.net.debug=ssl,handshake");
    // System.setProperty("javax.net.debug", "ssl,handshake");



    this.httpClient = httpBuilder.build();

  }



  public void sendRequest() throws Exception {

    File fileToUpload = FileResourcesUtils.getFileFromResource("files/testFileToUploadForTest.zip");

    // multipart Request Body for client
    RequestBody body1 = new MultipartBody.Builder().setType(MultipartBody.FORM)
        .addFormDataPart("event", "Import Batch Data").addFormDataPart("fullload", "true")
        .addFormDataPart("content", fileToUpload.getName(),
            RequestBody.create(fileToUpload, MediaType.parse("application/octect-stream")))
        .addFormDataPart("clientinfo", "my-client").addFormDataPart("clienttype", "my-client-type")
        .addFormDataPart("clientversion", "1.0").build();


    // simple request with xml data
    String profileRequest = new BufferedReader(new InputStreamReader(
        new FileInputStream(FileResourcesUtils.getFileFromResource("files/testFileXmlData.xml")),
        StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
    RequestBody body =
        RequestBody.create(profileRequest, MediaType.parse("text/xml; charset=utf-8"));


    Request requestHttp = new Request.Builder().url(URL_TO_CALL)
        // .addHeader("Content-Type", "application/xml")
        // .addHeader("Content-Type", "multipart/attachment")
        // .addHeader("Accept", "gzip")
        .method("POST", body1).build();


    /*
     * // request with multipart to send invoice with TLS RequestBody bodyMultiRequest =
     * this.addMultiPartSampleRequest(); Request requestHttp = new
     * Request.Builder().url(URL_TO_CALL) .method("POST", bodyMultiRequest).build();
     */

    logger.info("Sending {} request to url:{} ", requestHttp.method(), URL_TO_CALL);
    logger.info("Content-Type:{} ", requestHttp.header("Content-Type"));



    Response response = this.postRequest(requestHttp);

    logger.info("Response Code:{}", response.code());

    logger.info("Response Header:{}", response.toString());

    logger.info("Response Body:{}", response.body().string());

  }

  private RequestBody addMultiPartSampleRequest() throws FileNotFoundException {

    String multipartHeader = new BufferedReader(new InputStreamReader(
        new FileInputStream(
            FileResourcesUtils.getFileFromResource("cert/test_request_multipart_header.xml")),
        StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));

    String multipartBody = new BufferedReader(new InputStreamReader(
        new FileInputStream(
            FileResourcesUtils.getFileFromResource("cert/test_request_multipart_body.xml")),
        StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));


    // creating real multipart request
    okhttp3.MultipartBody.Builder requestMultiPartBodyBuilder =
        new MultipartBody.Builder().setType(MultipartBody.FORM);

    requestMultiPartBodyBuilder.addPart(
        Headers.of(Map.ofEntries(entry("Content-Disposition", "attachment; filename=testFile"))),
        RequestBody.create(multipartHeader,
            MediaType.get("text/xml; charset=UTF-8; name=testFile")));

    requestMultiPartBodyBuilder.addPart(
        Headers.of(Map.ofEntries(entry("Content-Disposition", "attachment; filename=testFile"),
            entry("Content-ID", "<invXml>"))),
        RequestBody.create(multipartBody, MediaType.get("text/xml; charset=UTF-8; name=testFile")));

    requestMultiPartBodyBuilder.addPart(
        Headers.of(Map.ofEntries(entry("Content-Disposition", "attachment; filename=testFile"),
            entry("Content-ID", "<image>"), entry("Content-Transfer-Encoding", "base64"))),
        RequestBody.create("NO_PDF_TO_ATTACH",
            MediaType.get("application/pdf; name=testFile.pdf")));


    return requestMultiPartBodyBuilder.build();
  }

  private Response postRequest(final Request request) throws Exception {

    Response response = null;
    int retry = 0;
    while (retry++ != CLIENT_CONNECT_RETRY) {
      try {
        response = httpClient.newCall(request).execute();
        break;
      } catch (Exception excp) {
        logger.error("Call to Client failed.{}", excp.getMessage(), excp);
      }
    }

    // throw the exception if the call was not successful
    if (response == null) {
      String errorMsg = "postRequest response is null. Check logs for more details";
      logger.error(errorMsg);
      throw new IOException(errorMsg);
    }

    /*
     * if (!response.isSuccessful()) { String errorMsg = "postRequest Unexpected code " + response +
     * " , body =" + response.peekBody(Long.MAX_VALUE).string(); logger.error(errorMsg); throw new
     * IOException(errorMsg); }
     */

    return response;
  }



}
