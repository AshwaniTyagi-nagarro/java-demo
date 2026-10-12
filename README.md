# Amazon.in Selenium Automation Framework

## Overview
A Selenium WebDriver automation framework built with Java, Maven, and TestNG
following the Page Object Model (POM) design pattern.

## Prerequisites
- Java JDK 11 or 17
- Apache Maven 3.6+
- Eclipse IDE (or IntelliJ IDEA)
- Google Chrome, Mozilla Firefox, Microsoft Edge (latest versions)
- Internet connection (WebDriverManager downloads drivers automatically)

## Installation Steps

### 1. Clone/Import the Project
- In Eclipse: File → Import → Existing Maven Projects → Browse to project folder

### 2. Install Dependencies
Right-click project → Maven → Update Project → Check "Force Update" → OK
Maven will automatically download all required libraries.

### 3. Verify Setup
Check that the following exist:
- src/test/resources/config.properties
- src/test/resources/testdata.properties
- testng.xml at project root

## Configuration

### Change Browser
Edit `src/test/resources/config.properties`: