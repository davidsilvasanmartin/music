# Java Exceptions

## 1. Checked vs unchecked

In Java, exceptions are events that occur during the execution of a program that disrupt the normal flow of
instructions. When an error occurs, Java creates an exception object and "throws" it. The system then searches for code
to handle that exception. Java categorizes exceptions (specifically, those inheriting from the `Throwable` class) into
two main types based on how the compiler treats them:

1. **Checked Exceptions**
2. **Unchecked Exceptions**

### 1. Checked Exceptions

- **What they are:** These are exceptions that the **compiler forces** you to deal with. They represent conditions that
  a well-written application should anticipate and potentially recover from. Think of external factors like missing
  files or network problems.
- **Rule:** If a method might throw a checked exception (either directly with `throw` or by calling another method that
  throws one), it _must_ do one of two things:
    - **Handle it:** Catch the exception using a `try...catch` block.
    - **Declare it:** Specify that the method throws the exception using the `throws` keyword in the method signature.
      This passes the responsibility of handling it to the method that calls _this_ method.

- **Inheritance:** They inherit directly from the `java.lang.Exception` class but _not_ from
  `java.lang.RuntimeException`.
- **Examples:** `IOException`, `FileNotFoundException`, `SQLException`.

**Simple Example (Checked):**
Imagine reading from a file. The file might not exist (`FileNotFoundException` is a type of `IOException`).

_Handling with `try-catch`:_

``` java
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

public class CheckedExample {

    public void readFile(String fileName) {
        File file = new File(fileName);
        try {
            // FileReader constructor can throw FileNotFoundException (checked)
            FileReader reader = new FileReader(file);
            System.out.println("File found and opened.");
            // ... read the file ...
            reader.close(); // close() can throw IOException (checked)
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found: " + fileName);
            // Handle the error, maybe provide a default value or log it
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            // Handle other I/O errors
        }
    }

    public static void main(String[] args) {
        CheckedExample example = new CheckedExample();
        example.readFile("myFile.txt"); // Compiler is happy because readFile handles it
    }
}
```

_Declaring with `throws`:_

``` java
import java.io.File;
import java.io.FileReader;
import java.io.IOException; // Covers FileNotFoundException too

public class CheckedThrowsExample {

    // We declare that this method might throw an IOException
    public void readFileAndThrow(String fileName) throws IOException {
        File file = new File(fileName);
        FileReader reader = new FileReader(file); // Might throw
        System.out.println("File found and opened.");
        // ... read the file ...
        reader.close(); // Might throw
    }

    public static void main(String[] args) {
        CheckedThrowsExample example = new CheckedThrowsExample();
        try {
            // Now the CALLER must handle it because readFileAndThrow DECLARED it
            example.readFileAndThrow("myFile.txt");
        } catch (IOException e) {
            System.err.println("Error in main: " + e.getMessage());
        }
    }
}
```

### 2. Unchecked Exceptions

- **What they are:** These are exceptions that the **compiler does not force** you to handle or declare. They usually
  represent programming errors (bugs) or other unexpected runtime failures.
- **Rule:** You are _not required_ to use `try-catch` or add a `throws` clause for them, although you _can_ if you want
  to try and recover or log specifically.
- **Inheritance:** They inherit from `java.lang.RuntimeException` or `java.lang.Error`.
    - `RuntimeException` subclasses typically indicate bugs: `NullPointerException`, `ArrayIndexOutOfBoundsException`,
      `ArithmeticException` (like division by zero), `IllegalArgumentException`.
    - `Error` subclasses usually indicate serious problems outside the application's control, like JVM issues (
      `OutOfMemoryError`, `StackOverflowError`), which applications generally shouldn't try to catch.

- **Examples:** `NullPointerException`, `ArrayIndexOutOfBoundsException`, `ArithmeticException`,
  `IllegalArgumentException`.

**Simple Example (Unchecked):**

Accessing an array element outside its bounds.

``` java
public class UncheckedExample {

    public void accessArray() {
        int[] numbers = {1, 2, 3};
        try {
             // This line might throw ArrayIndexOutOfBoundsException (unchecked)
            System.out.println("Accessing element 5...");
            int number = numbers[5]; // Oops! Index 5 is out of bounds (0, 1, 2)
            System.out.println("The number is: " + number);
        } catch (ArrayIndexOutOfBoundsException e) {
            // We CAN catch it if we want, but we don't HAVE to.
            System.err.println("Error: Tried to access an invalid array index.");
            System.err.println("Exception message: " + e.getMessage());
        }
         System.out.println("Finished accessing array.");
    }

     public void causeNullPointer() {
         String text = null;
         // This line will throw NullPointerException (unchecked)
         // The compiler doesn't force us to handle it.
         System.out.println("Length of text: " + text.length());
     }


    public static void main(String[] args) {
        UncheckedExample example = new UncheckedExample();

        example.accessArray(); // We caught the exception inside the method

        System.out.println("\nNow causing a NullPointerException...");
        try {
             example.causeNullPointer(); // This will crash if not caught here
        } catch (NullPointerException e) {
             System.err.println("Caught the NullPointerException in main!");
        }

        System.out.println("Program finished.");
    }
}
```

If you removed the `try-catch` block inside `accessArray` or around the call to `causeNullPointer` in `main`, the
compiler would _still_ compile the code fine. However, the program would likely crash at runtime when the exception
occurs if it's not caught anywhere up the call stack.

### Why the Distinction?

- **Checked:** Forces developers to think about predictable problems that are often outside their direct code's
  control (like external resources). It encourages writing more robust code by prompting consideration of failure
  scenarios.
- **Unchecked:** Avoids cluttering code with `try-catch` or `throws` declarations for errors that usually stem from
  logical bugs in the code itself. The fix is generally to correct the bug (e.g., add a null check, fix loop
  boundaries), not just to catch the exception everywhere.
