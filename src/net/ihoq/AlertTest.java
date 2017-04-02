package net.ihoq;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AlertTest {

	static WebDriver driver = null;
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub

		System.out.println(System.getProperty("user.dir"));
	 
     	//Login to Freestor
		
		driverSet("chrome");
		driver.manage().deleteAllCookies();
		driver.navigate().to("http://192.168.13.65");
		driver.manage().window().maximize();
		Thread.sleep(2500);
		driver.findElement(By.xpath("//input[@name='username']")).sendKeys("superadmin");
		driver.findElement(By.xpath("//input[@name='domain']")).sendKeys("");
		driver.findElement(By.xpath("//input[@name='password']")).sendKeys("freestor");
		driver.findElement(By.xpath("//button[@name='login']")).click();
		Thread.sleep(1500);
		//Explicit Wait for 10 sec for new page appear
	    //WebDriverWait wait = new WebDriverWait(driver, 10);
	    //wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("html/body/ng-include[1]/div/div/div[2]/ul")));
		//click manage
		
		topDropDown("Manage");
		selectServer("FSS-R720-42");	
		manageTab("Settings");
		//settingAction("Configuration Repository");
	    //Find the tab elements
		//Settings options = .title.ng-binding
     	setUpIOC("FSS-R720-43","FSS-4243","10.6.13.145");
		//setUpIOMC();

	    //driver.findElement(By.xpath("//button[.='Submit']")).click();
		//driver.findElement(By.xpath("//button[contains(text(),'Submit')]")).click();
		System.out.println("DONE");
	}
	
private static void selectServer(String serverIP) {
		// TODO Auto-generated method stub
		waitUntilAppear(15, "//input[@placeholder='Search for...']");
		driver.findElement(By.xpath("//input[@placeholder='Search for...']")).sendKeys(serverIP);
		driver.findElement(By.xpath("html/body/div[1]/div/div[1]/div[2]/ul/li")).click();
	}

public static void driverSet(String browerName){
		
		if (browerName.equalsIgnoreCase("Chrome")){
		
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\drivers\\chromedriver.exe");
			driver= new ChromeDriver(); 
	
		}
		else if  (browerName.equalsIgnoreCase("Firefox")){
		
			System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "\\drivers\\geckodriver.exe");
			driver= new FirefoxDriver(); 
		}
		else if  (browerName.equalsIgnoreCase("ie")){
			
			System.setProperty("webdriver.ie.driver", System.getProperty("user.dir") + "\\drivers\\IEDriverServer.exe");
			driver= new InternetExplorerDriver(); 
		}
		else 
		{
			System.setProperty("webdriver.edge.driver", System.getProperty("user.dir") + "\\drivers\\MicrosoftWebDriver.exe");
			driver = new EdgeDriver();
		}
	}
public static void clickByXpath(String locator){
		driver.findElement(By.xpath(locator)).click();
	}

public static void clickByID(String locator){
		driver.findElement(By.id(locator)).click();
	}
	
public static void waitFor(int sec){
		driver.manage().timeouts().implicitlyWait(sec, TimeUnit.SECONDS);
	}

public static void alertHandler() {
		
		Alert alert = driver.switchTo().alert();
		System.out.println(alert.getText());
		alert.sendKeys("Shamol");
		alert.accept();

	}
	
public static void alertHandler(String sendme) {
		Alert alert = driver.switchTo().alert();
		alert.sendKeys(sendme);
		alert.accept();
	}
	
public static void topDropDown(String tabName) {
	waitUntilAppear(50,"html/body/ng-include[1]/div/div/div[2]/ul");
    WebElement temp = driver.findElement(By.xpath("html/body/ng-include[1]/div/div/div[2]/ul"));
    List<WebElement> navList = temp.findElements(By.cssSelector("li"));
    findItemOnlist(navList, tabName);
	}

public static void findItemOnlist(List<WebElement> listOfItem, String findme) {
    for (WebElement item : listOfItem) {
    	System.out.println("Listed item "+item.getText());
        if (item.getText().equalsIgnoreCase(findme)) {
            System.out.println("findItemOnlist() --Requested Dropdown List: " + item.getText());
            item.click();
            break;
        }
    }
}

