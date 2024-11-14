# 📚 Library Management System

Welcome to the **Library Management System**! This project provides a comprehensive solution for managing a library, offering both a **user-friendly interface** for patrons and **administrative tools** for library staff. Users can browse and view books, manage their profiles, and access issued books, while admins can perform tasks like adding books, issuing and returning books, and managing fines for overdue items. This project also integrates **OneSignal** for notifications to keep users updated on new books, due dates, and other important events.

![download](https://github.com/user-attachments/assets/2d46f54c-b870-4a8a-998c-e91294af4de0)(https://github.com/HafizWaqas-me/LibraryManagementSystem/releases/download/FinalRelease/Library.Management.System.apk)


## 📝 Table of Contents
1. [Features](#features)
2. [Notifications](#notifications)
3. [Screenshots & Demo](#screenshots--demo)
4. [Installation](#installation)
5. [Usage](#usage)
6. [Project Structure](#project-structure)
7. [Tech Stack](#tech-stack)
8. [Contributing](#contributing)
9. [License](#license)

---
## ✨ Features

### User Dashboard
- **View Issued Books**: Users can see a list of books currently issued to them.
- **Explore All Books**: View a comprehensive list of all books in the library.
- **Search**: Find specific books by title or author.
- **Profile Management**: Users can view and update their personal information.

### Admin Panel
- **Add New Book**: Admins can add books with full details.
- **Issue & Reissue Books**: Seamlessly issue or reissue books to users.
- **Return Books**: Manage book returns efficiently.
- **Fine Management**: Track overdue books and calculate fines for late returns.

---

## 🔔 Notifications

This Library Management System leverages **OneSignal** for real-time notifications to keep users updated on key events. Notifications are customized based on user roles and relevance.

### Notification Features
- **New Book Notifications**: All users receive notifications whenever a new book is added to the library.
- **Issued Book Notifications**: Only the specific user who has a book issued to them will receive the notification for that transaction, ensuring relevance.
- **Custom Notification System**: This feature allows for targeted notifications, reducing unnecessary alerts for unrelated users.
  
### Technical Implementation
- **OneSignal REST API & OkHttp3 SDK**: The project uses OneSignal's REST API to send notifications. With OkHttp3 SDK, notifications are triggered and sent through the OneSignal server, utilizing REST API keys for secure and efficient message delivery.

---

## 📷 Screenshots & Demo

### Screenshots
| Feature         | Screenshot                                  |
<table>
  <tr>
    <td align="center">
      <strong>Admin Control Panel</strong><br>
      <img src="https://github.com/user-attachments/assets/ed57aba7-77ac-4631-8759-42ec97d86db5" alt="Admin Control Panel" width="200">
    </td>
    <td align="center">
      <strong>Add New Book Screen</strong><br>
      <img src="https://github.com/user-attachments/assets/22d0bc6d-6e49-4679-a7d2-37ab8b3661be" alt="Add New Book Screen" width="200">
    </td>
    <td align="center">
      <strong>User Profile</strong><br>
      <img src="https://github.com/user-attachments/assets/a177ed28-b32e-4e60-9d43-e7b7accc8a12" alt="User Profile" width="200">
    </td>
  </tr>
  <tr>
    <td align="center">
      <strong>User Dashboard with Issued Books</strong><br>
      <img src="https://github.com/user-attachments/assets/cebd652b-24bb-478f-a70e-c4afda2ec8c1" alt="User Dashboard" width="200">
    </td>
    <td align="center">
      <strong>Return Book Screen</strong><br>
      <img src="https://github.com/user-attachments/assets/673f7969-b72a-46e2-874c-bea4f2c6f852" alt="Return Book Screen" width="200">
    </td>
    <td align="center">
      <strong>Re-Issue Book to User</strong><br>
      <img src="https://github.com/user-attachments/assets/a37c47e1-962a-452f-aa0c-1e438c8777e3" alt="Re-Issue Book" width="200">
    </td>
  </tr>
  <tr>
    <td align="center">
      <strong>Notification Tray</strong><br>
      <img src="https://github.com/user-attachments/assets/56bd6861-6d9e-4634-b733-7ff3fa725abd" alt="Notification Tray" width="200">
    </td>
    <td align="center">
      <strong>New Book Notification</strong><br>
      <img src="https://github.com/user-attachments/assets/d3ab2572-3ee6-4723-ab77-76626c198ccd" alt="New Book Notification" width="200">
    </td>
    <td align="center">
      <strong>Issued Books List for Admin</strong><br>
      <img src="https://github.com/user-attachments/assets/88b4ecee-95c2-4342-8949-c4e3d6b8919d" alt="Issued Books List" width="200">
    </td>
  </tr>
  <tr>
    <td align="center">
      <strong>Issue Book to User</strong><br>
      <img src="https://github.com/user-attachments/assets/c480d3c6-33d4-4409-9ea1-568c5e12581a" alt="Issue Book" width="200">
    </td>
    <td align="center">
      <strong>Heads-Up Notification</strong><br>
      <img src="https://github.com/user-attachments/assets/95315855-6e48-4420-b75e-5c4340c49234" alt="Heads-Up Notification" width="200">
    </td>
    <td align="center">
      <strong>Check Fine if Book Expired</strong><br>
      <img src="https://github.com/user-attachments/assets/1ce3d2d3-4849-44c2-89a2-72690bb4db35" alt="Check Fine" width="200">
    </td>
  </tr>
  <tr>
    <td align="center">
      <strong>All Books in Library</strong><br>
      <img src="https://github.com/user-attachments/assets/23bb6a1c-2b7b-4a0d-badd-f335faa39dd3" alt="All Books" width="200">
    </td>
  </tr>
</table>

### Demo Video
A quick demo of the app is available [here](path/to/demo_video.mp4).

---

## 🚀 Installation

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/yourusername/library-management-system.git
   cd library-management-system
   ```

2. **Setup Firebase**:
   - Configure Firebase as the database backend.
   - Update the `google-services.json` file with your Firebase credentials.

3. **Setup OneSignal**:
   - Sign up on [OneSignal](https://onesignal.com/) and create a new app for your project.
   - Obtain your **OneSignal App ID** and **REST API Key**.
   - Update your project’s configuration with the App ID and ensure proper integration for notifications.

4. **Install Dependencies**:
   Open the project in Android Studio and sync Gradle files to ensure all dependencies are installed.

---

## 📖 Usage

### User Guide
- **Dashboard**: Access your issued books and explore the library’s collection.
- **Book Search**: Use the search bar to filter books by title or author.
- **Profile**: Navigate to the profile section to view or update personal information.

### Admin Guide
- **Add Book**: Use the "Add Book" option in the Admin Panel to add new books.
- **Issue Book**: Find a book, then select "Issue" to assign it to a user.
- **Return Book**: Mark books as returned when they are handed back.
- **Fine Management**: Track overdue books and manage fines for expired loans.

---

## 🛠️ Tech Stack
- **Frontend**: Android (Kotlin), XML
- **Authentication**: Firebase Authentication
- **Backend**: Firebase Realtime Database
- **Notifications**: OneSignal with REST API integration using OkHttp3 SDK
- **Architecture**: MVVM (Model-View-ViewModel)
- **Testing**: MockK for Unit Testing, Espresso for UI Testing

---

## 🤝 Contributing

We welcome contributions! Please follow these steps:
1. Fork the repository.
2. Create a new branch (`feature/your-feature-name`).
3. Commit changes and push to your branch.
4. Open a pull request for review.

---

## 📜 License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.

---

**Thank you for visiting the Library Management System!** If you have any questions, feel free to reach out at hafizwaqas.me@gmail.com
