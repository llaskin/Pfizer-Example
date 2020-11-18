//Step 1: Load iBrance.com
//Step 2: Validate that iBrance.com is as expected
//Step 3: Click a link on iBrance.com ("For Newly Diagnosed")
//Step 4: Load the link
//Step 5: Validate that the For Newly Diagnosed page is as expected.
import com.applitools.eyes.*;
import com.applitools.eyes.selenium.*;
import com.applitools.eyes.selenium.fluent.Target;
import com.applitools.eyes.visualgrid.model.DeviceName;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

import javax.swing.*;
import java.util.List;

public class accordionTest {
    String url = "https://www.ibrance.com/about-ibrance";
    private static EyesRunner runner = null;

    private static void lazyLoadPage(WebDriver driver) throws InterruptedException {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Long height = (Long) js.executeScript("return document.body.scrollHeight;");
        for (int i = 0; i < height / 100; i++)
            js.executeScript("window.scrollBy(0,100)");
    }

    public static Eyes initializeEyes(VisualGridRunner runner) {
        // Create Eyes object with the runner, meaning it'll be a Visual Grid eyes.
        Eyes eyes = new Eyes(runner);
        // Set API key
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
        eyes.setLogHandler(new StdoutLogHandler(true));
        // Create SeleniumConfiguration.
        Configuration sconf = new Configuration();
        // Set the AUT name
        sconf.setAppName("iBrance App");
        // Set a test name
        sconf.setTestName("iBrance Accordions");
        // Set a batch name so all the different browser and mobile combinations are
        // part of the same batch
        sconf.setBatch(new BatchInfo("iBrance Batch"));
        // Add Chrome browsers with different Viewports
        sconf.addBrowser(1440, 820, BrowserType.CHROME);
        sconf.addBrowser(700, 500, BrowserType.CHROME);
        // Add Firefox browser with different Viewports
        sconf.addBrowser(1440, 820, BrowserType.FIREFOX);
        sconf.addBrowser(1600, 1200, BrowserType.FIREFOX);
        //Add IE 10 browser with different viewports
        sconf.addBrowser(1024, 768, BrowserType.IE_10);
        //Add IE 10 browser wiath different viewports
        sconf.addBrowser(1000, 900, BrowserType.IE_11);
        sconf.addBrowser(1000, 900, BrowserType.EDGE_CHROMIUM);
        sconf.addBrowser(1000, 900, BrowserType.SAFARI);
        // Set the configuration object to eyes
        eyes.setConfiguration(sconf);
        return eyes;
    }
    @Test()
    public void loadiBrance() throws InterruptedException {
        int i;
        WebElement currentAccordion = null;

        // Create a runner with concurrency of 10
        VisualGridRunner runner = new VisualGridRunner(10);
        //Initialize Eyes with Visual Grid Runner
        Eyes eyes = initializeEyes(runner);
        // Create a new Webdriver
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless","--ignore-certificate-errors");
        ChromeDriver driver = new ChromeDriver(options);

        eyes.setForceFullPageScreenshot(true);
        eyes.setStitchMode(StitchMode.CSS);

        eyes.open(driver);

        driver.get(url);
        lazyLoadPage(driver);

        //Thread.sleep to make sure Tablets link closes
        Thread.sleep(5000);

        List<WebElement> accordion = driver.findElements(By.cssSelector(".accordion-row .accordion-header"));

        Actions actions = new Actions(driver);

        eyes.check("Initial State, All Accordions Closed", Target.window().fully());

        for(i = 1; i < accordion.size(); i++)
        {
            currentAccordion = accordion.get(i);

            actions.moveToElement(currentAccordion).click().perform();

        }
        eyes.check("All Accordions Opened ", Target.window().fully());
        System.out.println("Closing");
        for(i = 1; i < accordion.size(); i++)
        {
            currentAccordion = accordion.get(i);
            actions.moveToElement(currentAccordion).click().perform();
        }
        eyes.check("All Accordions Re-Closed ", Target.window().fully());



        eyes.closeAsync();

        TestResultsSummary allTestResults = runner.getAllTestResults(false);
        System.out.println(allTestResults);

        driver.quit();
    }
}
