
# Java RSS Reader

This is a simple Java application that allows users to manage and view updates from their favorite RSS feeds.

## Features

- Add a new RSS feed URL
- Delete an existing RSS feed URL
- View updates from selected RSS feeds


## Installing
1. Clone the repository to your local machine
```bash
    git clone https://github.com/MKmasterg/JavaRSSReader.git
```
2. Download jsoup library from https://jsoup.org/. You will find the download link on the homepage. Download the latest version of the library.
3. After downloading the JSOUP library, you should include it in your project. If you're using IntelliJ IDEA, you can do this by following these steps:  
- Open your project in IntelliJ IDEA.
- Go to File -> Project Structure -> Libraries.
- Click on the + button and select Java.
- Navigate to the location where you downloaded the JSOUP library, select the .jar file and click OK.
- Click Apply and then OK.

4. Open the project in your IDE.  
5. Run the Main.java file to start the application.

## Usage

The application presents a menu with four options:

1. Show updates: This option allows you to view updates from your selected RSS feeds. You can choose to view updates from all feeds or select a specific one.
2. Add URL: This option allows you to add a new RSS feed URL to your list.
3. Delete URL: This option allows you to delete an existing RSS feed URL from your list.
4. Exit: This option allows you to exit the application. By selecting this option, the application will write the current state of your RSS feeds to a file(data.txt). This allows the application to restore your previous settings the next time it runs.

*This is a part of the Advanced Programming course assignment at Amirkabir University of Technology.*
