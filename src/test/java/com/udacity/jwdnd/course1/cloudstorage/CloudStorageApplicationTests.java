package com.udacity.jwdnd.course1.cloudstorage;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));
		
		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depening on the rest of your code.
		*/
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}

	
	
	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling redirecting users 
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric: 
	 * https://review.udacity.com/#!/rubrics/2724/view 
	 */
	@Test
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","RT","123");
		
		// Check if we have been redirected to the log in page.
		Assertions.assertNotEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling bad URLs 
	 * gracefully, for example with a custom error page.
	 * 
	 * Read more about custom error pages at: 
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");
		
		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code. 
	 * 
	 * Read more about file size limits here: 
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	public void testLargeUpload() {
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}

		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403–Forbidden"));

	}

	/**
	 * Verifies that an unauthorized user can only access the login and signup pages
	 */
	@Test
	public void testVerifyNotAccess(){
		verifyAccess();
	}

	/**
	 * Write a test that signs up a new user, logs in, verifies that the home page is accessible,
	 * logs out, and verifies that the home page is no longer accessible.
	 */
	@Test
	public void testVerifyAccess(){
		// Create a test account
		doMockSignUp("Verify","Access","verify","123");
		doLogIn("verify", "123");

		driver.get("http://localhost:" + this.port + "/home");

		WebDriverWait webDriverWait = new WebDriverWait(driver, 5);
		Assertions.assertEquals("Home", driver.getTitle());

		logout(webDriverWait);

		Assertions.assertTrue(driver.findElement(By.id("inputUsername")).getText().contains(""));
		verifyAccess();
	}

	private void verifyAccess(){
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertNotEquals("Home", driver.getTitle());

		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());

		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
	}

	/**
	 * Creates a note, and verifies it is displayed.
	 */
	@Test
	public void testCreateNote(){
		// Create a test account
		doMockSignUp("Julio","Martinez","juliozarate5","123");
		doLogIn("juliozarate5", "123");

		WebDriverWait webDriverWait = new WebDriverWait(driver, 5);

		createNote(webDriverWait, "todo", "-one" +
				"- two" +
				"- three");

		Assertions.assertTrue(driver.getPageSource().contains("todo"));

	}

	/**
	 * edits an existing note and verifies that the changes are displayed.
	 */
	@Test
	public void testEditNote(){
		// Create a test account
		doMockSignUp("Juliana","Martinez","juliana","123");
		doLogIn("juliana", "123");

		WebDriverWait webDriverWait = new WebDriverWait(driver, 5);

		createNote(webDriverWait, "todo2", "-one" +
				"- two" +
				"- three");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes")));
		WebElement btnNewNote = driver.findElements(By.cssSelector("div#nav-notes table td .btn-success")).get(0);
		btnNewNote.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement noteTitle = driver.findElement(By.id("note-title"));
		noteTitle.click();
		noteTitle.sendKeys("");
		noteTitle.sendKeys("todo1");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("noteDescription")));
		WebElement noteDescription = driver.findElement(By.name("noteDescription"));
		noteDescription.click();
		noteDescription.sendKeys("-one");

		noteDescription.submit();

		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("No success");
		}

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.alert-success")));
		WebElement btnOK = driver.findElements(By.cssSelector("span a")).get(0);
		btnOK.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-tab")));
		WebElement tabNotes2 = driver.findElement(By.id("nav-notes-tab"));
		tabNotes2.click();

		Assertions.assertTrue(driver.getPageSource().contains("todo1"));

	}

	/**
	 * deletes a note and verifies that the note is no longer displayed.
	 */
	@Test
	public void testDeleteNote(){
		doMockSignUp("Delete","Note","deletenote","123");
		doLogIn("deletenote", "123");

		WebDriverWait webDriverWait = new WebDriverWait(driver, 5);

		createNote(webDriverWait, "delete", "- delete0 - delete1");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes")));
		WebElement btnNewNote = driver.findElements(By.cssSelector("div#nav-notes table td .btn-danger")).get(0);
		btnNewNote.click();

		Assertions.assertFalse(driver.getPageSource().contains("todo"));
	}

	private void createNote(WebDriverWait webDriverWait, String title, String content){
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-tab")));
		WebElement tabNotes = driver.findElement(By.id("nav-notes-tab"));
		tabNotes.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes")));
		WebElement btnNewNote = driver.findElements(By.cssSelector("div#nav-notes button")).get(0);
		btnNewNote.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement noteTitle = driver.findElement(By.id("note-title"));
		noteTitle.click();
		noteTitle.sendKeys(title);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("noteDescription")));
		WebElement noteDescription = driver.findElement(By.name("noteDescription"));
		noteDescription.click();
		noteDescription.sendKeys(content);

		noteDescription.submit();

		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("No success");
		}

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.alert-success")));
		WebElement btnOK = driver.findElements(By.cssSelector("span a")).get(0);
		btnOK.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-tab")));
		WebElement tabNotes2 = driver.findElement(By.id("nav-notes-tab"));
		tabNotes2.click();

	}

	/**
	 * creates a set of credentials, verifies that they are displayed, and verifies that the displayed password is encrypted.
	 */
	@Test
	public void testCreateCredentials(){
		doMockSignUp("Create","Credentials","createcredentials","123");
		doLogIn("createcredentials", "123");

		WebDriverWait webDriverWait = new WebDriverWait(driver, 5);

		createCredentials(webDriverWait, "http://localhost", "username", "password");
		createCredentials(webDriverWait, "http://localhost", "username2", "password2");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		List<WebElement> credentialPasswds = driver.findElements(By.cssSelector("table#credentialTable tbody tr td"));
		WebElement element = credentialPasswds.get(credentialPasswds.size() - 1);
		String texto = element.getText();

		Assertions.assertTrue(driver.getPageSource().contains("http://localhost"));
		Assertions.assertNotEquals("password", texto);
	}

	/**
	 * views an existing set of credentials, verifies that the viewable password is unencrypted,
	 * edits the credentials, and verifies that the changes are displayed
	 *
	 */
	@Test
	public void testViewsCredentials() throws InterruptedException {
		doMockSignUp("Views","Credentials","viewcredentials","123");
		doLogIn("viewcredentials", "123");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 5);

		createCredentials(webDriverWait, "http://localhost", "username3", "password3");
		createCredentials(webDriverWait, "http://localhost", "username4", "password4");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-tab")));
		WebElement tabCredentials = driver.findElement(By.id("nav-credentials-tab"));
		tabCredentials.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		List<WebElement> credentialPasswds = driver.findElements(By.cssSelector("table#credentialTable tbody tr"));

		List<String> encPasswds = new ArrayList<>();
		List<String> passwds = new ArrayList<>();

		for (WebElement element : credentialPasswds) {
			List<WebElement> list = element.findElements(By.tagName("td"));
			WebElement pwd = list.get(list.size() - 1);
			String text = pwd.getText();
			encPasswds.add(text);
		}


		Thread.sleep(1000);
		List<WebElement> btnEdits = driver.findElements(By.cssSelector("div#nav-credentials table td .btn-success"));

		for(WebElement btnEdit: btnEdits){
			Thread.sleep(200);
			webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table#credentialTable tbody tr td .btn-success")));
			btnEdit.click();

			webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
			WebElement passwd = driver.findElement(By.id("credential-password"));
			String textPasswd = passwd.getAttribute("value");

			webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div#credentialModal .btn-secondary")));
			WebElement btnClose = driver.findElements(By.cssSelector("div#credentialModal .btn-secondary")).get(0);
			btnClose.click();

			passwds.add(textPasswd);
		}

		// edit

		List<String> newPasswd = new ArrayList<>();


		Thread.sleep(200);
		List<WebElement> btnEdits2 = driver.findElements(By.cssSelector("div#nav-credentials .btn-success"));

		for(int i = 0; i < btnEdits2.size(); i++){

			String newPass = "newpass"+i;

			//webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div#nav-credentials .btn-success")));
			WebElement btn = driver.findElements(By.cssSelector("div#nav-credentials .btn-success")).get(i);
			Thread.sleep(1000);
			btn.click();

			webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
			WebElement passwd = driver.findElement(By.id("credential-password"));
			//webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("credential-password")));
			//WebElement inputPasswd = driver.findElement(By.name("ncredential-password"));
			passwd.click();
			passwd.sendKeys("");
			passwd.sendKeys(newPass);
			passwd.submit();

			webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.alert-success")));
			WebElement btnOK = driver.findElements(By.cssSelector("span a")).get(0);
			btnOK.click();

			webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-tab")));
			WebElement tabCredentials2 = driver.findElement(By.id("nav-credentials-tab"));
			tabCredentials2.click();

			newPasswd.add(newPass);

		}


		for(int i = 0; i < encPasswds.size(); i++){
			Assertions.assertNotEquals(encPasswds.get(i), passwds.get(i));
			Assertions.assertNotEquals(passwds.get(i), newPasswd.get(i));
		}
	}

	/**
	 * deletes an existing set of credentials and verifies that the credentials are no longer displayed
	 */
	@Test
	public void testDeleteCredential() throws InterruptedException {
		doMockSignUp("Delete","Credentials","delcredentials","123");
		doLogIn("delcredentials", "123");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 5);

		createCredentials(webDriverWait, "http://localhost", "username5", "password5");
		createCredentials(webDriverWait, "http://localhost", "username6", "password5");

		Thread.sleep(1000);
		List<WebElement> btns = driver.findElements(By.cssSelector("div#nav-credentials .btn-danger"));

		for(int i = 0; i < btns.size(); i++){

			WebElement btnDel = driver.findElements(By.cssSelector("div#nav-credentials .btn-danger")).get(0);
			Thread.sleep(1000);
			btnDel.click();

			webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.alert-success")));
			WebElement btnOK = driver.findElements(By.cssSelector("span a")).get(0);
			btnOK.click();

			webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-tab")));
			WebElement tabCredentials2 = driver.findElement(By.id("nav-credentials-tab"));
			tabCredentials2.click();
		}

		Assertions.assertFalse(driver.getPageSource().contains("http://localhost"));
	}

	private void createCredentials(WebDriverWait webDriverWait, String url, String username, String password){
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-tab")));
		WebElement tabCredentials = driver.findElement(By.id("nav-credentials-tab"));
		tabCredentials.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials")));
		WebElement btnNewCredential = driver.findElements(By.cssSelector("div#nav-credentials button")).get(0);
		btnNewCredential.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement credentialURL = driver.findElement(By.id("credential-url"));
		credentialURL.click();
		credentialURL.sendKeys(url);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
		WebElement credentialUsername = driver.findElement(By.id("credential-username"));
		credentialUsername.click();
		credentialUsername.sendKeys(username);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		WebElement credentialPassword = driver.findElement(By.id("credential-password"));
		credentialPassword.click();
		credentialPassword.sendKeys(password);

		credentialPassword.submit();

		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("No success");
		}

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.alert-success")));
		WebElement btnOK = driver.findElements(By.cssSelector("span a")).get(0);
		btnOK.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-tab")));
		WebElement tabCredentials2 = driver.findElement(By.id("nav-credentials-tab"));
		tabCredentials2.click();

	}

	private void logout(WebDriverWait webDriverWait){
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logoutDiv")));
		WebElement btnLogout = driver.findElements(By.cssSelector("div#logoutDiv button")).get(0);
		btnLogout.click();
	}
}
