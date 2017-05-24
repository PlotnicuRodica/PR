import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;


public class Run {
    public static void main(String[] args) throws Exception {
        Crawler deepCrawler = new Crawler();
        Scanner userInput = new Scanner(System.in);
        String userLink;

        //Insert first link
        System.out.print("Insert first link >> ");
        userLink = userInput.next();

        //Couple of threads
        CountDownLatch dependency = new CountDownLatch(2);

        new newThread(Optional.empty(), Optional.of(dependency), userLink, "HEAD").start();
        new newThread(Optional.empty(), Optional.of(dependency), userLink, "GET").start();
        new newThread(Optional.of(dependency), Optional.of(dependency), userLink, "POST").start();

        //Send link for crawling
        deepCrawler.goCrawl(userLink, 4, 2);
    }

    // HTTP GET request
    public static void sendGet(String url) throws Exception {
        final String USER_AGENT = "Chrome/57.0.2987.110";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();

        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader input = new BufferedReader(new InputStreamReader(con.getInputStream()));

        String inputLine;

        StringBuffer response = new StringBuffer();

        while ((inputLine = input.readLine()) != null) {
            response.append(inputLine);
        }
        input.close();

        //print result
        System.out.println(response.toString());

    }

    // HTTP HEAD request
    public static void sendHead(String url) throws Exception {
        final String USER_AGENT = "Chrome/57.0.2987.110";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("HEAD");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        int contentLength = con.getContentLength();
        String contentType = con.getContentType();
        long date = con.getDate();
        long lastModified = con.getLastModified();

        Date date2 = new Date(date);
        Date dateLastMod = new Date(lastModified);

        System.out.println("\nSending 'HEAD' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);
        System.out.println("Content Length: " + contentLength);
        System.out.println("Content Type : " + contentType);
        System.out.println("Date : " + date2);
        System.out.println("Last Modified : " + dateLastMod);
    }

    public static void sendPost(String query) throws Exception {
        final String USER_AGENT = "Chrome/57.0.2987.110";
        //Part of the query that is echoing what we insert as query
        String sendData = "e=" + query;

        URL obj = new URL("http://echo.itcuties.com");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //Method used
        con.setRequestMethod("POST");

        //Set request properties
        con.setDoOutput(true);
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        con.setRequestProperty("Content-Length", String.valueOf(sendData.length()));

        //Open a stream for writing data as bytes
        OutputStream outputStream = con.getOutputStream();
        outputStream.write(sendData.getBytes());

        //Get a response
        StringBuilder responseStream = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

        String response;
        while ((response = bufferedReader.readLine()) != null) {
            responseStream.append(response);
        }

        // Close both streams
        bufferedReader.close();
        outputStream.close();

        System.out.println(responseStream.toString());
    }
}
