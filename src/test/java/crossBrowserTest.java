//Step 1: Load iBrance.com
//Step 2: Validate that iBrance.com is as expected
//Step 3: Click a link on iBrance.com ("For Newly Diagnosed")
//Step 4: Load the link
//Step 5: Validate that the For Newly Diagnosed page is as expected.
import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.EyesRunner;
import com.applitools.eyes.FileLogger;
import com.applitools.eyes.TestResultsSummary;
import com.applitools.eyes.selenium.BrowserType;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import com.applitools.eyes.selenium.fluent.Target;
import com.applitools.eyes.visualgrid.model.DeviceName;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.Test;

public class crossBrowserTest {
    String url = "http://www.ibrance.com";
    private static EyesRunner runner = null;

    private static void lazyLoadPage(WebDriver driver) throws InterruptedException {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Long height = (Long) js.executeScript("return document.body.scrollHeight;");
        for (int i = 0; i < height / 100; i++)
            js.executeScript("window.scrollBy(0,500)");
        js.executeScript("window.scrollTo(0,0)");
    }

    public static Eyes initializeEyes(VisualGridRunner runner) {
        // Create Eyes object with the runner, meaning it'll be a Visual Grid eyes.
        Eyes eyes = new Eyes(runner);
        // Set API key
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
//        eyes.setLogHandler(new StdoutLogHandler(true));
        // Create SeleniumConfiguration.
        Configuration sconf = new Configuration();
        // Set the AUT name
        sconf.setAppName("iBrance App");
        // Set a test name
        sconf.setTestName("iBrance CrossBrowser Test");
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
        sconf.addBrowser(700, 500, BrowserType.IE_11);
        sconf.addBrowser(1000, 900, BrowserType.EDGE_CHROMIUM);
        sconf.addBrowser(1000, 900, BrowserType.EDGE_LEGACY);
        sconf.addBrowser(1000, 900, BrowserType.SAFARI);
        // Add iPhone device emulation
        sconf.addDeviceEmulation(DeviceName.iPhone_X);
        sconf.addDeviceEmulation(DeviceName.iPhone_6_7_8);
        // Set the configuration object to eyes
        eyes.setConfiguration(sconf);
        return eyes;
    }
    @Test()
    public void loadiBrance() throws InterruptedException {
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
        eyes.setLogHandler(new FileLogger("target/logging/crossBrowserTest.log",false,true));

        eyes.open(driver);

        driver.get(url);
        lazyLoadPage(driver);

        eyes.check("Full Screen popup", Target.window().fully());

        eyes.closeAsync();
        TestResultsSummary allTestResults = runner.getAllTestResults(false);
        System.out.println(allTestResults);

        driver.quit();

    }
}
