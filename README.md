# Gemini AI Coding Tutor - Java Console App

A Java console-based AI tutor that uses the Gemini API to answer coding and general questions.

## Features

- Java coding question support
- C++ coding question support
- General question support
- Menu-based console interface
- Uses Gemini API
- API key handled using environment variable
- Beginner-friendly formatted answers

## Tech Stack

- Java
- Gemini API
- HTTP Client
- JSON library

## How It Works

1. User selects the answer type:
   - `j` for Java
   - `c` for C++
   - `o` for other/general questions

2. User enters a question.

3. The app builds a prompt based on the selected choice.

4. The prompt is sent to Gemini API.

5. The response is displayed in the console.

## Environment Variable

Set your Gemini API key as an environment variable:

```text
GEMINI_API_KEY=your_api_key_here
