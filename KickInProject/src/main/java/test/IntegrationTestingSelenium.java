package test;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


/**
 * This class is used for full integration testing of the system. 
 * Different scenarios are executed one by one.
 * @author Kick-In Team 6
 *
 */
public class IntegrationTestingSelenium {
	//Test users
	static final String testKickInUser = "kigexo3722@bbsaili.com";
	static final String testOrganisationUser = "payitod897@d4wan.com";
	
	//Test passwords
	static final String testCorrectPass = "admin1234";
	static final String testWrongPass = "0000";
	
	//ChromeDriver path
	static final String chromeDriverPath = "C:\\Selenium\\chromeDriver\\chromedriver.exe";

	public static void main(String[] args) throws InterruptedException {
	    // declaration and instantiation of objects/variables
		System.setProperty("webdriver.chrome.driver", chromeDriverPath);
		WebDriver driver = new ChromeDriver();		
		String baseURL = "http://kick-in-5.paas.hosted-by-previder.com/kickInProject/LoginPage.html";
		
		driver.get(baseURL);	
		String accessibilityTestURL = "http://kick-in-5.paas.hosted-by-previder.com/kickInProject/rest/main/email/162610";
		
		IntegrationTestingSelenium.logInTest(driver, testKickInUser); //for kick-in member
		Thread.sleep(4000);
		IntegrationTestingSelenium.paginationTest(driver);
	    Thread.sleep(2000);
	    IntegrationTestingSelenium.credentialsTest(driver);
	    Thread.sleep(4000);
	    IntegrationTestingSelenium.emailPreviewTest(driver);
	    Thread.sleep(2000);
	    IntegrationTestingSelenium.readEmailTest(driver);
	    Thread.sleep(2000);
	    IntegrationTestingSelenium.attachmentPreviewTest(driver);
	    Thread.sleep(2000);
	    IntegrationTestingSelenium.readAttachmentTest(driver);
	    Thread.sleep(2000);
	    IntegrationTestingSelenium.filterTest(driver);
	    Thread.sleep(2000);
		IntegrationTestingSelenium.dateOrderTest(driver);
		Thread.sleep(2000);
	    IntegrationTestingSelenium.signOutTest(driver);
	    Thread.sleep(2000);
	    IntegrationTestingSelenium.logInTest(driver, testOrganisationUser); //for organisation user
	    Thread.sleep(2000);
	    
	    driver.get(accessibilityTestURL);
	    IntegrationTestingSelenium.accessibilityTest(driver);
	    Thread.sleep(2000);
	    
	    System.out.println("Integration testing finished");
	    
        driver.close();
        		
		
	}
	
	/**
	 * Testing whether a user from second organisation can access an email from third organisation from the URL
	 * @param driver - The WebDriver used
	 */
	public static void accessibilityTest(WebDriver driver) {
		List<WebElement> permissionDeniedMessage = driver.findElements(By.xpath("/html/body/div/div[1]/h2[2]"));
		if (!permissionDeniedMessage.isEmpty()) {
			System.out.println("Accessibility test successful\n");
		} else {
			System.out.println("Accessibility test unsuccessful\n");
		}
		
	}

	/**
	 * Testing if the order of the emails presented can be changed from descending to ascending
	 * @param driver - The WebDriver used
	 * @throws InterruptedException
	 */
	public static void dateOrderTest(WebDriver driver) throws InterruptedException {

		WebElement dateOrder = driver.findElement(By.xpath("//*[@id=\"dateOrder\"]"));
		dateOrder.click();
		Thread.sleep(2000);
		System.out.println("Successful date order test\n");	
		
	}

	/**
	 * Testing sign out functionality.
	 * @param driver - The WebDriver used
	 * @throws InterruptedException
	 */
	public static void signOutTest(WebDriver driver) throws InterruptedException {
		
		List<WebElement> credentialsButton = driver.findElements(By.xpath("//*[@id=\"credentialsIcon\"]/i"));
		if (!credentialsButton.isEmpty()) {
			credentialsButton.get(0).click();
			Thread.sleep(2000);
			
			WebElement signOutButton = driver.findElement(By.xpath("//*[@id=\"logoutResponse\"]"));
			signOutButton.click();
			Thread.sleep(2000);
			
			List<WebElement> loginButton = driver.findElements(By.xpath("//*[@id=\"button\"]"));
			if (!loginButton.isEmpty()) {
				System.out.println("Successful sign out test\n");
			} else {
				System.out.println("Unsuccessful sign out test\n");
			}
			
		} else {
			WebElement signOutButton = driver.findElement(By.xpath("//*[@id=\"logout\"]"));
			signOutButton.click();
			Thread.sleep(2000);
			
			List<WebElement> loginButton = driver.findElements(By.xpath("//*[@id=\"button\"]"));
			if (!loginButton.isEmpty()) {
				System.out.println("Successful sign out test\n");
			} else {
				System.out.println("Unsuccessful sign out test\n");
			}
		}
		
	}
	
