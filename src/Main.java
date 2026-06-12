import java.io.IOException; /*
This imports the IOException class.

IOException means input/output error.

For example:

- internet problem
- API request failed due to connection issue
- reading/writing problem

Your API call can throw this error, so you imported it. */

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println("\nWhich type of answer do you want?");
            System.out.println("Enter c for C++ coding questions");
            System.out.println("Enter j for Java coding questions");
            System.out.println("Enter o for Other/general questions");
            System.out.println("Enter exit to stop");
            System.out.print("Your choice: ");

            String languageChoice = sc.nextLine().trim(); //.trim() - Removes extra spaces from start and end.

            if (languageChoice.equalsIgnoreCase("exit")) { //equalsIgnoreCase() means it ignores uppercase/lowercase.
                System.out.println("Program ended.");
                break;
            }

            if (!languageChoice.equalsIgnoreCase("c")
                    && !languageChoice.equalsIgnoreCase("j")
                    && !languageChoice.equalsIgnoreCase("o")) {
                System.out.println("Invalid choice. Please enter c, j, o, or exit.");
                continue;
            }

            System.out.print("\nEnter your question: ");
            String ques = sc.nextLine().trim();

            try {
                System.out.println("\nProcessing... Please wait\n");

                String answer = ApiService.getQuestion(ques, languageChoice); //It sends two things to your ApiService class (ques and languageChoice) And getQuestion() is a method.
                //here, we used getQuestion method directly using class we don't need to create object of class (as ApiService Class is Static).
                System.out.println("Answer:\n");
                System.out.println(answer);

                System.out.println("\n----------------------------------------");

            } catch (IOException e) {
                System.out.println("Network/Input error occurred.");
                e.printStackTrace(); //This prints the full technical error details.
            } catch (InterruptedException e) { 
            	/*This catches interruption errors.
            	This can happen if the API request gets interrupted while waiting.*/
            	
                System.out.println("Request was interrupted.");
                e.printStackTrace();
                Thread.currentThread().interrupt();
                /*This is good practice.
                When an InterruptedException happens, Java clears the interrupted status.
                So this line sets it again. (MAKE JAVA REMEMBER SOMETHING HAPPENED)
                
                Why put it back?
                		Because some other part of the program may need to know:
                		Was this thread interrupted?*/
                
            }
        }

        sc.close(); 
        /*Good practice is to close resources after using them.
        This line runs only after the loop ends, meaning after user types exit.*/
    }
}