public static void manageTab(String actionType){
	waitUntilAppear(10,"html/body/div[1]/div/div[3]/div/div[1]/div[1]/ul");
	WebElement temp = driver.findElement(By.xpath("html/body/div[1]/div/div[3]/div/div[1]/div[1]/ul"));
	List<WebElement> navList = temp.findElements(By.cssSelector("li"));
    findItemOnlist(navList, actionType);
	}

public static void waitUntilAppear(int sec,String locator){
	WebDriverWait wait = new WebDriverWait(driver, sec);
	wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locator)));
	}

public static void setUpIOMC(){
	driver.navigate().to(driver.getCurrentUrl()+"failover");
	psdeployment();
}

public static void setUpIOC(String partnerServer,String clustername,String clusterIP) throws InterruptedException{
	
	// Go to the iocluster page
	driver.navigate().to(driver.getCurrentUrl()+"iocluster");
	
	// type password for psdeployment 
	psdeployment();
	
	//Select the partner node
	selectPartner(partnerServer);
	
	//Click Validation
	driver.findElement(By.xpath("//button[contains(text(),'Validate IO Cluster')]")).click();
	
	//Check if validation fail or validation process is stuck
	waitUntilDisappear(15,"css",".modal-title");
	
	//Cluster name 
	driver.findElement(By.xpath("//input[@ng-model='ioclusterItem.clusterName']")).sendKeys(clustername);
	
	//Management IP address
	driver.findElement(By.xpath("//input[@ng-model='ioclusterItem.mgmtIp']")).sendKeys(clusterIP);
	
	
	//IPMI username, password and confirm password 

	driver.findElement(By.xpath("//input[@ng-model='ioclusterItem.powercontrol.ipmi.username']")).sendKeys("admin");
	driver.findElement(By.xpath("//input[@ng-model='ioclusterItem.powercontrol.ipmi.password']")).sendKeys("falcon111");
	driver.findElement(By.xpath("//input[@ng-model='forms.panelFailoverForm.passwordAgain2']")).sendKeys("falcon111");
	
	//Click Validate IPMI option
	
	driver.findElement(By.cssSelector(".btn.btn-sm.btn-primary.ng-scope")).click();
	Thread.sleep(1500);
	if (driver.findElement(By.xpath("html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[2]/div[2]/div/form/div[2]/div[5]/div[6]/div/div/form-error/div/span")).isDisplayed())
		driver.findElement(By.xpath("html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[2]/div[2]/div/form/div[2]/div[5]/div[6]/div/div/form-error/div/span")).getText();
	else
		System.out.println("No error");
	
}

public static void psdeployment(){
		waitUntilAppear(200, "//input[@type='password'][@placeholder='Deployment password']");
		WebElement psdeployment = driver.findElement(By.xpath("//input[@type='password'][@placeholder='Deployment password']" ));
		psdeployment.sendKeys("psdeploymenton");
		psdeployment.sendKeys(Keys.ENTER);
	}

public static void settingAction(String tabName){
	waitUntilAppear(200, "//div[@ng-controller='ManageSettingsCtrl']");
	 WebElement temp = driver.findElement(By.xpath("//div[@ng-controller='ManageSettingsCtrl']"));
	
	 //"html/body/div[1]/div/div[2]/div/div[2]/div/div"
    List<WebElement> navList = temp.findElements(By.cssSelector("li"));
    findItemOnlist(navList, tabName);
	}

// Need to work on it 
public static void slideSubmit(){
	// letsWait(10, ".form-group");
	WebElement temp = driver.findElement(By.cssSelector(".form-group"));
	//"html/body/div[1]/div/div[2]/div/div[2]/div/div"
	List<WebElement> navList = temp.findElements(By.cssSelector("li"));
	//findItemOnlist(navList, tabName);
	}


//Select partner node for IOC/IOMC/Failover

public static void selectPartner(String partnerNode){
	waitUntilAppear(10, "//*[@placeholder='(Select)']");
	driver.findElement(By.xpath("//*[@placeholder='(Select)']")).click();
	List<WebElement> navList = driver.findElements(By.cssSelector(".ui-select-choices-row-inner"));
	findItemOnlist(navList, partnerNode);
	}


public static void waitUntilDisappear(int sec,String type,String locator){
	WebDriverWait wait = new WebDriverWait(driver, sec);
	if (type.equalsIgnoreCase("xpath"))
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(locator)));
	if (type.equalsIgnoreCase("css"))
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(locator)));
	}
}





