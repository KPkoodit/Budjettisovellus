# Budget calculator app
This application is used for tracking expenses and calculating budget for the remaining month.
The app allows users to add expenses into self created categories and provides graphs and charts to help them understand their spending habits better.

## Team

* Wille Pulkkinen
* Hanne Salmi
* Minna Karhu
* Katja Peltonen

## General info
Desktop application that allows user to add expenses and track their spending. Some features of the application:
* Adding new userprofiles including nickname and maximum budget
* Choosing a profile from all the created ones
* Adding expenses including name, price, date, category, user and description 
* Adding new categories
* Modifying and removing expenses and categories
* Viewing expenses in chart and filtering expenses by category
* Viewing spending habits through graphs  

In documents folder, you can find a manual and documentation for the application in PDF format. Sometimes Github has problems opening large PDFs. If this happens, waiting for a while the page open and refreshing the page after waiting helps. Another option is to download a raw version of the document.

## Additional info
Some functionalities (Ostoslista and Muistilista) of this version of the program have been removed from the front page because they require a paid API-key from OpenAi. Functionalities are still visible in the manual found under documents but they cannot be used anymore.

## Technologies used:
* **Java**: The programming language used for the project.
* **JavaFX**: A set of libraries which allow the creation of a graphical user interface (GUI) for a desktop app.
* **Maven**: A build automation tool which manages dependencies and builds the app.
* **MariaDB**: A database used for storing the information.
* **Jakarta Persistence (JPA)**: A library which provides an object-relational mapping (ORM) framework for Java.
* **Hibernate**: A framework that simplifies the communication between a program and a database.
* **JUnit 4**: A framework used for unit testing.
* **Mockito**: A mocking framework used for testing.

## Installation and configuration

Installing MariaDB before running the application is necessary.
After installing, you need to create a new database for the expenses with the following credentials:
* Database Name: budjettisovellus
* Username: your_username
* Password: your_password

Update the persistence.xml file located in the META-INF folder with your database credentials:
* property: javax.persistence.jdbc.user -> your_username
* property: javax.persistence.jdbc.password -> your_password

After this, the application can be run using Eclipse or other IDE. Class MainView contains the main class of the newest version of the application, so this is the class you should run. Class Main contains the old graphic user interface.

Alternatively the application can be run through command line. To run the application through command line, ensure that Maven is installed and added to system's PATH environment variable. Open a command prompt or terminal and navigate to the root directory of the application. Then type the following commands (before running the third command, change the module path of the command to match a real javafx module path of your device):
*	mvn compile
*	mvn package
* java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -jar target/projekti-0.0.1-SNAPSHOT.jar

