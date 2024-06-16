<div align="left">
  <h1>Roll Attender🙋 - Android App</h1>
  <a href="https://play.google.com/store/apps/details?id=com.apt.attendancecall">
    <img alt="Play Store" src="https://img.shields.io/badge/Google_Play-34A853?style=for-the-badge&logo=google-play&logoColor=white" style="margin-left: 10px;">
  </a>
  <img alt="Android" src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" style="margin-left: 10px;">
  <img alt="Java" src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white" style="margin-left: 10px;">
  <img alt="Firebase" src="https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black" style="margin-left: 10px;">
  <a href="https://github.com/JayeshPatil18/Roll-Attender">
    <img alt="GitHub" src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white" style="margin-left: 10px;">
  </a>
</div>
</br>

Welcome to **Roll Attender**, an Android app designed for teachers to efficiently manage student attendance records. This app simplifies the process of recording attendance subject-wise and date-wise, ensuring accurate tracking without manual errors.

</br>

*Available on Play Store.* [Click here](https://play.google.com/store/apps/details?id=com.apt.attendancecall)

![Available](https://github.com/JayeshPatil18/Roll-Attender/blob/master/roll-attender.png)

## Description

**Roll Attender** allows teachers to maintain detailed attendance records of students using a simple and intuitive interface. It supports quick attendance marking, automatic marking of present students based on predefined roll numbers, and provides a dashboard for overviewing attendance statistics.

## Features

- **Maintain Student Attendance**: Record attendance subject-wise and date-wise.
- **Roll Number Management**: Add and remove student roll numbers as required.
- **Quick Attendance**: Easily mark absent students, automatically marking others as present.
- **Dashboard for Overview**: Provides an overview of attendance statistics.
- **Student View**: Students can view their attendance once allowed by the teacher.


## Technology Used

- **Android**: For a native mobile application.
- **Java**: For application logic and development.
- **XML**: For designing user interfaces.
- **Firebase**: Ensuring robust and scalable backend services including authentication, database, and cloud storage.


### Key Components:

- **`build.gradle`**: Configuration file for Gradle build system.
- **`settings.gradle`**: Settings file that includes all modules in the project.
- **`AndroidManifest.xml`**: Describes the fundamental characteristics of the app and defines each of its components.
- **`java/`**: Contains Java (or Kotlin) source code for the app.
- **`res/`**: Contains resources used by the app such as images, layouts, strings, and styles.
- **`drawable/`**: Stores drawable resources like images and XML drawables.
- **`layout/`**: Contains XML files that define the layout structure of activities and fragments.
- **`mipmap/`**: Contains launcher icons for different densities.
- **`values/`**: Contains XML files for various values such as strings, styles, dimensions, colors, and more.
- **`test/`**: Contains test code for the app, typically used for unit tests.

This structure provides a basic overview of how an Android project is organized, with directories for source code, resources, tests, and configuration files. Adjustments can be made based on specific project requirements or additional modules included in the project.


## Getting Started

To get started with **MSBTE Aspirants**, follow these steps:

1. **Clone the repository**:
   ```bash
   git clone https://github.com/JayeshPatil18/Roll-Attender.git
   
## Install Android Studio

Download and install [Android Studio](https://developer.android.com/studio). Follow the installation instructions for your operating system.

## Set up Android SDK

1. Open Android Studio.
2. Navigate to `File > Settings > Appearance & Behavior > System Settings > Android SDK`.
3. **SDK Platforms tab:**
   - Ensure that `Android 10 (Q)` is checked or installed.
   
4. **SDK Tools tab:**
   - Install the latest version of `Android SDK Build-Tools`.

## Set up Android SDK

1. Open Android Studio.
2. Go to `File > Settings > Appearance & Behavior > System Settings > Android SDK`.
3. **SDK Platforms tab**:
   - Ensure that `Android 10 (Q)` is checked or installed.

4. **SDK Tools tab**:
   - Install the latest version of `Android SDK Build-Tools`.

## Install Java Development Kit (JDK)

Ensure you have JDK 8 or higher installed. You can download it from [Oracle's website](https://www.oracle.com/java/technologies/javase-downloads.html).


## Install Dependencies

Open a terminal in the project directory and run:

```bash
./gradlew build
```

## Connect Your Device

### For Physical Device

1. Enable `Developer Options` and `USB Debugging` on your Android device.
   - Go to `Settings` > `About phone`.
   - Tap on `Build number` multiple times until it says you are now a developer.
   - Go back to `Settings` > `Developer options`.
   - Enable `USB debugging`.

2. Connect your device to your computer via USB.

### For Virtual Device

1. Open Android Studio.
2. Go to `Tools` > `AVD Manager`.
3. Click on `Create Virtual Device`.
4. Choose a hardware profile and click `Next`.
5. Select a system image (Android version) and click `Next`.
6. Customize the virtual device configuration as needed (e.g., RAM size, VM heap size) and click `Finish`.
7. Start the virtual device by clicking the `Play` button next to the device name in the AVD Manager.
