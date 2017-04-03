package net.ihoq;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AlertTest {
static WebDriver driver = null;
public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub

		System.out.println(System.getProperty("user.dir"));
	 
		
		/* This is the part where the configuration files needs to be set before running the setupIOC */
		String node1ServerName = "10.6.13.40";
		String node2ServerName = "FSS-R720-43";
		String iocName = "IOC-4243";
		String managementIP = "10.6.13.145";
		String qurom1Disk = "(102:0:0:2)";
		String qurom2Disk = "(103:0:1:3)";
		String ipmiUsername ="admin";
		String ipmiPasswd = "falcon101";
		
		
		/* This is the part where the configuration files needs to be set before running the setupIOMC */
		
		String partnerIOC = "IOC-4243";
		
		
		
		
     	//Login to Freestor
		
		driverSet("chrome");
		driver.manage().deleteAllCookies();
		driver.navigate().to("http://192.168.13.65");
		driver.manage().window().maximize();
		Thread.sleep(1500);

		findElementByXpath("//input[@name='username']","superadmin");
		findElementByXpath("//input[@name='domain']","");
		findElementByXpath("//input[@name='password']","freestor");
		
		driver.findElement(By.xpath("//button[@name='login']")).click();
		Thread.sleep(1500);
		
		topDropDown("Manage");
		selectServer(node1ServerName);	
		manageTab("Settings");

     //	setUpIOC(node2ServerName,iocName,managementIP,qurom1Disk,qurom2Disk,ipmiUsername,ipmiPasswd);
		setUpIOMC(partnerIOC);


		System.out.println("DONE");
	}
public static void findElementByXpath(String xpathLocation,String information){
	driver.findElement(By.xpath(xpathLocation)).sendKeys(information);
}
public static void selectServer(String serverIP) {
		// TODO Auto-generated method stub
		waitUntilAppear(50,"xpath", "//input[@placeholder='Search for...']");
		findElementByXpath("//input[@placeholder='Search for...']",serverIP);
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
	waitUntilAppear(50,"xpath","html/body/ng-include[1]/div/div/div[2]/ul");
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
	waitUntilAppear(10,"xpath","html/body/div[1]/div/div[3]/div/div[1]/div[1]/ul");
	WebElement temp = driver.findElement(By.xpath("html/body/div[1]/div/div[3]/div/div[1]/div[1]/ul"));
	List<WebElement> navList = temp.findElements(By.cssSelector("li"));
    findItemOnlist(navList, actionType);
	}
public static void waitUntilAppear(int sec,String type, String locator){
	
	try {
			WebDriverWait wait = new WebDriverWait(driver, 120);
			//wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locator)));
			if (type.equalsIgnoreCase("xpath"))
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locator)));
			if (type.equalsIgnoreCase("css"))
				wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(locator)));
		} 
	catch (NoSuchElementException e){
			System.out.println("Element not found");
		}
	catch (TimeoutException e) {
		System.out.println("Time out exception");
		// TODO: handle exception
		}
	}
