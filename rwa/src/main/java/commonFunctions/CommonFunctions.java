package commonFunctions;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
//import io.appium.java_client.android.AndroidDeviceActionShortcuts;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;

public class CommonFunctions {

	private String OS;
	private String AppPath;
	private String DeviceName;
	private String PlatformName;
	private String PlatformVersion;
	private String URL = "";

	private AppiumDriver<MobileElement> driver = null;

	public CommonFunctions() {
		try {
			Properties obj = new Properties();
			obj.load(this.getClass().getResourceAsStream("/Configs/Configuration.properties"));
			OS = obj.getProperty("OS");
			AppPath = obj.getProperty("AppPath");
			DeviceName = obj.getProperty("DeviceName");
			PlatformName = obj.getProperty("PlatformName");
			PlatformVersion = obj.getProperty("PlatformVersion");
			URL = obj.getProperty("URL");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * creates the driver object
	 */
	public AppiumDriver<MobileElement> startAppiumServer() {

		DesiredCapabilities capabilities = new DesiredCapabilities();

		capabilities.setCapability("deviceName", DeviceName);
		capabilities.setCapability("platformName", PlatformName);
		capabilities.setCapability("platformVersion", PlatformVersion);
		capabilities.setCapability("app", AppPath);
		capabilities.setCapability(MobileCapabilityType.FULL_RESET, false);
		capabilities.setCapability(MobileCapabilityType.NO_RESET, true);

		try {
			driver = new AndroidDriver<MobileElement>(new URL(URL), capabilities);
			driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
		} catch (MalformedURLException e) {

			e.printStackTrace();
		}
		return driver;

	}

	/**
	 * returns the driver object to be used across this class
	 * 
	 */
	public AndroidDriver<MobileElement> getDriver() {
		return (AndroidDriver<MobileElement>) driver;
	}

	/**
	 * returns the text attribute of an object
	 */
	public String getText(By identifier) {
		waitForVisibilityOfElement(identifier);
		String userNameText = driver.findElement(identifier).getAttribute("text");
		return userNameText;
	}

	public String getValue(By identifier) {
		waitForVisibilityOfElement(identifier);
		String userNameText = driver.findElement(identifier).getAttribute("value");
		return userNameText;
	}

	public String getContent(By identifier) {
		waitForVisibilityOfElement(identifier);
		String text = driver.findElement(identifier).getText();
		return text;
	}

	/**
	 * returns the web element
	 */
	public MobileElement retrieveElement(By identifier) {
		return driver.findElement(identifier);
	}

	/**
	 * Write Text in Text box
	 */
	public void writeText(By identifier, String text) {
		driver.findElement(identifier).sendKeys(text);
	}

	/**
	 * Clear Text in Text Box
	 */
	public void clearTextBox(By identifier) {
		driver.findElement(identifier).clear();
	}

	/**
	 * Scrolls the Text
	 */
	public void scroll(String text) {
		// dr.scrollTo(text);

	}

	public void swipe() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		int startx = driver.manage().window().getSize().getWidth() - 500;
		System.out.println(startx);
		int starty = (driver.manage().window().getSize().getHeight()) / 2;
		System.out.println(starty);

		(new TouchAction(driver)).press(startx, starty).moveTo(0, -70).release()
				.waitAction(java.time.Duration.ofMillis(5000)).perform().waitAction(java.time.Duration.ofMillis(5000));
	}

	/**
	 * scrollTillLast
	 */
	public Set<String> scrollTillLastElement(String text, By identifier) {
		Set<String> categoryNames = new HashSet<String>();
		int listSize = 0;
		boolean isElementFound = false;
		String catName = "";

		while (!isElementFound) {
			listSize = listOfWebElements(identifier).size();
			for (int i = 0; i < listSize; i++) {
				catName = listOfWebElements(identifier).get(i).getText();
				System.out.println(catName);
				categoryNames.add(catName);
			}

			if ((catName.equalsIgnoreCase(text))) {
				isElementFound = true;
				System.out.println("Last element reached - " + catName);
				break;
			} else {
				swipe();
			}
		}

		return categoryNames;
	}

	/**
	 * Hides the Keyboard
	 */
	public void hideKeyboard() {
		driver.hideKeyboard();
	}

	/**
	 * Clicks the element by identifier
	 */
	public void clickElement(By identifier) {

		if (isElementVisible(identifier)) {
			driver.findElement(identifier).click();
		}
	}

	/**
	 * Returns List of web elements created on : 9th January 2016
	 * 
	 */
	public List<MobileElement> listOfWebElements(By identifier) {
		List<MobileElement> listOfElements = driver.findElements(identifier);
		return listOfElements;
	}

	/**
	 * Stops the Appium Server created on : 9th January 2016
	 * 
	 */
	public void stopAppiumServer() {
		if ((AndroidDriver<MobileElement>) driver != null) {
			driver.quit();
		}
	}

	/**
	 * Gets the Screenshot of application
	 */

	public void getScreenshot() {
		System.out.println("Capturing the snapshot of the page ");
		File srcFiler = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd_MMM_hh-mm-ss_aaa(zzz)");
			java.util.Date curDate = new java.util.Date();
			FileUtils.copyFile(srcFiler, new File("/FailureScreenshots/" + sdf.format(curDate) + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns boolean based on visibility of any element
	 * 
	 */

	public boolean isElementVisible(By identifier) {
		return driver.findElement(identifier).isDisplayed();
	}

	/**
	 * Returns boolean based on presence of any element
	 */
	public boolean isElementPresent(By identifier) {
		if (driver.findElements(identifier).size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * waits for the visibility of a particular element
	 */
	public WebElement waitForVisibilityOfElement(By identifier) {
		WebElement element = null;
		try {
			WebDriverWait wait = (WebDriverWait) new WebDriverWait(driver, 120).pollingEvery(2, TimeUnit.SECONDS)
					.ignoring(NoSuchElementException.class);
			element = wait.until(ExpectedConditions.visibilityOfElementLocated(identifier));

		} catch (Exception e) {
		}
		return element;
	}

	/**
	 * waits for the clickablity of a particular element
	 */
	public WebElement waitForClickabilityOfElement(By identifier) {
		WebElement element = null;
		try {
			WebDriverWait wait = (WebDriverWait) new WebDriverWait(driver, 120).pollingEvery(5, TimeUnit.SECONDS)
					.ignoring(NoSuchElementException.class);
			element = wait.until(ExpectedConditions.elementToBeClickable(identifier));

		} catch (Exception e) {
		}
		return element;
	}
}
