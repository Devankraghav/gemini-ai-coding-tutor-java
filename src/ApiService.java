import java.io.IOException; // IOException means input/output error.
import java.net.URI; // URI means Uniform Resource Identifier. It represents a web address.
import java.net.http.HttpClient; // HttpClient is used to send HTTP requests.
import java.net.http.HttpRequest; // HttpRequest is used to create the request we want to send.
import java.net.http.HttpResponse; // HttpResponse stores the response that comes back from the API.

import org.json.JSONArray; // JSONArray is used to create JSON arrays: [ ]
import org.json.JSONObject; // JSONObject is used to create JSON objects: { }

public class ApiService {

    /*
     * Recommended:
     * Do not write your API key directly inside the code.
     *
     * Instead, set it in Environment Variables.
     *
     * Variable name: GEMINI_API_KEY
     * Variable value: Your actual Gemini API key
     */
    private static final String API_KEY = System.getenv("GEMINI_API_KEY");

    /*
     * This method receives two values from Main class:
     *
     * 1. question
     *    - The question typed by the user.
     *
     * 2. languageChoice
     *    - c means C++
     *    - j means Java
     *    - o means Other/general question
     *
     * This method sends the question to Gemini API and returns the answer.
     */
    public static String getQuestion(String question, String languageChoice)
            throws IOException, InterruptedException {

        /*
         * First, we check whether the API key exists or not.
         *
         * API_KEY == null means:
         * Environment variable is not found.
         *
         * API_KEY.isBlank() means:
         * Environment variable exists, but it is empty.
         */
        if (API_KEY == null || API_KEY.isBlank()) {
            return "API key not found. Please set GEMINI_API_KEY environment variable.";
        }

        /*
         * buildPrompt() creates the final instruction for Gemini.
         *
         * Example:
         * If user chooses j, it creates a Java tutor prompt.
         * If user chooses c, it creates a C++ tutor prompt.
         * If user chooses o, it creates a general teacher prompt.
         */
        String prompt = buildPrompt(question, languageChoice);

        /*
         * If buildPrompt() returns null, it means the choice was invalid.
         *
         * In your Main class, you already validate c, j, and o.
         * But this check is still good for safety.
         */
        if (prompt == null) {
            return "Invalid choice. Please enter c for C++, j for Java, or o for Other questions.";
        }

        /*
         * This is the Gemini API URL.
         *
         * The API key is added at the end of the URL.
         * Gemini uses this key to identify your request.
         */
        String apiUrl =
                "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-lite:generateContent?key="
                        + API_KEY;

        /*
         * HttpClient is used to send the HTTP request.
         *
         * Simple meaning:
         * This object works like Postman/browser inside Java.
         */
        HttpClient client = HttpClient.newHttpClient();

        /*
         * Gemini API expects data in JSON format.
         *
         * Required JSON format:
         *
         * {
         *   "contents": [
         *     {
         *       "parts": [
         *         {
         *           "text": "your prompt here"
         *         }
         *       ]
         *     }
         *   ]
         * }
         */

        /*
         * This creates one JSON object named part.
         *
         * At first, it looks like:
         * { }
         */
        JSONObject part = new JSONObject();

        /*
         * This adds the prompt into the part object.
         *
         * Now it becomes:
         * {
         *   "text": "your full prompt here"
         * }
         */
        part.put("text", prompt);

        /*
         * This creates a JSON array named parts.
         *
         * At first, it looks like:
         * [ ]
         */
        JSONArray parts = new JSONArray();

        /*
         * This puts the part object inside the parts array.
         *
         * Now it becomes:
         * [
         *   {
         *     "text": "your full prompt here"
         *   }
         * ]
         */
        parts.put(part);

        /*
         * This creates a JSON object named content.
         *
         * It will store the parts array.
         */
        JSONObject content = new JSONObject();

        /*
         * This adds parts array into content object.
         *
         * Now it becomes:
         * {
         *   "parts": [
         *     {
         *       "text": "your full prompt here"
         *     }
         *   ]
         * }
         */
        content.put("parts", parts);

        /*
         * This creates a JSON array named contents.
         *
         * Gemini expects "contents" to be an array.
         */
        JSONArray contents = new JSONArray();

        /*
         * This puts the content object inside contents array.
         */
        contents.put(content);

        /*
         * This creates the final request body object.
         *
         * This is the full JSON that will be sent to Gemini.
         */
        JSONObject requestBody = new JSONObject();

        /*
         * This adds contents array into requestBody.
         *
         * Final JSON becomes:
         *
         * {
         *   "contents": [
         *     {
         *       "parts": [
         *         {
         *           "text": "your full prompt here"
         *         }
         *       ]
         *     }
         *   ]
         * }
         */
        requestBody.put("contents", contents);

        /*
         * Now we create the actual HTTP request.
         *
         * This request contains:
         * - API URL
         * - Header
         * - POST method
         * - JSON body
         */
        HttpRequest request = HttpRequest.newBuilder()

                /*
                 * uri() sets the API URL.
                 *
                 * URI.create(apiUrl) converts String URL into URI object.
                 */
                .uri(URI.create(apiUrl))

                /*
                 * Header gives extra information about the request.
                 *
                 * Content-Type means type of data we are sending.
                 * application/json means we are sending JSON data.
                 */
                .header("Content-Type", "application/json")

                /*
                 * POST means we are sending data to the server.
                 *
                 * requestBody.toString() converts JSON object into String.
                 *
                 * BodyPublishers.ofString() tells Java:
                 * Send this String as request body.
                 */
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))

                /*
                 * build() completes the request creation.
                 *
                 * After this, request is ready to send.
                 */
                .build();

        /*
         * This line sends the request to Gemini API.
         *
         * client.send() sends the request and waits for response.
         *
         * BodyHandlers.ofString() means:
         * Read the response body as String.
         */
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        /*
         * Status code tells whether request was successful or not.
         *
         * 200 means success.
         *
         * If status code is not 200, we return error details.
         */
        if (response.statusCode() != 200) {
            return "Error: HTTP Status " + response.statusCode()
                    + "\nResponse: " + response.body();
        }

        /*
         * response.body() contains the full response as String.
         *
         * This line converts that response String into JSONObject,
         * so we can read specific values from it.
         */
        JSONObject jsonResponse = new JSONObject(response.body());

        /*
         * Gemini response usually looks like this:
         *
         * {
         *   "candidates": [
         *     {
         *       "content": {
         *         "parts": [
         *           {
         *             "text": "actual answer here"
         *           }
         *         ]
         *       }
         *     }
         *   ]
         * }
         *
         * We need to extract only the "text" value.
         */
        try {
            return jsonResponse
                    .getJSONArray("candidates") // Get candidates array.
                    .getJSONObject(0) // Get first candidate.
                    .getJSONObject("content") // Get content object.
                    .getJSONArray("parts") // Get parts array.
                    .getJSONObject(0) // Get first part.
                    .getString("text"); // Get actual answer text.

        } catch (Exception e) {

            /*
             * If response format changes or text is missing,
             * this catch block will run.
             *
             * Instead of crashing the program,
             * we return the full response for debugging.
             */
            return "Unable to read response properly.\nFull Response:\n" + response.body();
        }
    }

    /*
     * This method builds the prompt according to user choice.
     *
     * j = Java coding tutor prompt
     * c = C++ coding tutor prompt
     * o = Other/general teacher prompt
     */
    private static String buildPrompt(String question, String languageChoice) {

        /*
         * If user selected j, create Java-specific prompt.
         */
        if (languageChoice.equalsIgnoreCase("j")) {

            return "Act as a senior Java expert and a friendly coding tutor.\n\n"
                    + "Explain the answer in very simple English, like you are teaching a beginner student.\n"
                    + "Do not skip small details.\n\n"

                    + "Use this exact format:\n\n"

                    + "1. Concept\n"
                    + "- Explain what this topic/problem means in simple words.\n\n"

                    + "2. Simple Idea\n"
                    + "- Explain the basic logic before writing code.\n"
                    + "- Make it easy to understand.\n\n"

                    + "3. Real-Life Example\n"
                    + "- Give one real-life example or analogy.\n\n"

                    + "4. Step-by-Step Approach\n"
                    + "- Explain the steps to solve the problem.\n\n"

                    + "5. Java Code\n"
                    + "- Write clean, beginner-friendly Java code.\n"
                    + "- Use proper class name, main method if needed, and comments.\n\n"

                    + "6. Line-by-Line Explanation\n"
                    + "- Explain every important line of the code.\n"
                    + "- Tell why that line is used.\n\n"

                    + "7. Dry Run\n"
                    + "- Take a small example input.\n"
                    + "- Show how the code works step by step.\n\n"

                    + "8. Common Mistakes\n"
                    + "- Mention common mistakes beginners make in this topic.\n\n"

                    + "9. Time and Space Complexity\n"
                    + "- Explain time complexity in simple words.\n"
                    + "- Explain space complexity in simple words.\n\n"

                    + "10. Final Summary\n"
                    + "- Give a short final revision of the concept.\n\n"

                    + "Rules:\n"
                    + "- Use simple English.\n"
                    + "- Avoid unnecessary difficult words.\n"
                    + "- If the question is incomplete, assume a beginner-friendly version and explain it.\n"
                    + "- Focus more on understanding than only giving code.\n\n"

                    + "Question:\n" + question;

        /*
         * If user selected c, create C++-specific prompt.
         */
        } else if (languageChoice.equalsIgnoreCase("c")) {

            return "Act as a senior C++ expert and a friendly coding tutor.\n\n"
                    + "Explain the answer in very simple English, like you are teaching a beginner student.\n"
                    + "Do not skip small details.\n\n"

                    + "Use this exact format:\n\n"

                    + "1. Concept\n"
                    + "- Explain what this topic/problem means in simple words.\n\n"

                    + "2. Simple Idea\n"
                    + "- Explain the basic logic before writing code.\n"
                    + "- Make it easy to understand.\n\n"

                    + "3. Real-Life Example\n"
                    + "- Give one real-life example or analogy.\n\n"

                    + "4. Step-by-Step Approach\n"
                    + "- Explain the steps to solve the problem.\n\n"

                    + "5. C++ Code\n"
                    + "- Write clean, beginner-friendly C++ code.\n"
                    + "- Use proper main function and comments.\n\n"

                    + "6. Line-by-Line Explanation\n"
                    + "- Explain every important line of the code.\n"
                    + "- Tell why that line is used.\n\n"

                    + "7. Dry Run\n"
                    + "- Take a small example input.\n"
                    + "- Show how the code works step by step.\n\n"

                    + "8. Common Mistakes\n"
                    + "- Mention common mistakes beginners make in this topic.\n\n"

                    + "9. Time and Space Complexity\n"
                    + "- Explain time complexity in simple words.\n"
                    + "- Explain space complexity in simple words.\n\n"

                    + "10. Final Summary\n"
                    + "- Give a short final revision of the concept.\n\n"

                    + "Rules:\n"
                    + "- Use simple English.\n"
                    + "- Avoid unnecessary difficult words.\n"
                    + "- If the question is incomplete, assume a beginner-friendly version and explain it.\n"
                    + "- Focus more on understanding than only giving code.\n\n"

                    + "Question:\n" + question;

        /*
         * If user selected o, create general teacher prompt.
         *
         * This is useful for non-coding questions.
         */
        } else if (languageChoice.equalsIgnoreCase("o")) {

            return "Act as a highly skilled teacher and explain the answer in very simple English.\n\n"

                    + "Use this format only if suitable:\n\n"

                    + "1. Simple Explanation\n"
                    + "- Explain the topic clearly from basic level.\n\n"

                    + "2. Easy Example\n"
                    + "- Give a simple example or real-life comparison.\n\n"

                    + "3. Step-by-Step Understanding\n"
                    + "- Break the answer into small points.\n\n"

                    + "4. Important Points\n"
                    + "- Mention the key things to remember.\n\n"

                    + "5. Final Summary\n"
                    + "- Give a short and clear conclusion.\n\n"

                    + "Rules:\n"
                    + "- Do not force coding format unless the question is related to programming.\n"
                    + "- Use beginner-friendly language.\n"
                    + "- Give examples wherever useful.\n"
                    + "- Make the answer easy to revise.\n\n"

                    + "Question:\n" + question;

        /*
         * If languageChoice is not j, c, or o,
         * return null.
         */
        } else {
            return null;
        }
    }
}
