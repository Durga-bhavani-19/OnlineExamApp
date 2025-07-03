# OnlineExamApp
# 🧠 Java Online Exam System

A desktop-based Java Swing application that simulates an online multiple-choice exam with a built-in timer, auto-submission, and scoring system. Ideal for showcasing Java GUI, event handling, and file I/O skills.

---

## 💻 Features

✅ Multiple-choice questions (MCQs)  
✅ Reads questions from a `questions.txt` file  
✅ 2-minute countdown timer with auto-submission  
✅ "Next" and "Back" buttons to navigate between questions  
✅ Score displayed upon submission  
✅ Clean, intuitive Java Swing GUI

---

## 🛠 Technologies Used

- Java
- Java Swing (GUI)
- File I/O (for question loading)
- Java Timer (`java.util.Timer`)

---

## 📂 Project Structure

OnlineExamApp/
├── OnlineExamApp.java # Main application file
├── Question.java # Question model class (inside OnlineExamApp.java or separate)
├── questions.txt # Questions & options file
├── manifest.txt # Manifest for JAR creation
├── README.md # This file

---

## 📄 `questions.txt` Format

Each question follows this format:
