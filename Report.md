# Report

Submitted report to be manually graded. We encourage you to review the report as you read through the provided
code as it is meant to help you understand some of the concepts. 

## Technical Questions

1. What is the difference between == and .equals in java? Provide a code example of each, where they would return different results for an object. Include the code snippet using the hash marks (```) to create a code block.
   ```java
   public class Main { 
       public static void main(String[] args) {
           // Create two distinct String objects with identical content
           String str1 = new String("hello");
           String str2 = new String("hello");

           // Using '==' compares if str1 and str2 reference the same object in memory.
           System.out.println("Using '==': " + (str1 == str2)); // Expected output: false

           // Using '.equals()' compares the content of the two strings.
           System.out.println("Using '.equals()': " + str1.equals(str2)); // Expected output: true
       }
   }
   ```




2. Logical sorting can be difficult when talking about case. For example, should "apple" come before "Banana" or after? How would you sort a list of strings in a case-insensitive manner? 

In Java we can sort strings without considering case by using a comparator that ignores case differences. One of the simplest approaches is to use the built-in comparator, String.CASE_INSENSITIVE_ORDER, which compares strings in a case-insensitive manner. This way, "apple" and "Apple" will be treated as equal in terms of ordering, and we'll get a logical (alphabetical) sort regardless of case.



3. In our version of the solution, we had the following code (snippet)
    ```java
    public static Operations getOperatorFromStr(String str) {
        if (str.contains(">=")) {
            return Operations.GREATER_THAN_EQUALS;
        } else if (str.contains("<=")) {
            return Operations.LESS_THAN_EQUALS;
        } else if (str.contains(">")) {
            return Operations.GREATER_THAN;
        } else if (str.contains("<")) {
            return Operations.LESS_THAN;
        } else if (str.contains("=="))...
    ```
    Why would the order in which we checked matter (if it does matter)? Provide examples either way proving your point. 

The order matters because some operators are substrings of others. For instance, the string ">=" contains the character ">". If you check for ">" before checking for ">=", then the method will match ">" and return the wrong result. This is why you should check for the longer (multi-character) operators first.


4. What is the difference between a List and a Set in Java? When would you use one over the other? 

A List in Java is an ordered collection that allows duplicate elements. This means that elements maintain their insertion order and you can access them by their index. Lists are ideal when you need to maintain the order of elements or require positional access.


A Set, on the other hand, is an unordered collection that does not allow duplicate elements. This makes Sets useful when you need to ensure that every element is unique.


5. In [GamesLoader.java](src/main/java/student/GamesLoader.java), we use a Map to help figure out the columns. What is a map? Why would we use a Map here? 

A Map in Java is a collection that stores key-value pairs, meaning each unique key is associated with a specific value. Unlike lists or sets, Maps are designed for fast lookups, insertions, and updates based on keys.

In the context of GamesLoader.java, a Map is likely used to associate column names (the keys) with their corresponding indices (the values). This makes it easy to reference a column's position in a dataset without scanning through the entire header row every time.


6. [GameData.java](src/main/java/student/GameData.java) is actually an `enum` with special properties we added to help with column name mappings. What is an `enum` in Java? Why would we use it for this application?

An enum in Java is a special data type that represents a fixed set of constants. Rather than using literal strings or integers throughout your code, enums allow you to define a group of related constants in one central place with type safety and clarity.

In the context of the GameData.java file, the enum is used to represent the different columns of game data from a CSV file. All column names are defined in one place. If a column name changes in the CSV file, you only need to update it in the enum.




7. Rewrite the following as an if else statement inside the empty code block.
    ```java
    switch (ct) {
                case CMD_QUESTION: // same as help
                case CMD_HELP:
                    processHelp();
                    break;
                case INVALID:
                default:
                    CONSOLE.printf("%s%n", ConsoleText.INVALID);
            }
    ``` 

    ```java
    if (ct == Command.CMD_QUESTION || ct == Command.CMD_HELP) {
        processHelp();
    } else {
        CONSOLE.printf("%s%n", ConsoleText.INVALID);
    }  
    ```

## Deeper Thinking

ConsoleApp.java uses a .properties file that contains all the strings
that are displayed to the client. This is a common pattern in software development
as it can help localize the application for different languages. You can see this
talked about here on [Java Localization – Formatting Messages](https://www.baeldung.com/java-localization-messages-formatting).

Take time to look through the console.properties file, and change some of the messages to
another language (probably the welcome message is easier). It could even be a made up language and for this - and only this - alright to use a translator. See how the main program changes, but there are still limitations in 
the current layout. 

Post a copy of the run with the updated languages below this. Use three back ticks (```) to create a code block.

Below is an example of a console run after updating the welcome message in the properties file to Spanish. Notice that the welcome text now appears in Spanish (“Bienvenido a la Arena de Juegos!”), while the rest of the messages remain in their original language (highlighting one limitation of the current layout):
```text
Bienvenido a la Arena de Juegos!
Please enter a command: exit
Goodbye!
```

Now, thinking about localization - we have the question of why does it matter? The obvious
one is more about market share, but there may be other reasons.  I encourage
you to take time researching localization and the importance of having programs
flexible enough to be localized to different languages and cultures. Maybe pull up data on the
various spoken languages around the world? What about areas with internet access - do they match? Just some ideas to get you started. Another question you are welcome to talk about - what are the dangers of trying to localize your program and doing it wrong? Can you find any examples of that? Business marketing classes love to point out an example of a car name in Mexico that meant something very different in Spanish than it did in English - however [Snopes has shown that is a false tale](https://www.snopes.com/fact-check/chevrolet-nova-name-spanish/).  As a developer, what are some things you can do to reduce 'hick ups' when expanding your program to other languages?


As a reminder, deeper thinking questions are meant to require some research and to be answered in a paragraph for with references. The goal is to open up some of the discussion topics in CS, so you are better informed going into industry. 

Localization matters not only for expanding market share but also for creating software that is culturally sensitive, accessible, and user-friendly. For instance, with over 7,000 languages spoken worldwide, developers must consider that large user bases might require interfaces in languages other than English—especially in regions like Latin America, Asia, and Africa, where internet access is rapidly growing. Poor localization can lead to miscommunications, layout issues, or even cultural offenses, potentially damaging a brand’s reputation. While the famous tale of the Chevrolet Nova in Mexico has been debunked, there are many documented cases of localization errors causing real problems. To mitigate such risks, developers are encouraged to externalize strings into resource files, adopt internationalization (i18n) best practices from the design phase, and work with professional translators and cultural consultants to ensure that translations and adaptations are accurate and culturally appropriate.

References:

Ethnologue. (n.d.). Ethnologue: Languages of the World. Retrieved from https://www.ethnologue.com/

Snopes. (2020). Chevrolet Nova Name in Spanish. Retrieved from https://www.snopes.com/fact-check/chevrolet-nova-name-spanish/

Baeldung. (2020). Java Localization – Formatting Messages. Retrieved from https://www.baeldung.com/java-localization-messages-formatting