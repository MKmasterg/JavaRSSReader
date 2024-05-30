public class Data {
    private String blogName;
    private String blogURL;
    private String blogRSS;

    public String getBlogName(){
        return blogName;
    }
    public String getBlogURL(){
        return blogURL;
    }
    public String getBlogRSS(){
        return blogRSS;
    }
    public Data(String[] formattedLine){
        blogName = formattedLine[0];
        blogURL = formattedLine[1];
        blogRSS = formattedLine[2];
    }
}
