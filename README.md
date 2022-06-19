<!--
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->

<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#account-access">Account Access</a></li>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#launch">Launch</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#testing">Testing</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>

<br>

<!-- ABOUT THE PROJECT -->
## About The Project
This Project is created for a client who wants a web application that makes managing and viewing communication between the Kick-In association and other associations easier. 

<br>

### Built With
For our project we used multiple frameworks and libraries that are mentioned and linked below.  

* [JQuery](https://jquery.com)
* [Apache Tomcat](https://tomcat.apache.org)
* [OAuth2](https://oauth.net/2/)
* [DataTables](https://datatables.net)
* [Jersey](https://eclipse-ee4j.github.io/jersey/)
* [Gson](https://github.com/google/gson)
* [Bootstrap](https://getbootstrap.com)

<br>

<!-- GETTING STARTED -->
## Getting Started
To use our project few things are needed. Mentioned below are the necessary things to run it. 

### Account Access
To use our application ones credentials are needed to log-in. Therefore, we provided two sample accounts as a kick-in member and an association member to view our application.

*  Kick-In member
>Email: kigexo3722@bbsaili.com <br/>
>Password: admin1234

*  Association member
>Email: payitod897@d4wan.com <br/>
>Password: admin1234


### Prerequisites
A functional browser e.g. google chrome to run our web application on. If a 502 error is prompted, make sure to go to Clear Browsing Data -> Advanced -> clear image and files cache. It is essential for Chrome to work, so that Selenium automated testing works!

### Launch

1. Choose which credentials to use

2. Go to [LINK](http://kick-in-5.paas.hosted-by-previder.com/kickInProject/)

3. Login with chosen credentials

4. Explore

<br>

<!-- USAGE EXAMPLES -->
## Usage
### <b>Using search filters</b>

Our application has great filtering capabilities for searching emails and documents

- keywords
  - Typing in keywords in the search bar searches throuhgout emails and documents

- dates
  - Chosing start and end dates limits displayed emails depending on which time frame you want

- receiver/sender 
  - Entering specific email addresses to find their or your emails

- has attachments
  - Limits results to only show emails with an attachment

- organisations (kick-in only)
  - Allows kick-in members to look for emails from specific organisation through the usage of a drop down selection or entering in the name with an auto-complete option


![](https://git.snt.utwente.nl/s2478412/kickinproject/-/raw/master/Readme_screenshots/mainKickIn.png)
![](https://git.snt.utwente.nl/s2478412/kickinproject/-/raw/master/Readme_screenshots/mainAss.png)


### <b>Accessing emails and documents</b>

The emails can be easily accessed by just clicking on the email on the main page. The attachments can also be accessed from the main page and also when inside the email preview. 

![](https://git.snt.utwente.nl/s2478412/kickinproject/-/raw/master/Readme_screenshots/emailAttMain.PNG)
![](https://git.snt.utwente.nl/s2478412/kickinproject/-/raw/master/Readme_screenshots/emailPreview.PNG)
![](https://git.snt.utwente.nl/s2478412/kickinproject/-/raw/master/Readme_screenshots/attachmentInside.PNG)

### <b>How to check who has read something (Kick-In only)</b>

Kick-In members can check which association users have read the emails and documents. To check who has read an email the user can go to the email preview page and select the eye icon. This will open the table with the users who have accessed the email. To check who has read a document, go to the wanted document and preview it. To the right of the preview select the eye icon for the same type of table.

![](https://git.snt.utwente.nl/s2478412/kickinproject/-/raw/master/Readme_screenshots/readMeMail.PNG)
![](https://git.snt.utwente.nl/s2478412/kickinproject/-/raw/master/Readme_screenshots/readMeAtt.PNG)


### <b>Adding new organisations, people or emails (Kick-In only)</b>
Kick-In members can add new organisations, people or emails with attachments to the database when (DIRECTION to THAT part)
- organsiations 
  - add new organisations to the database by entering its name

![](https://git.snt.utwente.nl/s2478412/kickinproject/-/raw/master/Readme_screenshots/Add_organisation.png)

- people
  - select organisation from drop-down, enter their name and their email address

  ![](https://git.snt.utwente.nl/s2478412/kickinproject/-/raw/master/Readme_screenshots/Add_person.png)
- email
  - select receiving organisation or receiver from drop down, select date, enter subject, sender name and email address details. Type in the wanted content and choose an attachment if wanted.
  
  ![](https://git.snt.utwente.nl/s2478412/kickinproject/-/raw/master/Readme_screenshots/Add_email.png)
  ![](https://git.snt.utwente.nl/s2478412/kickinproject/-/raw/master/Readme_screenshots/Add_email_person.png)


<br>

<!-- TESTING -->
## Testing
To test our system we used JUnit for unit testing and Selenium for automated integration testing. The tests can be found in the Test folder in src/main. They can easily be executed by just running them in Eclipse. The Tomcat server needs to be started as well. For Selenium, the appropriate results of the tests will be shown in the console, while the software runs through the website. For proper unit testing, the user should run the tests before inputing new content! 
For more information: [Testing Report](https://docs.google.com/document/d/1h4qNrV_POputReaKtxUVZL5QL9vrEEfOHYDowAR4uOE/edit?usp=sharing)

NOTE about Selenium: 
The Selenium test runs on chrome driver, but it may not work sometimes, because Previder has problems with Chrome, giving 502 error.
If there are errors when trying to run the test, this means some files need to be added to the project. To do that, a user needs to download all files from directory Selenium_docs in the repository. Then right-click on the project and go to Properties (bottom). From there select Java Build Path and go to libraries where upon clicking on Classpath, adding external JARs is possible. Add all the files, EXCEPT the one in ChromeDriver folder, and click Apply. There may be duplipate jars with errors, delete them. The last change that needs to be done is to change the chromeDriverPath field in the IntegrationTestingSelenium.java class to the actual download path of the chromedriver that the user used. Example: C:\\path\\chromedriver.exe
<br>

<!-- CONTACT -->
## Contact
Kick-In Project Team 6 

Contact Form - [Google Form](https://forms.gle/diKLMJuZHWjWnidd7
)


Trello Board: [https://trello.com/b/xhWAwxfU/2021-m4-project-kick-in-team-6](https://trello.com/b/xhWAwxfU/2021-m4-project-kick-in-team-6)


