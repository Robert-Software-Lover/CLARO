import Service.DescriptorService;
import java.util.Scanner;

public class MainDescriptorApp {

    public static void main(String []args) {

        int attempts = 1; // Initialize the attempts counter to 1

        while (attempts <= 3){ // Loop until attempts exceeds 3

            System.out.print("Ruta de archivo: "); // Print a prompt for the user to enter a file path

            Scanner console = new Scanner(System.in); // Create a new Scanner instance to read user input

            String path = console.nextLine(); // Read the user's input as a string

            DescriptorService descriptorService = new DescriptorService(); // Create a new instance of the DescriptorService class

            System.out.println(descriptorService.GetDescriptionFromClass(path)); // Call the GetDescriptionFromClass method on the DescriptorService instance and print the result

            System.out.println(""); // Print an empty line for formatting purposes

            if(descriptorService.GetDescriptionFromClass(path).contains("not found")){ // Check if the result of GetDescriptionFromClass contains the string "not found"

                if(attempts==3) { // If this is the third attempt,

                    System.out.println("Has alcanzado el # maximo de intentos."); // inform the user that they have exceeded the maximum number of attempts.

                }

                attempts++; // Increment the attempts counter

            }
            else{ // If the result of GetDescriptionFromClass does not contain "not found", exit the loop.

                break;

            }


        }

    }
}
