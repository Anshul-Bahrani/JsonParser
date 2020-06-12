'''
I have Explained some of the stuff here and more in the documentation as you go with the flow of the file.
'''

'''
The Main goal of the parser is to convert:
  JSON          JAVA
  Array         ArrayList
  JObject       LinkedHashMap
  Number        Float
  String        String
  true/false    Boolean
  null          null
'''
### JSONPARSER PACKAGE ###
This package is related to parsing the json file and returning the java objectified Data structure.
# JsonParser
 This is a Test class to test the JsonObject class,
     I have used test.json in which i tried different json files using examples from the net.
     In the future i plan to pass this class the path of the file itself and then this class can open the file
     and pass the contents of the file to the JsonObject class

# JsonObject
 After opening the file, just pass the contents to the json object.
 According to the contents of the json file, the class of 'o' will be different.
 Example - if the json contains an array, the Object o should be of the type ArrayList.
         
# checkvalue function
  It is just a nested if-else to convert the contents to the respective class in java.(The conversions are explained in the start)
  if it does not match any val throw an Exception.
# isit methods:
  1. Number:
    Try to convert it into a number, if it does not throw a NumberFormatException, It is a number.
  2. Boolean
    Compare the string with true or false
  3. null
    Compare the string with null.
  4. Array
    After trimming the first position and last position of the string should be '[' and ']' respectively.
  5. Object 
    After trimming the first position and last position of the string should be '{' and '}' respectively.
  6. String:
    After trimming the first position and last position of the string should be '"' and '"' respectively.
  # make methods:
  1. Number
    just used Float.parseFloat
  2. Boolean and null
     Compared and converted accordingly
  3. Array:
    Removed the '[' and ']' and splitted on ',' and again called checkvalue for each value.
  4. Object:
    Removed the '{' and '}' and splitted on ',' first and ':' again and called checkvalue for each.
 # stringyarr
 This method was needed because the content as a whole is a string and thus in the following example:
 {
    "Hi:Yo" : 6
 }
  Thus for a ':' splitter,  
 the java splitter would return ["Hi", "Yo", "6"] which is wrong.
 also:
 {
    "Hi" : 6,
    "Yo" : [6, 7]
 }
 For a ',' splitter:
 the java splitter would return ["Hi : 6", "Yo : [6", " 7]"] which is wrong.
 To overcome this i have maintained a flag for '"' marks in order to maintain if i am inside the quotes(then basically ignore everything) or outside.
 And Maintained a Stack for '[ ]' and '{ }' (Keep track of brackets)
 
 # InvalidJsonException
  This was created to throw an exception when an invalid json syntax was encountered.
  In future versions i will try to elaborate more on why and how to fix the syntax of json.
  
  ### CREATE PACKAGE ###
  This package is responsible for anything related to creation of tables in the database but not actuall interaction with the database because that is done by DataBaseHelper class.
  The reason behind creating the package and not one class is future scope, as this project grows i will add support for creation of views and triggers.
# CreateTable
This class creates json files in database/migrations folder with the name specified by the user along with a timestamp.
For testing purpose i have not taken any input from the user and manually ran the java file with input test.

### DATABASE ###
This ackage is responsible for anything interacting with the batabase and reads from the conf.json file which is stored in the root directory of this git repo.
This folder also contains migrations folder where all the create table files are stored.
# DataBaseHelper
Any interaction with the database is dont by this class it has multiple methods which is well explained in the documentation as you will go through the class in the flow.

# Utils
This class contains all static methods required by other classes as a utility.
More on the methods in the documentation of the class.

# InvalidCofigException
Not a huge purpose for this class but again required as for the future scope when the functionality increases so will the conf.json file and so will the requirement of such an exception to know what exactly is wrong, the working of the app or the config file.

### MAINAPP ###
# Main
This is the main class with the main method to start the app, future scope can include a gui based interface but the requirement of that is highly unlikely.

# Initializer
This is the app responsible to initialize all the helper classes and maintaining the variables for the same so it can also act as a dependencyInjector for initializing more classes.

### MIGRATE ###
This package is responsible for running the migrations of create table(Future scope can include insert and update functionalities which will require different migration classes)
# Migrate
This class validates the files in the migrations folder and calls create method of DBHelper to create tables in the required format.

Constraint functionality is partially done which includes PRIMARY KEY, FOREIGN KEY, UNIQUE but the working of the these costraints is ok but is a bit odd.
Explanation of all the methods is done in the documentation so i prefer you go with the flow of the app starting from Main.java.
