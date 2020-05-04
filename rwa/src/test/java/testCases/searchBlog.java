package testCases;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.openqa.selenium.By;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;

public class searchBlog 
    
{
	AppiumDriver<MobileElement>  driver = null;
	
	@BeforeClass
	 public void setUp() throws MalformedURLException {
	  // Created object of DesiredCapabilities class.
	  DesiredCapabilities capabilities = new DesiredCapabilities();

	  // Set android deviceName desired capability. Set your device name.
	  capabilities.setCapability("deviceName", "emulator-5554");

	  // Set BROWSER_NAME desired capability. It's Android in our case here.
	  capabilities.setCapability(CapabilityType.BROWSER_NAME, "chrome");

	  // Set android VERSION desired capability. Set your mobile device's OS version.
	  capabilities.setCapability(CapabilityType.VERSION, "8.1");

	  // Set android platformName desired capability. It's Android in our case here.
	  capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
	  capabilities.setCapability(MobileCapabilityType.FULL_RESET, false);
	  capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
	  System.setProperty("webdriver.chrome.driver", "C:\\Jars\\chromedriver.exe");
	  
	  // Created object of RemoteWebDriver will all set capabilities.
	  // Set appium server address and port number in URL string.
	  // driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
	  
	  driver = new AndroidDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
	  driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
	 }

	@Test
	public void test() {
		driver.get("http://www.google.com");
		driver.findElement(By.id("io.selendroid.testapp:id/my_text_field")).sendKeys("Test");
		System.out.print("Typed: Test");
        driver.findElement(By.id("io.selendroid.testapp:id/waitingButtonTest")).click();
        System.out.print("/nclicked");
       /* TouchAction ta = new TouchAction(driver);
       // WebElement we=driver.findElement(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.RelativeLayout/android.widget.FrameLayout/android.view.ViewGroup/android.support.v4.view.ViewPager/android.widget.RelativeLayout/android.view.ViewGroup/android.widget.FrameLayout/android.support.v7.widget.RecyclerView/android.widget.FrameLayout[5]/android.widget.LinearLayout/android.widget.RelativeLayout"));
       // ta.press(10,50).moveTo(10,-30).release().perform();
       int startx = (driver.manage().window().getSize().getWidth())/2;
        System.out.println(startx);
      //  int starty = (driver.manage().window().getSize().getHeight()) / 2;
        int starty = (driver.manage().window().getSize().getHeight())/2;
       
        System.out.println(starty);
        
        (new TouchAction(driver))
          .press(startx, starty) 
          .moveTo(0,-100)
          .release()
          .perform()
          .waitAction(java.time.Duration.ofMillis(5000));
        System.out.print("done");
        driver.findElement(By.id("com.hitesh_sahu.retailshop:id/checkout")).click();*/
		try {
	Thread.sleep(5000);
		}
		catch(InterruptedException e)
		{
		}
	}
	@AfterClass
	 public void End() {
	  driver.quit();
	 }
	}