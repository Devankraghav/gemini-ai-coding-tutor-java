/*
 * Flow of this Main class:
 *
 * 1. First, we import IOException and Scanner.
 *
 * 2. Scanner is used to take input from the user.
 *
 * 3. Program starts from the main() method.
 *
 * 4. We create Scanner object to read user input from keyboard.
 *
 * 5. Then we start an infinite loop using while(true).
 *    This keeps the program running again and again.
 *
 * 6. Inside the loop, we show a menu to the user:
 *    - c for C++ questions
 *    - j for Java questions
 *    - o for other/general questions
 *    - exit to stop the program
 *
 * 7. We take user's choice using sc.nextLine().
 *
 * 8. trim() removes extra spaces from start and end.
 *
 * 9. If user types exit, we print "Program ended" and stop the loop using break.
 *
 * 10. If user enters anything other than c, j, o, or exit,
 *     we show invalid choice message and restart the loop using continue.
 *
 * 11. If choice is valid, we ask the user to enter a question.
 *
 * 12. Then we show "Processing... Please wait" message.
 *
 * 13. We send the user's question and choice to ApiService.getQuestion().
 *
 * 14. ApiService sends the question to Gemini API and returns the answer.
 *
 * 15. We print the answer on the console.
 *
 * 16. If a network/input error occurs, IOException catch block handles it.
 *
 * 17. If the request is interrupted, InterruptedException catch block handles it.
 *
 * 18. After user types exit, the loop ends.
 *
 * 19. Finally, we close the Scanner using sc.close().
 */
 
   -----------------------------------------------------------------------------------------------------------------
   
 /*
 * Step-wise flow of this ApiService class:
 *
 * 1. First, we import the required classes for:
 *    - handling errors
 *    - creating API URL
 *    - sending HTTP request
 *    - receiving HTTP response
 *    - creating JSON data
 *
 * 2. Then, we get the Gemini API key from Environment Variables
 *    using System.getenv("GEMINI_API_KEY").
 *
 * 3. Main class sends two values to getQuestion():
 *    - user's question
 *    - user's choice: j, c, or o
 *
 * 4. First, we check whether the API key is available or not.
 *    If API key is missing, we return an error message.
 *
 * 5. Then, we call buildPrompt() method.
 *    This method creates a proper prompt according to user's choice.
 *
 * 6. If user selected:
 *    - j, we create Java tutor prompt
 *    - c, we create C++ tutor prompt
 *    - o, we create general teacher prompt
 *
 * 7. After creating the prompt, we create the Gemini API URL
 *    and attach the API key with it.
 *
 * 8. Then, we create an HttpClient.
 *    This client will send the request to Gemini API.
 *
 * 9. After that, we convert our prompt into JSON format.
 *
 * 10. JSON is created in this structure:
 *     requestBody
 *        -> contents
 *            -> content
 *                -> parts
 *                    -> part
 *                        -> text = prompt
 *
 * 11. Then, we create an HTTP POST request.
 *     In this request, we add:
 *     - API URL
 *     - Content-Type header
 *     - JSON request body
 *
 * 12. Then, we send the request using client.send().
 *
 * 13. Gemini API sends a response back.
 *
 * 14. We check the response status code.
 *     If status code is not 200, we return the error response.
 *
 * 15. If status code is 200, we convert the response body into JSONObject.
 *
 * 16. Then, we extract the actual answer from:
 *     candidates -> content -> parts -> text
 *
 * 17. Finally, we return that answer back to Main class.
 *
 * 18. Main class prints the final answer on the console.
 */
