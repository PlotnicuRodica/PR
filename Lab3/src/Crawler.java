import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Vector;


public class Crawler {

    //Crawl for links
    public void goCrawl(String myUrl, int howManyLinks, int treeDepth) throws Exception {
        final String USER_AGENT = "Chrome/57.0.2987.110";

        int i = 0, j = 0;
        int linkNr = 1, currentLink = 0, nonParsedSize = 0;

        String absLink = " ";
        int nrOfImages = 0;

        Vector<String> nonParsed = new Vector<>();
        Vector<String> lastLinks = new Vector<>();


        String oldPath;
        String filePath = "D:\\Univer\\PR\\Laboratoare\\lab 3\\Lab3PR\\Links" + URLEncoder.encode(myUrl, "UTF-8") + "\\";
        String fileFormat = "Link - " + URLEncoder.encode(myUrl, "UTF-8") + ".txt";

        //Initializing validator
        String[] schemes = {"http", "https"};
        org.apache.commons.validator.routines.UrlValidator urlValidator = new org.apache.commons.validator.routines.UrlValidator(schemes);

        while (treeDepth != 0) {
            if (nonParsedSize >= 0) {
                //==============================================================================================================================================
                //Creating directory to write links
                File directory = new File(filePath);
                directory.mkdirs();

                //Creating a new file in folder to keep links
                File file = new File(filePath, fileFormat);

                if (urlValidator.isValid(myUrl)) {

                    //Send GET request to first link
                    System.out.println("\n\t\t START LINK \t\t");

                    URL obj = new URL(myUrl);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                    // optional default is GET
                    con.setRequestMethod("GET");

                    //add request header
                    con.setRequestProperty("User-Agent", USER_AGENT);

                    int responseCode = con.getResponseCode();
                    System.out.println("Sending 'GET' request to URL : " + myUrl);
                    System.out.println("Response Code : " + responseCode);
                    System.out.println("");

                    //Verify status codes
                    if (responseCode >= 400 && responseCode < 500) {
                        System.out.println("Error at client side!");
                    } else {

                        //Parsing for other links
                        org.jsoup.nodes.Document doc = Jsoup.connect(myUrl).get();

                        //Selecting tags from HTML doc
                        Elements links = doc.select("a");
                        Elements images = doc.select("img");

                        //Count nr. of images per page
                        for (Element image : images) {
                            nrOfImages = nrOfImages + 1;
                        }
                        System.out.println("Nr. of images per page >> " + nrOfImages);

                        FileWriter fileWriter = new FileWriter(file);
                        //Print links
                        for (Element link : links) {
                            if (linkNr != howManyLinks + 1) {
                                linkNr = linkNr + 1;

                                //Parse html doc to get the value of href attribute
                                absLink = link.attr("abs:href");
                                nonParsed.add(absLink);
                                lastLinks.add(absLink);

                                //Set connection
                                URL obj2 = new URL(absLink);
                                HttpURLConnection con2 = (HttpURLConnection) obj2.openConnection();

                                // optional default is GET
                                con2.setRequestMethod("GET");

                                //add request header
                                con2.setRequestProperty("User-Agent", USER_AGENT);

                                int responseCode2 = con2.getResponseCode();

                                System.out.println(absLink);
                                fileWriter.write(absLink);
                                fileWriter.write(System.lineSeparator());
                                fileWriter.write("Response Code >> " + responseCode2);
                                fileWriter.write(System.lineSeparator());
                                fileWriter.write(System.lineSeparator());
                            } else {
                                break;
                            }

                        }
                        fileWriter.close();
                        linkNr = 0;
                    }
                } else {
                    System.out.println("Status >> Invalid");
                }

                oldPath = filePath;

                for (i = 0; i < nonParsed.size(); i++) {
                    myUrl = nonParsed.elementAt(i);

                    filePath = filePath + " " + URLEncoder.encode(myUrl, "UTF-8") + "\\";

                    nextLevelCrawl(myUrl, howManyLinks, filePath, fileFormat);

                    filePath = oldPath;
                }

                System.out.println("Switch to main!");

                nonParsed.clear();

                myUrl = lastLinks.elementAt(currentLink);

                filePath = oldPath + " " + URLEncoder.encode(myUrl, "UTF-8") + "\\" + URLEncoder.encode(myUrl, "UTF-8") + "\\";

                currentLink = currentLink + 1;

                nonParsedSize = lastLinks.size() - 1;

                treeDepth = treeDepth - 1;
            }
        }
    }

    //Crawl for links
    public void nextLevelCrawl(String myUrl, int howManyLinks, String newPath, String newFileFormat) throws Exception {
        final String USER_AGENT = "Chrome/57.0.2987.110";

        int i = 0, j = 0;
        int linkNr = 1;
        int nrOfImages = 0;

        String[] schemes = {"http", "https"};

        String absLink = " ";

        Vector<String> nonParsed2 = new Vector<>();

        String filePath2 = newPath;
        String fileFormat2 = newFileFormat;

        //Initializing validator
        org.apache.commons.validator.routines.UrlValidator urlValidator = new org.apache.commons.validator.routines.UrlValidator(schemes);

        //Creating directory to write links
        File directory = new File(filePath2);
        directory.mkdirs();

        //Creating a new file in folder to keep links
        File file = new File(filePath2, fileFormat2);

        if (urlValidator.isValid(myUrl)) {

            //Send GET request to first link
            System.out.println("\n\t\t START LINK \t\t");

            URL obj = new URL(myUrl);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            //add request header
            con.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode = con.getResponseCode();
            System.out.println("Sending 'GET' request to URL : " + myUrl);
            System.out.println("Response Code : " + responseCode);
            System.out.println("");

            //Verify status codes
            if (responseCode >= 400 && responseCode < 500) {
                System.out.println("Error at client side!");
            } else {

                //Parsing for other links
                org.jsoup.nodes.Document doc = Jsoup.connect(myUrl).get();

                //Selecting tags from HTML doc
                Elements links = doc.select("a");
                Elements images = doc.select("img");

                //Count nr. of images per page
                for (Element image : images) {
                    nrOfImages = nrOfImages + 1;
                }
                System.out.println("Nr. of images per page >> " + nrOfImages);

                FileWriter fileWriter = new FileWriter(file);
                //Print links
                for (Element link : links) {
                    if (linkNr != howManyLinks + 1) {
                        linkNr = linkNr + 1;

                        //Parse html doc to get the value of href attribute
                        absLink = link.attr("abs:href");
                        nonParsed2.add(absLink);

                        //Set connection
                        URL obj2 = new URL(absLink);
                        HttpURLConnection con2 = (HttpURLConnection) obj2.openConnection();

                        // optional default is GET
                        con2.setRequestMethod("GET");

                        //add request header
                        con2.setRequestProperty("User-Agent", USER_AGENT);

                        int responseCode2 = con2.getResponseCode();

                        System.out.println(absLink);
                        fileWriter.write(absLink);
                        fileWriter.write(System.lineSeparator());
                        fileWriter.write("Response Code >> " + responseCode2);
                        fileWriter.write(System.lineSeparator());
                        fileWriter.write(System.lineSeparator());
                    } else {
                        break;
                    }

                }
                fileWriter.close();
            }
        } else {
            System.out.println("Status >> Invalid");
        }
        nonParsed2.clear();
    }
}
