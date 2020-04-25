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
