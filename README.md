# AI Expense Tracker
*Java Swing ProjectA modern desktop expense tracker application built with Java Swing. This app simplifies expense management by allowing users to add expenses manually or by simply uploading a picture of a receipt, which is then automatically processed by the Gemini AI to extract the relevant details.*

## ✨ Features

⌨ Manual Data Entry: A straightforward interface to add expenses by typing the name, amount, and category.

💯 View All Expenses: See a clear, sortable table of all your past expenses.

🚮 Delete Expenses: Remove expenses you no longer need.

📁 Local Database Storage: All expenses are securely saved in a local SQLite database file (expensetracker.db), making the application self-contained and portable.

📊 Clear Expense Table: View all your past expenses in a simple, sortable table.

## 🛠️ Technology Stack
               • Core: Java 11
               • GUI: Java Swing
               • Database: SQLite with JDBC Driver
               • Dependency Management: Apache Maven

## 📂 Project Structure

*The project follows the standard Maven directory layout for clean and organized code:*
```bash
ai-expense-tracker/
├── pom.xml                   # Maven configuration file for dependencies
├── .gitignore                # Specifies files for Git to ignore
├── README.md                 # This project overview file
└── src/
    └── main/
        └── java/
            └── com/
                └── expensetracker/
                    ├── Expense.java            # Data model for an expense
                    ├── DatabaseHelper.java     # Handles all JDBC/SQLite operations
                    ├── OpenAiApiService.java   # Manages communication with Gemini API
                    └── ExpenseTrackerApp.java  # Main class with the Swing GUI
```
## 🚀 Getting Started

*Follow these steps to get the project running on your local machine.*
```bash
1. Prerequisites
   Git: To download the source code. (Download Git)
   Java Development Kit (JDK): Version 11 or higher. (Download OpenJDK)
   Apache Maven: To build the project. (Download Maven)
 ``` 

## How to Set Up and Run

*You can run this project either directly from an IDE like IntelliJ IDEA or from your computer's command line.*

Option 1: Running from an IDE (IntelliJ IDEA)

1. Clone the Repository:
```bash
git clone [https://github.com/akshxdhh/Ai-Expense-Tracker.git](https://github.com/akshxdhh/Ai-Expense-Tracker.git)
```
2. Open in IntelliJ IDEA:
      • Launch IntelliJ IDEA.
      • Select File > Open....
      • Navigate to the cloned project folder and select the pom.xml file.
      • Choose "Open as Project".

3. Load Maven Dependencies:
      • IntelliJ will automatically read the pom.xml file.
      • If prompted, click "Load Maven Changes" or open the Maven tool window on the right and click the "Reload All Maven Projects" button. This will download the necessary SQLite-JDBC driver.

4. Run the Application:
      • Navigate to the src/main/java/com/expensetracker folder.
      • Right-click on the ExpenseTrackerApp.java file and select "Run 'ExpenseTrackerApp.main()'".

# Option 2: Running from the Command Line

**Prerequisites**
*Before you begin, you must have the following software installed and configured in your system's PATH.*
      • Git: To download the source code. ([Download Git](https://git-scm.com/downloads))
      • Java Development Kit (JDK): Version 11 or higher. ([Download OpenJDK](https://adoptium.net/))
      • Apache Maven: To build the project. ([Download Maven](https://maven.apache.org/download.cgi))

**Steps**

1. Clone the Repository:
*Open your command prompt, navigate to a directory of your choice, and run:*
```bash
git clone [https://github.com/akshxdhh/Ai-Expense-Tracker.git](https://github.com/akshxdhh/Ai-Expense-Tracker.git)
```
2. Navigate to the Project Directory:
```bash
cd Ai-Expense-Tracker
```

3. Build the Project:
*Use Maven to compile the code and package it into a runnable*.jar *file.*
```bash
mvn clean package
```
You will see a **BUILD SUCCESS** message when it's done. The output file will be located in the **target/** directory.

5. Run the Application:
*Execute the packaged* .jar *file using the* java *command.*
```bash
java -jar target/ai-expense-tracker-1.0.0.jar
```

The application window should now appear. The expensetracker.db file will be created in the project folder as you add your first expense.
## Contributing

Pull requests are welcome. For major changes, please open an issue first
to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

[MIT](https://choosealicense.com/licenses/mit/)