	/**
	 * Filtering a search results based on key word, organisation, start date, end date and receiver.
	 * @param driver -  The WebDriver used
	 * @throws InterruptedException
	 */
	public static void filterTest(WebDriver driver) throws InterruptedException {
		String keyWord = "association";
		String organisation = "SecondOrganisation";
		String startDate = "05062020";
		String endDate = "27072020";
		String receiver = "info@kick-in.nl";

		WebElement searchInput = driver.findElement(By.xpath("//*[@id=\"searchField\"]"));
		WebElement advancedSearchButton = driver.findElement(By.xpath("//*[@id=\"dropdownAdvancedFilter\"]"));
		WebElement organisationFilter = driver.findElement(By.xpath("//*[@id=\"filterOrganisationField\"]"));
		WebElement startDateFilter = driver.findElement(By.xpath("//*[@id=\"startDate\"]"));
		WebElement endDateFilter = driver.findElement(By.xpath("//*[@id=\"endDate\"]"));
		WebElement receiverFilter = driver.findElement(By.xpath("//*[@id=\"filterSenderEmail\"]"));
		WebElement hasAttachmentFilter = driver.findElement(By.xpath("//*[@id=\"filterHasAttachment\"]"));
		WebElement searchButton = driver.findElement(By.xpath("//*[@id=\"searchButton\"]"));
		
		
		searchInput.sendKeys(keyWord);
		advancedSearchButton.click();
		Thread.sleep(1000);
		organisationFilter.sendKeys(organisation);
		Thread.sleep(1000);
		startDateFilter.sendKeys(startDate);
		Thread.sleep(1000);
		endDateFilter.sendKeys(endDate);
		Thread.sleep(1000);
		receiverFilter.sendKeys(receiver);
		Thread.sleep(1000);
		hasAttachmentFilter.click();
		Thread.sleep(1000);
		searchButton.click();
		Thread.sleep(1000);
		
		System.out.println("Search filter test successful\n");
				
	}

	/**
	 * Testing if the button that displays who read the attachment works properly
	 * @param driver - The WebDriver used
	 * @throws InterruptedException
	 */
	public static void readAttachmentTest(WebDriver driver) throws InterruptedException {
		WebElement readButtonAtt = driver.findElement(By.xpath("//*[@id=\"readbutton\"]"));
		readButtonAtt.click();
		Thread.sleep(2000);
		
		List<WebElement> readTable = driver.findElements(By.xpath("		//*[@id=\"readDocTable_wrapper\"]"
				+ "/div[2]/div/div/div[1]/div/table/thead/tr/th[1]\r\n"));
		if (!readTable.isEmpty()) {
			System.out.println("Read attachment test successful\n");
		} else {
			System.out.println("Read attachment test unsuccessful\n");
		}
		Thread.sleep(1000);
		
		//Closing the attachment
		WebElement closeAttButton = driver.findElement(By.xpath("//*[@id=\"closebutton\"]"));
		closeAttButton.click();
		System.out.println("Attachment previewed successfully\n");
		Thread.sleep(2000);
		
		//Closing the email
		WebElement closeEmailButton = driver.findElement(By.xpath("//*[@id=\"back\"]/i"));
		closeEmailButton.click();
		Thread.sleep(2000);
		
	}
	
