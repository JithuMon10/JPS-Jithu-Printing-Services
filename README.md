<div align="center">

# 📱 JPS Studio
### Jithu Printer Service Manager

**A modern, offline-first Android application designed to streamline printing order management.**

![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-purple?style=for-the-badge&logo=kotlin)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-UI-4285F4?style=for-the-badge&logo=android)
![Platform](https://img.shields.io/badge/Platform-Android-green?style=for-the-badge&logo=android)
![License](https://img.shields.io/badge/License-Personal-orange?style=for-the-badge)

</div>

---

## 📖 Overview

**JPS Studio** is a single-user productivity tool built for small printing businesses, freelance printing services, and college print shops. It replaces manual logbooks with a sleek, digital dashboard to track orders, payments, and completion statuses.

Built entirely with **Kotlin** and **Jetpack Compose**, the app focuses on a clean UI, smooth animations, and secure local data handling.

---

## 📸 Screenshots

| Dashboard (Light) | Order Details | Dark Mode | Order Status |
|:---:|:---:|:---:|:---:|
| ![Dashboard] | ![Details] | ![Dark Mode] | ![Status] |
| *(Add Image)* | *(Add Image)* | *(Add Image)* | *(Add Image)* |

---

## ✨ Key Features

### 📋 Order Management
- **Smart Entry:** Add new printing orders with comprehensive details.
- **Auto-Timestamp:** Automatically records the order creation date & time.
- **Due Dates:** Built-in calendar picker for setting deadlines.

### 🧠 Intelligent Status Tracking
The app uses a smart visual system to track the lifecycle of an order:
- 🔴 **Pending:** Order received, work not started.
- 🟡 **Amount Received:** Payment made, work pending.
- 🟠 **Unpaid Completed:** Work done, payment pending.
- 🟢 **Fully Completed:** Work done and payment settled.

### ⚙️ Productivity & UX
- **Auto-Sorting:** Newest orders appear at the top; completed orders move to the bottom.
- **Smart Search:** Compact, icon-based search to find orders instantly.
- **Theme Persistence:** Remembers your preference for **Light** or **Dark** mode.
- **Offline First:** No internet required; runs safely in the background.

### 🔐 Security
- **PIN Protection:** Dashboard is secured via a **6-digit PIN**.
- **Local Privacy:** All data is stored locally on the device. No cloud sync, no third-party access.

---

## 🛠 Tech Stack

* **Language:** [Kotlin](https://kotlinlang.org/)
* **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material Design 3)
* **Architecture:** MVVM / Single-User Local Storage
* **Navigation:** Jetpack Navigation Compose
* **Data Persistence:** Local Storage (Room/DataStore)

---

## 🎨 UI Highlights

* **Gradient Cards:** Visually distinct order cards for better readability.
* **Compact Top Bar:** Minimalist design with centered app branding.
* **Visual Feedback:** Color-coded states for instant status recognition.
* **Animations:** Smooth transitions and floating action button effects.

---

## 📌 Use Cases

This project is ideal for:
1.  **College Printing Shops:** Managing student project prints.
2.  **Freelancers:** Tracking client orders and payments.
3.  **Personal Business:** A digital replacement for paper logbooks.

---

## 👤 Developer

**Jithu (Jithendra V Anand)**
*Creator of JPS Studio*

> "Building efficient tools for everyday problems."

---

## 📄 License & Usage

**Current Status:** Personal & Educational Use Only.

This project is open for viewing for educational purposes. Commercial use, redistribution, or modification of the source code requires explicit permission from the author.

---

## ⭐ Support

If you find this project interesting or useful:
* ⭐ **Star this repository** to show your support!
* 🐞 **Report Bugs** via the Issues tab.
* 🛠 **Suggest Improvements** to help make JPS Studio better.

**Thank you for visiting JPS Studio! 🚀**
