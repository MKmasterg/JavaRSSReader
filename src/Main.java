import org.jsoup.Jsoup;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

public class Main {
    public static final int MAX_URL_COUNT = 100;
    public static final int MAX_CONTENT_FETCH = 5;
    public static final String DATA_SEPARATOR = ";";
    public static Data[] dataArrays = new Data[MAX_URL_COUNT];
    public static int dataArraysIndex = 0;
    public static void main(String[] args){
        System.out.println("Welcome to RSS Reader!");
        Menu menuObject = new Menu();
        File dataTXT = new File("data.txt");
        if(dataTXT.exists()){
            //Reading data from data.txt if exists
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(dataTXT));
                String line;
                while((line = bufferedReader.readLine()) != null){
                    String[] formattedLine = line.split(DATA_SEPARATOR);
                    dataArrays[dataArraysIndex++] = new Data(formattedLine);
                }
                bufferedReader.close();
            } catch (IOException ex){
                System.out.println("Something happened in reading file!");
                ex.printStackTrace();
            }
        }

        do{
            //Main loop
            menuObject.showMenu();
            menuObject.getUserChoice();
        } while(!menuObject.getExitSwitch());

        //Writing the data array into file after user selected exit
        if(dataTXT.exists())
            dataTXT.delete();
        if(dataArraysIndex == 0)
            //If there are no data then just exit the app
            return;
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("data.txt", true));
            for (int i = 0; i < dataArraysIndex; i++) {
                String payload = dataArrays[i].getBlogName() + DATA_SEPARATOR + dataArrays[i].getBlogURL() + DATA_SEPARATOR + dataArrays[i].getBlogRSS() + "\n";
                bufferedWriter.write(payload);
            }
            bufferedWriter.close();
        } catch (IOException ex) {
            System.out.println("Something happened in writing file!");
            ex.printStackTrace();
        }
    }


    public static void removeItemFromDataArrays(int targetIndex){
        //Simple removing method element from dataArrays by getting an index
        for(int i = targetIndex; i < dataArraysIndex; i++){
            if(i != dataArraysIndex - 1){
                dataArrays[i] = dataArrays[i+1];
            } else {
                dataArrays[i] = null;
                dataArraysIndex--;
                break;
            }
        }
    }
    public static String fetchPageSource(String urlString) throws Exception {
        //This method gets HTML from url and return it as a string
        URI uri = new URI(urlString);
        URL url = uri.toURL();
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML , like Gecko) Chrome/108.0.0.0 Safari/537.36");
        //The codes used below source: https://www.tutorialspoint.com/java/urlconnection_getinputstream.htm
        BufferedReader bufferedReaderFromInputStream = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String content = "";
        String current;
        while((current = bufferedReaderFromInputStream.readLine()) != null){
            content += current;
        }
        bufferedReaderFromInputStream.close();
        return content;
    }
    public static String getPageTitleFromHTML(String HTML) throws Exception{
        //This method retrieve page title by getting an HTML string
        try{
            org.jsoup.nodes.Document doc = Jsoup.parse(HTML);
            return doc.select("title").first().text();
        } catch(Exception e){
            return "Error: no title tag found in page source!";
        }
    }
    public static String getRSSFromURL(String URL) throws IOException{
        //This method tries to find and retrieve a specific rss link pattern from the entire HTML
        org.jsoup.nodes.Document doc = Jsoup.connect(URL).get();
        return doc.select("[type='application/rss+xml']").attr("abs:href");
    }

    public static void getLatestUpdateFromRSS(String RSS){
        //This method prints out the latest feeds from the given RSS link
        try {
            String rssXml = fetchPageSource(RSS);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            StringBuilder xmlStringBuilder = new StringBuilder();
            xmlStringBuilder.append(rssXml);
            ByteArrayInputStream input = new ByteArrayInputStream(
                    xmlStringBuilder.toString().getBytes("UTF-8"));
            Document doc = documentBuilder.parse(input);
            NodeList itemNodes = doc.getElementsByTagName("item");
            for (int i = 0; i < MAX_CONTENT_FETCH; ++i) {
                Node itemNode = itemNodes.item(i);
                if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) itemNode;
                    System.out.println("Title: " + element.getElementsByTagName("title").item(0).getTextContent());
                    System.out.println("Link: " + element.getElementsByTagName("link").item(0).getTextContent());
                    System.out.println("Description: " + element.getElementsByTagName("description").item(0).getTextContent());
                }
            }
        } catch (Exception e){
            System.out.println("Error in retrieving RSS content for " + RSS + ": " + e.getMessage());
        }
    }
}