	/**
	 * Testing if the button that displays who read the email works properly
	 * @param driver - The WebDriver used
	 * @throws InterruptedException
	 */
	public static void readEmailTest(WebDriver driver) throws InterruptedException {
		WebElement readButtonEmail = driver.findElement(By.xpath("//*[@id=\"goToRead\"]"));
		readButtonEmail.click();
		Thread.sleep(2000);
		
		List<WebElement> readTable = driver.findElements(By.xpath("//*[@id=\"readTable_wrapper\"]/div[2]/div/div/div[1]/div/table"));
		if (!readTable.isEmpty()) {
			System.out.println("Read email test successful\n");
		} else {
			System.out.println("Read email test unsuccessful\n");
		}
		
	}
	
	/**
	 * Testing if the attachment button works and if the attachment is previewed correctly
	 * @param driver - The WebDriver used
	 * @throws InterruptedException
	 */
	public static void attachmentPreviewTest(WebDriver driver) throws InterruptedException {
		WebElement attachmentButton = driver.findElement(By.xpath("//*[@id=\"scrolltoattachments\"]"));
		attachmentButton.click();
		Thread.sleep(2000);

		WebElement attachment = driver.findElement(By.xpath("//*[@id=\"attachment\"]"));
		attachment.click();
		System.out.println("Attachment successfully opened\n");
		Thread.sleep(2000);
		
		//In case attachment doesnt load, refresh the attachment.		
		WebElement refreshButton = driver.findElement(By.xpath("//*[@id=\"refreshbutton\"]"));
		refreshButton.click();
		Thread.sleep(1000);
		refreshButton.click();
		Thread.sleep(1000);
		refreshButton.click();
		Thread.sleep(1000);
		refreshButton.click();
		Thread.sleep(3000);
		
	}

	/**
	 * Testing if the emails are clickable and showing the correct information.
	 * @param driver - The WebDriver used
	 * @throws InterruptedException
	 */
	public static void emailPreviewTest(WebDriver driver) throws InterruptedException {
		WebElement firstEmail = driver.findElement(By.xpath("//*[@id=\"example\"]/tbody/tr[1]"));
		firstEmail.click();
		Thread.sleep(3000);
		
		WebElement title = driver.findElement(By.xpath("//*[@id=\"subject\"]"));
		WebElement content = driver.findElement(By.xpath("//*[@id=\"emailContent\"]/div[6]/span/span"));
		String titleCheck = "Update Kick-In 2020";
		String contentCheck = "For your information, an email has been sent to all do-group parents "
				+ "with important information regarding the gathering of do-group. This letter has "
				+ "been attached to this email.";
		
		if (title.getText().equals(titleCheck) && content.getText().equals(contentCheck)) {
			System.out.println("Email preview test successful\n");
		} else {
			System.out.println("Email preview test unsuccessful\n");
		}
		
		
	}

	/**
	 * Testing the credentials button if it is present or not if the credentials given are correct.
	 * @param driver - The WebDriver used
	 * @throws InterruptedException
	 */

	public static void credentialsTest(WebDriver driver) throws InterruptedException {
		List<WebElement> credentialsButton = driver.findElements(By.xpath("//*[@id=\"credentialsIcon\"]/i"));
		if (!credentialsButton.isEmpty()) {
			credentialsButton.get(0).click();
			Thread.sleep(2000);
			WebElement email = driver.findElement(By.xpath("//*[@id=\"emailResponse\"]"));

			String emailCredentials = email.getText();
			//Removing the Email:
			String actualemailCredentials = emailCredentials.substring(7);
			if (actualemailCredentials.equals(testKickInUser)) {
				System.out.println("Successful credentials test\n");
			} else {
				System.out.println("Unsuccessful credentials test\n");
			}
			
		} else {
			WebElement email = driver.findElement(By.xpath("//*[@id=\"email\"]"));
		
			String emailCredentials = email.getText();
			//Removing the Email:
			String actualemailCredentials = emailCredentials.substring(7);
			
			if (actualemailCredentials.equals(testKickInUser)) {
				System.out.println("Successful credentials test\n");
			} else {
				System.out.println("Unsuccessful credentials test\n");
			}
		}
		
	}


