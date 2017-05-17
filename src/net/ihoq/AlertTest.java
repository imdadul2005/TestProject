package net.ihoq;

import java.util.ArrayList;
import java.util.List;
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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AlertTest {
	static WebDriver driver = null;

	public static void main(String[] args) throws InterruptedException {

		System.out.println(System.getProperty("user.dir"));


		// This is the part where the configuration files needs to be set before running the setupIOC
		String node1ServerName = " IOC82532-86";
		String node2ServerName = "HPG9FSS133";
		String iocName = "IOC1-3484";
		String managementIP = "10.8.25.90";
		String qurom1Disk = "(101:0:5:2)";
		String qurom2Disk = "(100:0:4:2)";


   /*    String node1ServerName = "10.6.13.42";
		String node2ServerName = "FSS-R720-43";
		String iocName = "IOC-4243";
		String managementIP = "10.6.13.145";
		String qurom1Disk = "FALCON:IPSTOR DISK.002";
		String qurom2Disk = "FALCON:IPSTOR DISK.003";

		/* This is the part where the configuration files needs to be set before running the setupIOMC */
		//  String node1ServerName = "10.6.13.40";
		String partnerIOC = "IOC8253484";
		String primaryIOC = "IOC-99-114";

		String ipmiUsername = "admin";
		String ipmiPasswd = "falcon101";

		//Login to Freestor

		driverSet("chrome");
		driver.manage().deleteAllCookies();
		driver.navigate().to("http://192.168.13.65");
		driver.manage().window().maximize();
		Thread.sleep(1500);
		login("superadmin", "", "freestor");

		topDropDown("Manage");
		//	selectServer(node1ServerName);
		//	selectServer(node1ServerName);
		//  manageTab("Settings");
		//	setUpIOC(node1ServerName,node2ServerName,iocName,managementIP,qurom1Disk,qurom2Disk,ipmiUsername,ipmiPasswd);
		setUpIOMC(node1ServerName, partnerIOC);
		System.out.println("DONE");
	}

	public static void login(String username, String domain, String password) {
		findElementByXpath("//input[@name='username']", username);
		findElementByXpath("//input[@name='domain']", domain);
		findElementByXpath("//input[@name='password']", password);
		driver.findElement(By.xpath("//button[@name='login']")).click();
	}
	public static void findElementByXpath(String xpathLocation, String information) {
		driver.findElement(By.xpath(xpathLocation)).sendKeys(information);
	}
	public static void selectServer(String serverIP) {
		// TODO Auto-generated method stub
		waitUntilAppear(50, "xpath", "//input[@placeholder='Search for...']");
		findElementByXpath("//input[@placeholder='Search for...']", serverIP);
		driver.findElement(By.xpath("html/body/div[1]/div/div[1]/div[2]/ul/li")).click();
	}
	public static void driverSet(String driverName) {

		if (driverName.equalsIgnoreCase("Chrome")) {

			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\drivers\\chromedriver.exe");
			driver = new ChromeDriver();

		} else if (driverName.equalsIgnoreCase("Firefox")) {

			System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "\\drivers\\geckodriver.exe");
			driver = new FirefoxDriver();
		} else if (driverName.equalsIgnoreCase("ie")) {

			System.setProperty("webdriver.ie.driver", System.getProperty("user.dir") + "\\drivers\\IEDriverServer.exe");
			driver = new InternetExplorerDriver();
		} else {
			System.setProperty("webdriver.edge.driver", System.getProperty("user.dir") + "\\drivers\\MicrosoftWebDriver.exe");
			driver = new EdgeDriver();
		}
	}
	public static void clickByXpath(String locator) {
		driver.findElement(By.xpath(locator)).click();
	}
	public static void clickAndSendByXpath(String locator, String sendItem) {
		WebElement tempElement = driver.findElement(By.xpath(locator));
		tempElement.sendKeys(sendItem);
		tempElement.sendKeys(Keys.ENTER);
	}
	public static void clickByID(String locator) {
		driver.findElement(By.id(locator)).click();
	}
	public static void waitFor(int sec) {
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
		waitUntilAppear(50, "xpath", "html/body/ng-include[1]/div/div/div[2]/ul");
		WebElement temp = driver.findElement(By.xpath("html/body/ng-include[1]/div/div/div[2]/ul"));
		List<WebElement> navList = temp.findElements(By.cssSelector("li"));
		findItemOnlist(navList, tabName);
	}
	public static void findItemOnlist(List<WebElement> listOfItem, String findme) {
		System.out.println("findItemOnlist()--Requested item : " + findme);
		for (WebElement item : listOfItem) {
			System.out.println("Listed item " + item.getText());
			if (item.getText().equalsIgnoreCase(findme)) {
				System.out.println("findItemOnlist() --Found item : " + item.getText());
				item.click();
				break;
			}
		}
	}

	public static void manageTab(String actionType) {
		waitUntilAppear(10, "xpath", "html/body/div[1]/div/div[3]/div/div[1]/div[1]/ul");
		WebElement temp = driver.findElement(By.xpath("html/body/div[1]/div/div[3]/div/div[1]/div[1]/ul"));
		List<WebElement> navList = temp.findElements(By.cssSelector("li"));
		findItemOnlist(navList, actionType);
	}
	public static void waitUntilAppear(int sec, String type, String locator) {

		try {
			WebDriverWait wait = new WebDriverWait(driver, 120);
			//wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locator)));
			if (type.equalsIgnoreCase("xpath"))
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locator)));
			if (type.equalsIgnoreCase("css"))
				wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(locator)));
		} catch (NoSuchElementException e) {
			System.out.println("Element not found");
		} catch (TimeoutException e) {
			System.out.println("Time out exception");
			//TODO: handle exception
		}
	}
	public static void setUpIOMC(String primaryServer, String partnerIOC) throws InterruptedException {


	    String inputBox = "input[ng-click='$select.activate()']";
        String dropList = ".ui-select-choices-row-inner";

		selectServer(primaryServer);
		manageTab("Settings");


		//Go to failover page

		driver.navigate().to(driver.getCurrentUrl() + "failover");

		//Type the password for Deployment
		psdeployment();

		//Select the partner IOC node name
		driver.getPageSource();

		//	Thread.sleep(3000);
		new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".col-sm-7")));
		selectPartner(partnerIOC);
		//	Click Validate


		waitUntilAppear(10, "xpath", "html/body/div[1]/div/div[3]/div/div[1]/div[1]/ul");
		List<WebElement> temp1 = driver.findElements(By.cssSelector(".col-sm-7"));
		findItemOnlist(temp1, "Validate IO Multi-Cluster");

		// driver.findElement(By.xpath("//button[contains(text(),'Validate IO Multi-Cluster')]")).click();

		Thread.sleep(10000);

		// waitUntilAppear(1,"xpath","");

		waitUntilDisappear(15, "css", ".modal-title");

		String vip_1 = "10.8.25.86";
		String vip_2 = "10.8.25.34";
		String vip_3 = "10.8.25.32";
		String vip_4 = "10.8.25.84";
		String hb_1 = "10.8.25.87";
		String hb_2 = "10.8.25.35";

		List<String> target1 = new ArrayList<String>();
		target1.add("2101000d7709a787");
		target1.add("2101000d7709a786");

		List<String>standBy1 = new ArrayList<String>();
		standBy1.add("2001000e1e09a786");
		standBy1.add("2001000e1e09a787");

		List<String> standBy2 = new ArrayList<String>();
		standBy2.add("2001000e1e30a1e0");
		standBy2.add("2001000e1e30a1e1");

		List<String>target2 = new ArrayList<String>();
		target2.add("2101000d7730a1e0");
		target2.add("2101000d7730a1e1");

		List<String> target3 = new ArrayList<String>();
		target3.add("2101000d77c209a4");
		target3.add("2101000d77c209a5");

		List<String> standBy3 = new ArrayList<String>();
		standBy3.add("2001000e1ec209a4");
		standBy3.add("2001000e1ec209a5");

		List<String>target4 = new ArrayList<String>();
		target4.add("2101000d7709a8c2");
		target4.add("2101000d7709a8c3");

		List<String>standBy4 = new ArrayList<String>();
		standBy4.add("2001000e1e09a8c2");
		standBy4.add("2001000e1e09a8c3");





		//List<WebElement> config = findElementsList(By.cssSelector(".col-sm-7"));

		//.ui-select-search.input-xs.ng-pristine   .ui-select-search.input-xs.ng-pristine.ng-valid
		List<WebElement> config = findElementsList(By.cssSelector("input[ng-click='$select.activate()']"));
		findFromListIndex(config, 0, "10.8.25.86",false);
		findFromListIndex(config,1,"10.8.25.34",false);
		findFromListIndex(config,4,"10.8.25.87",false);
		findFromListIndex(config,5,"10.8.25.35",false);
		findFromListIndex(config, 6, "10.8.25.32",false);
		findFromListIndex(config,7,"10.8.25.84",false);
		findFromListIndex(config,10,"10.8.25.33",false);
		findFromListIndex(config,11,"10.8.25.85",false);
		wwpnList(config,12,target1);
		wwpnList(config,13,standBy2);
		wwpnList(config,14,standBy1);
		wwpnList(config,15,target2);

		wwpnList(config,16,target3);
		wwpnList(config,17,standBy4);
		wwpnList(config,18,standBy3);
		wwpnList(config,19,target4);

		//wwpnSelection(config, 12,14);
		//wwpnSelection(config, 12);
		/*findFromListIndex(config,13,"10.8.25.34",true);
		findFromListIndex(config,14,"10.8.25.87",true);
		findFromListIndex(config,15,"10.8.25.35",true);
		findFromListIndex(config, 16, "10.8.25.32",true);
		findFromListIndex(config,17,"10.8.25.84",true);
		findFromListIndex(config,18,"10.8.25.33",true);
		findFromListIndex(config,19,"10.8.25.85",true);*/
	}

	public static void setUpIOC(String primaryServer, String partnerServer, String clustername, String clusterIP, String qurom1, String qurom2, String ipmiUserName, String ipmiPassword) throws InterruptedException {

		selectServer(primaryServer);
		manageTab("Settings");


		// Go to the iocluster page
		driver.navigate().to(driver.getCurrentUrl() + "iocluster");

		// type password for psdeployment
		psdeployment();

		//Select the partner node
		selectPartner(partnerServer);

		//Click Validation
		driver.findElement(By.xpath("//button[contains(text(),'Validate IO Cluster')]")).click();

		//Check if validation fail or validation process is stuck
		waitUntilDisappear(15, "css", ".modal-title");

		//Cluster name
		findElementByXpath("//input[@ng-model='ioclusterItem.clusterName']", clustername);

		//Management IP address
		findElementByXpath("//input[@ng-model='ioclusterItem.mgmtIp']", clusterIP);

		//Detect Quorum Repository physical device need a better coding here for sure

		String qurom1Location = "html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[2]/div[2]/div/form/div[2]/div[4]/div[2]/div[1]/div/div/div/div/span";
		String qurom2Location = "html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[2]/div[2]/div/form/div[2]/div[4]/div[2]/div[2]/div/div/div/span";

		findFromList(qurom1Location, ".ng-binding.ng-scope", qurom1);
		findFromList(qurom2Location, ".ng-binding.ng-scope", qurom2);

		//IPMI username, password and confirm password

		findElementByXpath("//input[@ng-model='ioclusterItem.powercontrol.ipmi.username']", ipmiUserName);
		findElementByXpath("//input[@ng-model='ioclusterItem.powercontrol.ipmi.password']", ipmiPassword);
		findElementByXpath("//input[@ng-model='forms.panelFailoverForm.passwordAgain2']", ipmiPassword);
		//Click Validate IPMI option

		List<WebElement> buttonList = driver.findElements(By.cssSelector(".btn.btn-sm.btn-primary"));
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
		waitUntilAppear(10, "css", ".modal-title");


	}
	public static void psdeployment() {
		waitUntilAppear(200, "xpath", "//input[@type='password'][@placeholder='Deployment password']");
		WebElement psdeployment = driver.findElement(By.xpath("//input[@type='password'][@placeholder='Deployment password']"));
		psdeployment.sendKeys("psdeploymenton");
		psdeployment.sendKeys(Keys.ENTER);
	}
	public static void settingAction(String tabName) {
		waitUntilAppear(200, "xpath", "//div[@ng-controller='ManageSettingsCtrl']");
		WebElement temp = driver.findElement(By.xpath("//div[@ng-controller='ManageSettingsCtrl']"));

		//"html/body/div[1]/div/div[2]/div/div[2]/div/div"
		List<WebElement> navList = temp.findElements(By.cssSelector("li"));
		findItemOnlist(navList, tabName);
	}

	// Need to work on it
	public static void slideSubmit() {
		// letsWait(10, ".form-group");
		WebElement temp = driver.findElement(By.cssSelector(".form-group"));
		List<WebElement> navList = temp.findElements(By.cssSelector("li"));
		//findItemOnlist(navList, tabName);
	}
	public static void selectPartner(String partnerNode) {

		findItemOnlist(driver.findElements(By.cssSelector(".col-sm-7")), "(Select)");
		List<WebElement> navList = driver.findElements(By.cssSelector(".ui-select-choices-row-inner"));
		findItemOnlist(navList, partnerNode);
	}

	public static void waitUntilDisappear(int sec, String type, String locator) {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		if (type.equalsIgnoreCase("xpath"))
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(locator)));
		if (type.equalsIgnoreCase("css"))
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(locator)));
	}
	public static void findFromList(String listLocation, String csslistElement, String itemOnTheList) {
		waitUntilAppear(10, "xpath", listLocation);
		driver.findElement(By.xpath(listLocation)).click();
		List<WebElement> navList = driver.findElements(By.cssSelector(csslistElement));
		findItemOnlist(navList, itemOnTheList);
	}
	public static void findFromListIndex(List<WebElement> itemLists, int index, String stringToSend, boolean isList) {

		System.out.println(itemLists.size());
		//Check if item is Displayed
		if (itemLists.get(index).isDisplayed()) {
			if (!(isList)) {
				itemLists.get(index).sendKeys(stringToSend);
				itemLists.get(index).sendKeys(Keys.ENTER);
			}
			if (isList)
			{
				itemLists.get(index).click();
				List<WebElement> navList = driver.findElements(By.cssSelector(".ui-select-choices-row-inner"));
				findItemOnlist(navList, "1");
			}
		}
	}


	public static void wwpnList(List<WebElement> itemLists, int targetPort,List<String> wwpn) {

		List<String> adapterList = new ArrayList<String>();
		//Check if item is Displayed
		String dropdownList = ".ui-select-choices-row-inner";
		if (itemLists.get(targetPort).isDisplayed()) {

			itemLists.get(targetPort).click();
		//	try {
				//WebElement dropdown = driver.findElement(By.cssSelector(".ui-select-choices-row-inner"));
				List<WebElement> dropDownListItems = driver.findElements(By.cssSelector(dropdownList));

				//Loop until all the element of the list is selected
				for (String wwpnInfo : wwpn) {
						System.out.println(wwpnInfo);
						//while(!isFound){
						itemLists.get(targetPort).click();
						//waitUntilAppear(10,"css",dropdownList);
						dropDownListItems = driver.findElements(By.cssSelector(dropdownList));
						for (int i = 0; i <dropDownListItems.size(); i++) {
							System.out.println("dropdown list item number : " + dropDownListItems.size());
							System.out.println("dropdown list items: "+dropDownListItems.get(i).getText());
							if (dropDownListItems.get(i).getText().contains(wwpnInfo)) {
								System.out.println(dropDownListItems.get(i).getText());
								dropDownListItems.get(i).click();
								//itemLists = findElementsList(By.cssSelector("input[ng-click='$select.activate()']"));
								System.out.println(i);
								break;
								//i = 0;
							}
						}

					}
			//} catch (RuntimeException e) {
			//	System.out.println("Error");
			//}
		}
	}










	// This method is to comare the adapter number for standby and target
	public static void wwpnSelection(List<WebElement> itemLists, int targetPort,int standByPort) {


        List<String> adapterList = new ArrayList<String>();
        //Check if item is Displayed
        if (itemLists.get(targetPort).isDisplayed()) {

            itemLists.get(targetPort).click();

            // This is to select the first element of the list
            try {
                WebElement dropdown = driver.findElement(By.cssSelector(".ui-select-choices-row-inner"));
                // dropdown = driver.findElement(By.cssSelector(".ui-select-choices-row-inner"));

                //Need to find the list of the elements of the list
                List<WebElement> navList = driver.findElements(By.cssSelector(".ui-select-choices-row-inner"));

                //Loop until all the element of the list is selected
                for (int i = 0; i <= navList.size() - 1; i++) {

                    System.out.println("Before :" + itemLists.size() + " " + navList.size());
                    System.out.println("Listed item " + dropdown.getText());
                    adapterList.add(getAdapterNumber(dropdown.getText()));
                    dropdown.click();
                    itemLists.get(targetPort).click();
                    //navList.get(0).click();
                    //.ui-select-search.input-xs.ng-pristine.ng-valid
                    itemLists = findElementsList(By.cssSelector("input[ng-click='$select.activate()']"));
                    try {
                        dropdown = driver.findElement(By.cssSelector(".ui-select-choices-row-inner"));
                    } catch (RuntimeException e) {
                        break;
                    }
                    //itemLists.get(index).click();
                    System.out.println(itemLists.size() + " " + navList.size());
                    //itemLists.get(index).click();
                }
            } catch (RuntimeException e) {
            }

        }
        System.out.println(adapterList.toString());


        //StandBy WWPN Selection

        if (itemLists.get(standByPort).isDisplayed()) {

            itemLists.get(standByPort).click();

            // This is to select the first element of the list
            try {
                //WebElement	dropdown = driver.findElement(By.cssSelector(".ui-select-choices-row-inner"));
                List<WebElement> navList = driver.findElements(By.cssSelector(".ui-select-choices-row-inner"));

                //Loop until all the element of the list is selected

                for (int i = 0; i < adapterList.size(); i++) {
                    System.out.println("Adapter index " + i);
                    for (WebElement x : navList) {
                        if (x.getText().contains(adapterList.get(i))) {
                            System.out.println("Match found at index "+i  + " "+ adapterList.get(i) + " " + x.getText());
                            x.click();
                            navList = driver.findElements(By.cssSelector(".ui-select-choices-row-inner"));
                            itemLists = findElementsList(By.cssSelector("input[ng-click='$select.activate()']"));
                        }
                        navList = driver.findElements(By.cssSelector(".ui-select-choices-row-inner"));
                    }
                }

            }
            catch (RuntimeException e) {
            }
        }
    }

	public static List<WebElement> findElementsList(By locator) {
		return driver.findElements(locator);
	}
	public static String getAdapterNumber(String string) {

		int left = string.indexOf("(");
		int right = string.indexOf(")");
		// pull out the text inside the parens
		return "(" + string.substring(left + 1, right) +")";
	}



}




