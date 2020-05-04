package commonFunctions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;

public class Library {
	public static WebDriver driver;
	public static String strComments = "";
	public static String strURL = "";
	public static String strBrowserName = "";
	public static String strProxy = "";
	static SimpleDateFormat formatter = new SimpleDateFormat("DD-MM-YYYY");
	static Date date = new Date();
	static String dateStr = formatter.format(date);
	public static String strScreenshotFolder = "" + dateStr;
	public static String strScreenshotName = "";
	public static String strNewScreenshotFolder = "" + dateStr;
	public static String strLoginWay = "";
	ExtentReports extent;
	static ExtentTest logger;
	public static String strReportsFolder = "" + dateStr;
	private static final Object LOCK = new Object();

	public static synchronized String getParameterFromInputSheet(String sheetName, String parameter, int rowNum) {
		String value = null;
		try {
			FileInputStream fi = new FileInputStream("C:\\ProActor\\ProActorExecutionPanel.xlsm");
			XSSFWorkbook w = new XSSFWorkbook(fi);
			XSSFSheet settingSheet = w.getSheet("ExecuteSettings");
			String strDataFileName = settingSheet.getRow(19).getCell(3).getStringCellValue();
			FileInputStream file = new FileInputStream(new File(strDataFileName));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheet(sheetName);
			int paramCol = -1;
			Iterator<Cell> cellIterator = sheet.getRow(0).cellIterator();
			while (cellIterator.hasNext()) {
				Cell cell = (Cell) cellIterator.next();
				try {
					if (cell.getStringCellValue().equals(parameter)) {
						paramCol = cell.getColumnIndex();
					}
				} catch (Exception e) {
				}
			}
			try {
				value = sheet.getRow(rowNum).getCell(paramCol).getStringCellValue();
			} catch (Exception e) {
			}
			file.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Please verify the Data sheet, and the path where it is saved are correct");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	public static void SetParameterFromInputSheet(String sheetName, String parameter, int rowNum, String Value) {
		synchronized (LOCK) {
			try {
				String filePath = System.getenv("filePath");
				FileInputStream fi = new FileInputStream(filePath);
				XSSFWorkbook w = new XSSFWorkbook(fi);
				XSSFSheet settingSheet = w.getSheet("ExecuteSettings");
				String strDataFileName = settingSheet.getRow(19).getCell(3).getStringCellValue();
				FileInputStream file = new FileInputStream(new File(strDataFileName));

				XSSFWorkbook workbook = new XSSFWorkbook(file);
				XSSFSheet sheet = workbook.getSheet(sheetName);

				int paramCol = -1;
				Iterator<Cell> cellIterator = sheet.getRow(0).cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = (Cell) cellIterator.next();
					try {
						if (cell.getStringCellValue().equals(parameter)) {
							paramCol = cell.getColumnIndex();
						}
					} catch (Exception e) {
					}
				}
				try {
					XSSFRow row1 = sheet.getRow(rowNum);
					XSSFCell cellA1 = row1.createCell(paramCol);
					cellA1.setCellValue(Value);
				} catch (Exception e) {
				}

				FileOutputStream out = new FileOutputStream(new File(filePath));

				workbook.write(out);
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.out.println("Please verify the Data sheet, and the path where it is saved are correct");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static WebDriver SetBrowser(String strBrowserName) {

		strBrowserName = strBrowserName.toUpperCase();
		if (strBrowserName.equals("IE")) {
			// Change the path to E: drive if D not present
			System.setProperty("webdriver.ie.driver", "E:/Jars/IEDriverServer.exe");
			org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();
			proxy.setProxyType(org.openqa.selenium.Proxy.ProxyType.DIRECT);
			DesiredCapabilities ieCapabilities = new DesiredCapabilities();
			ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			ieCapabilities.setCapability(CapabilityType.VERSION, "10");
			ieCapabilities.setCapability(CapabilityType.PROXY, proxy);
			// driver = new InternetExplorerDriver(ieCapabilities);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		} else if (strBrowserName.equals("GOOGLE CHROME")) {
			// Change the path to E: drive if D not present
			System.setProperty("webdriver.chrome.driver", "C:/Jars/chromedriver.exe");
			ChromeOptions options = new ChromeOptions();
			options.addArguments("user-data-dir=C:/Users/azansari/AppData/Local/Chrome/User Data/Default");
			options.addArguments("--start-maximized");
			driver = new ChromeDriver(options);
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		} else {
			System.setProperty("webdriver.gecko.driver", "C:\\Jars\\geckodriver.exe");
			driver = new FirefoxDriver();
			driver.manage().window().maximize();
		}
		return driver;
	}

	public static String getScreenShot(WebDriver driver, String name) {

		TakesScreenshot ts = (TakesScreenshot) driver;
		File src = ts.getScreenshotAs(OutputType.FILE);
		String path = strNewScreenshotFolder + "\\" + name + ".png";
		File destination = new File(path);

		try {
			FileUtils.copyFile(src, destination);
		} catch (IOException e) {
			System.out.println("Capture Failed" + e.getMessage());
		}
		return path;
	}

	public static boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public static void waitForPageLoaded(int TimeOutinSeconds) throws Exception {
		ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
			}
		};

		Wait<WebDriver> wait = new WebDriverWait(driver, TimeOutinSeconds);
		try {
			wait.until(expectation);
		} catch (Exception e) {
			throw e;
		}
	}

	// <<<<<<<<<<<<<<<<<<<<<<<unibet - Functions >>>>>>>>>>>>>>>>>>>>>>>>>>

	public static void LaunchApplication(String baseURL, ExtentTest logger) throws Exception {
		String name = new Object() {
		}.getClass().getEnclosingMethod().getName();
		driver.get(baseURL);
		driver.manage().window().maximize();
		waitForPageLoaded(10);
		Thread.sleep(3000);
		// Verifying Login Functionality
		if (Library.isElementPresent(By.xpath("//a[@href='#tab-personal']"))) {
			System.out.println("Main page is opened succesfully");
			String temp = Library.getScreenShot(driver, name);
			logger.log(Status.PASS, "Application is Launched",
					MediaEntityBuilder.createScreenCaptureFromPath(temp).build());
		} else {
			String temp = Library.getScreenShot(driver, name);
			logger.log(Status.FAIL, "Application is NOT Launched",
					MediaEntityBuilder.createScreenCaptureFromPath(temp).build());
		}
	}
}