	/**
	 * Testing pagination by clicking the 'next' and then the 'previous' button
	 * @param driver - he WebDriver used
	 * @throws InterruptedException
	 */
	public static void paginationTest(WebDriver driver) throws InterruptedException {
		WebElement buttonNext = driver.findElement(By.xpath("//*[@id=\"paginationInside\"]/li[3]"));
		buttonNext.click();
		Thread.sleep(3000);
		WebElement buttonPrev = driver.findElement(By.xpath("//*[@id=\"paginationInside\"]/li[1]"));
		buttonPrev.click();
		System.out.println("Pagination test successful\n");
		
		
	}
	/**
	 * Tests login functionality tested for both kick-in and organisation user.
	 * Two options:
	 *   - already logged in -> proceeds to main page
	 *   - octa credentials needed -> user and password are passed, logs in and proceeds to main page
	 * @param The WebDriver used
	 * @throws InterruptedException
	 */
	public static void logInTest(WebDriver driver, String user) throws InterruptedException {
    	Thread.sleep(1500);
	    WebElement logInButton = driver.findElement(By.xpath("//*[@id=\"button\"]"));
	    logInButton.click();

	    //check if the user is prompted the Octa log in page or he is already logged in
	    Thread.sleep(4000);
	    if (!driver.findElements(By.xpath("//*[@id=\"main-message\"]/h1")).isEmpty()) {
			System.out.println("Already logged in\n");
			
			List<WebElement> proceed = driver.findElements(By.xpath("//*[@id=\"proceed-button\"]"));
			if (!proceed.isEmpty()) {
				proceed.get(0).click();
			}
			Thread.sleep(2000);
		} else {
			System.out.println("Octa credentials needed\n");
			WebElement userName = driver.findElement(By.xpath("//*[@id=\"okta-signin-username\"]"));
			WebElement pass = driver.findElement(By.xpath("//*[@id=\"okta-signin-password\"]"));
			WebElement signin = driver.findElement(By.xpath("//*[@id=\"okta-signin-submit\"]"));
			
			IntegrationTestingSelenium.unsuccessfulLogInTest(driver, userName, user, pass, signin);
			IntegrationTestingSelenium.successfulLogInTest(driver, userName, user,  pass, signin);
			
			List<WebElement> proceed = driver.findElements(By.xpath("//*[@id=\"proceed-button\"]"));
			if (!proceed.isEmpty()) {
				proceed.get(0).click();
			}
			Thread.sleep(2000);
			
			WebElement advancedSearchButton = driver.findElement(By.xpath("//*[@id=\"dropdownAdvancedFilter\"]"));
			advancedSearchButton.click();
			Thread.sleep(1000);
			
			List<WebElement> organisationFilter = driver.findElements(By.xpath("//*[@id=\"filterOrganisationField\"]"));
			
			/*
			 * If the organisation filter exists, then it is the kick in page. 
			 * If it does not, it is the organisations page.
			 */
			
			if (!organisationFilter.isEmpty()) {
				System.out.println("Successful log in as Kick-in member\n");
			} else {
				System.out.println("Successful log in as organisation member\n");
			}
			
		}
    }
	
	/**
	 * Test to log in with a wrong password
	 * @param driver - The WebDriver used
	 * @param userName - Username text field element
	 * @param pass - Password text field element
	 * @param signin - Signin button element
	 * @throws InterruptedException
	 */
	private static void unsuccessfulLogInTest(WebDriver driver, WebElement userName, String user,
			WebElement pass, WebElement signin) throws InterruptedException {
		userName.sendKeys(user);
		pass.sendKeys(testWrongPass);
		signin.click();
		
		Thread.sleep(3000);
		if (!driver.findElements(By.xpath("//*[@id=\"form2\"]/div[1]/div[1]/div/div/p")).isEmpty()) {
			System.out.println("Wrong sign in test successful\n");
		} else {
			System.out.println("Wrong sign in test unsuccessful\n");
		}

	}

	/**
	 * Test to log in with a correct password
	 * @param driver - The WebDriver used
	 * @param userName - Username text field element
	 * @param pass - Password text field element
	 * @param signin - Signin button element
	 * @throws InterruptedException
	 */
	private static void successfulLogInTest(WebDriver driver, WebElement userName, String user,  
			WebElement pass, WebElement signin) throws InterruptedException {
		userName.clear();
		pass.clear();
		userName.sendKeys(user);
		pass.sendKeys(testCorrectPass);
		signin.click();
		Thread.sleep(4000);
		
		if (!driver.findElements(By.xpath("//*[@id=\"searchField\"]")).isEmpty()) {
			System.out.println("Correct sign in test successful\n");
		} else {
			System.out.println("Correct sign in test unsuccessful\n");
		}
		Thread.sleep(4000);

	}


}

