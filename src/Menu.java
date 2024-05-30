import java.io.BufferedReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

public class Menu{
    private boolean exitSwitch = false;
    public void showMenu() {
        System.out.println("Type a valid number for your desired action:");
        System.out.println("[1] Show updates");
        System.out.println("[2] Add URL");
        System.out.println("[3] Remove URL");
        System.out.println("[4] Exit");
    }

    public boolean getExitSwitch(){
        return exitSwitch;
    }
    public void getUserChoice(){
        //This method gets the user choice for main menu operations
        Scanner inputHandler = new Scanner(System.in);
        int userChoice;
        //Some validation below
        if (!inputHandler.hasNextInt()){
            System.out.println("Invalid input!");
        } else {
            userChoice = inputHandler.nextInt();
            //Clearing one line input buffer
            inputHandler.nextLine();
            if(userChoice > 4 || userChoice < 1){
                System.out.println("Invalid input!");
                return;
            }
            switch (userChoice){
                //Main switch and its functions
                case 4:
                    exitSwitch = true;
                    break;
                case 1:
                    showUpdates();
                    break;
                case 2:
                    addURL();
                    break;
                case 3:
                    deleteURL();
                    break;
            }
        }
    }

    public void showUpdates(){
        //This method prints the controlled layout of user-selected RSS feed contents
        if (Main.dataArraysIndex == 0){
            System.out.println("There are no websites to get updates from.");
        } else {
            System.out.println("Show updates for :");
            //Prints out the possible choices for user
            for(int i = 0; i < Main.dataArraysIndex; i++){
                System.out.println("["+i+"] "+Main.dataArrays[i].getBlogName());
            }
            System.out.println("Enter -1 to show all.\nEnter -2 to return.");
            Scanner inputHandler = new Scanner(System.in);
            //Some input handling here
            if(!inputHandler.hasNextInt()){
                System.out.println("Invalid input!");
            } else {
                int userChoice = inputHandler.nextInt();
                if(userChoice >= Main.dataArraysIndex || (userChoice < 0 && userChoice != -1 && userChoice != -2)){
                    System.out.println("Invalid input!");
                    return;
                }
                if(userChoice == -1){
                    //Prints out the RSS feed contents from the whole list
                    for(int i = 0; i < Main.dataArraysIndex ; i++){
                        System.out.println(".:: " + Main.dataArrays[i].getBlogName() + " ::.");
                        Main.getLatestUpdateFromRSS(Main.dataArrays[i].getBlogRSS());
                        System.out.println("<----------------->");
                    }
                } else if(userChoice == -2){
                    //Returns to menu without doing anything
                    return;
                } else {
                    //Prints out only the contents of selected RSS
                    Main.getLatestUpdateFromRSS(Main.dataArrays[userChoice].getBlogRSS());
                }
            }
        }
    }

    public static void deleteURL(){
        System.out.println("Please enter website URL to remove:");
        Scanner inputHandler = new Scanner(System.in);
        String tempURL = inputHandler.next();
        //Clearing one line input buffer
        inputHandler.nextLine();
        //Searches through the array
        for(int i = 0; i < Main.dataArraysIndex; i++){
            if(tempURL.compareTo(Main.dataArrays[i].getBlogURL()) == 0){
                Main.removeItemFromDataArrays(i);
                System.out.println("Removed "+tempURL+" successfully!");
                return;
            }
        }
        System.out.println("The URL was not in the list!");
    }

    public static void addURL(){
        System.out.println("Please enter website URL to add:");
        Scanner inputHandler = new Scanner(System.in);
        String tempURL = inputHandler.next();
        //Clearing one line input buffer
        inputHandler.nextLine();
        try{
            //Tries to validate the entered URL
            if(isValidURL(tempURL)){
                try {
                    for(int i = 0; i < Main.dataArraysIndex; i++){
                        //Checks if the URL is unique among the list
                        if(Main.dataArrays[i].getBlogURL().compareTo(tempURL) == 0){
                            System.out.println("The URL is already is in the list!");
                            return;
                        }
                    }
                    //The adding process to the main array
                    String blogRSS = Main.getRSSFromURL(tempURL);
                    if(blogRSS.isBlank()){
                        System.out.println("Couldn't find RSS falling back to menu.");
                        return;
                    }
                    String blogName = Main.getPageTitleFromHTML(Main.fetchPageSource(tempURL));
                    String[] payload = {blogName,tempURL,blogRSS};
                    Main.dataArrays[Main.dataArraysIndex++] = new Data(payload);
                    System.out.println("Added "+tempURL+" successfully!");
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (MalformedURLException e){

        } catch (URISyntaxException ex){

        }
    }

    //Method below source code : https://www.baeldung.com/java-validate-url#validate-url-using-jdk
    public static boolean isValidURL(String url) throws MalformedURLException, URISyntaxException {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException e) {
            System.out.println("Invalid URL.");
            return false;
        } catch (URISyntaxException e) {
            System.out.println("Invalid URL");
            return false;
        }
    }
}