public static void setUpIOMC(String partnerIOC) throws InterruptedException{
	//Go to failover page
	
	driver.navigate().to(driver.getCurrentUrl()+"failover");
	
	//Type the password for Deployment 
	psdeployment();
	  
	//Select the partner IOC node name
	// driver.getPageSource();
	
	Thread.sleep(3000);

	new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".col-sm-7")));
	
	
	selectPartner(partnerIOC); 
   //Click Validate
	
	findItemOnlist(driver.findElements(By.cssSelector(".col-sm-7")), "Validate IO Multi-Cluster");
		
	//If Reconfigure IOMC appear
	
	
}
public static void setUpIOC(String partnerServer,String clustername,String clusterIP,String qurom1, String qurom2,String ipmiUserName, String ipmiPassword) throws InterruptedException{
	
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
	findElementByXpath("//input[@ng-model='ioclusterItem.clusterName']",clustername);
	
	//Management IP address
	findElementByXpath("//input[@ng-model='ioclusterItem.mgmtIp']",clusterIP);
		
	//Detect Quorum Repository physical device need a better coding here for sure
	
	String qurom1Location= "html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[2]/div[2]/div/form/div[2]/div[4]/div[2]/div[1]/div/div/div/div/span";
	String qurom2Location= "html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[2]/div[2]/div/form/div[2]/div[4]/div[2]/div[2]/div/div/div/span";

	findFromList(qurom1Location, ".ng-binding.ng-scope",qurom1);
	findFromList(qurom2Location, ".ng-binding.ng-scope", qurom2);
	
	//IPMI username, password and confirm password 

	findElementByXpath("//input[@ng-model='ioclusterItem.powercontrol.ipmi.username']",ipmiUserName);
	findElementByXpath("//input[@ng-model='ioclusterItem.powercontrol.ipmi.password']",ipmiPassword);
	findElementByXpath("//input[@ng-model='forms.panelFailoverForm.passwordAgain2']",ipmiPassword);
	//Click Validate IPMI option
	
	List<WebElement> buttonList =  driver.findElements(By.cssSelector(".btn.btn-sm.btn-primary"));
	findItemOnlist(buttonList, "Validate IPMI");
	findItemOnlist(buttonList, "Set Up IO Cluster");

	//This part needs to be re-arranged to handle ipmi validation error
/*	Thread.sleep(1500);
	if (driver.findElement(By.xpath("html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[2]/div[2]/div/form/div[2]/div[5]/div[6]/div/div/form-error/div/span")).isDisplayed())
		driver.findElement(By.xpath("html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[2]/div[2]/div/form/div[2]/div[5]/div[6]/div/div/form-error/div/span")).getText();
	else
		System.out.println("No error");
*/	
	//Wait until Configure IO Cluster appear then click Configure
	waitUntilAppear(10,"css",".modal-title");
	
	
}
public static void psdeployment(){
		waitUntilAppear(200,"xpath", "//input[@type='password'][@placeholder='Deployment password']");
		WebElement psdeployment = driver.findElement(By.xpath("//input[@type='password'][@placeholder='Deployment password']" ));
		psdeployment.sendKeys("psdeploymenton");
		psdeployment.sendKeys(Keys.ENTER);
	}
public static void settingAction(String tabName){
	waitUntilAppear(200, "xpath","//div[@ng-controller='ManageSettingsCtrl']");
	 WebElement temp = driver.findElement(By.xpath("//div[@ng-controller='ManageSettingsCtrl']"));
	
	 //"html/body/div[1]/div/div[2]/div/div[2]/div/div"
    List<WebElement> navList = temp.findElements(By.cssSelector("li"));
    findItemOnlist(navList, tabName);
	}
// Need to work on it 
public static void slideSubmit(){
	// letsWait(10, ".form-group");
	WebElement temp = driver.findElement(By.cssSelector(".form-group"));
	List<WebElement> navList = temp.findElements(By.cssSelector("li"));
	//findItemOnlist(navList, tabName);
	}
//Select partner node for IOC/IOMC/Failover
public static void selectPartner(String partnerNode){
//	waitUntilAppear(30,"xpath", "//*[@name='partnerId']");
//	driver.findElement(By.xpath("//*[@name='partnerId']")).click();
	
	findItemOnlist(driver.findElements(By.cssSelector(".col-sm-7")), "(Select)");
	List<WebElement> navList = driver.findElements(By.cssSelector(".ui-select-choices-row-inner"));
	findItemOnlist(navList, partnerNode);
	}
public static void waitUntilDisappear(int sec,String type,String locator){
	WebDriverWait wait = new WebDriverWait(driver,30);
	if (type.equalsIgnoreCase("xpath"))
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(locator)));
	if (type.equalsIgnoreCase("css"))
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(locator)));
	}
public static void findFromList(String listLocation, String csslistElement, String itemOnTheList){
	waitUntilAppear(10,"xpath",listLocation);
	driver.findElement(By.xpath(listLocation)).click();
	List<WebElement> navList = driver.findElements(By.cssSelector(csslistElement));
	findItemOnlist(navList, itemOnTheList);
	}


}





