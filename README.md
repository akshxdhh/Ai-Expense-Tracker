# AI Expense Tracker
*Java Swing ProjectA modern desktop expense tracker application built with Java Swing. This app simplifies expense management by allowing users to add expenses manually or by simply uploading a picture of a receipt, which is then automatically processed by the Gemini AI to extract the relevant details.*

# ✨ Features

⌨ Manual Data Entry: A straightforward interface to add expenses by typing the name, amount, and category.

💯 View All Expenses: See a clear, sortable table of all your past expenses.

🚮 Delete Expenses: Remove expenses you no longer need.

📁 Local Database Storage: All expenses are securely saved in a local SQLite database file (expensetracker.db), making the application self-contained and portable.

📊 Clear Expense Table: View all your past expenses in a simple, sortable table.

🛠️ Technology StackCore: Java 11GUI: Java SwingDatabase: SQLite with JDBC DriverAI & Vision: Google Gemini APIBuild & Dependency Management: Apache Maven

# 📂 Project Structure

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
# 🚀 Getting Started

*Follow these steps to get the project running on your local machine.*
```bash
1. Prerequisites
   Java Development Kit (JDK): Version 11 or newer.
   Apache Maven: Ensure Maven is installed and configured on your system to manage dependencies.
   IDE (Recommended): An IDE like IntelliJ IDEA, Eclipse, or VS Code with Java support.
   Google Gemini API Key: You must have an API key from Google AI Studio.
                          Visit Google AI Studio.
                          Click on "Get API key" and create a new key.

2. Configuration
   You must add your Gemini API key to the project before running it.  
   Navigate to src/main/java/com/expensetracker/OpenAiApiService.java.
   Find the following line:private static final String API_KEY = "YOUR_API_KEY_HERE";
   Replace "YOUR_API_KEY_HERE" with your actual Gemini API key.

3. Build & Run (Using an IDE - Easiest Method)
   Open Project: In your IDE, choose File > Open... and select the project pom.xml file. 
   Your IDE will automatically detect it as a Maven project and download the necessary dependencies 
   (like SQLite and JSON parsers).
   Run Application: Locate the ExpenseTrackerApp.java file, right-click it, and select "Run".

4. Build & Run (Using the Command Line)Open Terminal: 
   Navigate to the root directory of the project (ai-expense-tracker/).
   Build with Maven: Run the package command. 
   This will compile the code and create a single executable JAR with all dependencies included.mvn clean package
   Run the JAR: Once the build is successful, run the application using the following command:java -jar target/ai-expense-tracker-1.0.0-jar-with-dependencies.jar
   The application window should now appear, and an expensetracker.db file will be created in the root directory on the first run.
```
## Contributing

Pull requests are welcome. For major changes, please open an issue first
to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

[MIT](https://choosealicense.com/licenses/mit/)
