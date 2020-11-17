//Step 1: Load iBrance.com
//Step 2: Validate that iBrance.com is as expected
//Step 3: Click a link on iBrance.com ("For Newly Diagnosed")
//Step 4: Load the link
//Step 5: Validate that the For Newly Diagnosed page is as expected.
import com.applitools.eyes.EyesRunner;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.StdoutLogHandler;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import com.applitools.eyes.selenium.fluent.Target;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class linkClickTest {
    String url = "http://www.ibrance.com";
    private static EyesRunner runner = null;

    private static void lazyLoadPage(WebDriver driver) throws InterruptedException {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Long height = (Long) js.executeScript("return document.body.scrollHeight;");
        for (int i = 0; i < height / 100; i++)
            js.executeScript("window.scrollBy(0,100)");
    }

    @Test()
    public void iBranceLinkTest() throws InterruptedException {
        runner = new ClassicRunner();

        Eyes eyes = new Eyes(runner);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu");
        ChromeDriver driver = new ChromeDriver(options);

        eyes.setApiKey("Y7bRjY1ZkKKR9a4F1wCMNlBVyr1y6a23ckJo9I7ihQM110");
        eyes.setForceFullPageScreenshot(true);
        eyes.setStitchMode(StitchMode.CSS);
        eyes.setLogHandler(new StdoutLogHandler(true));

        eyes.open(driver, "iBrance", "iBrance crossBrowser", new RectangleSize(1200, 600));

        driver.get(url);
        lazyLoadPage(driver);
        eyes.check("Full Screen popup", Target.window().fully());
        driver.findElement(By.linkText("FOR NEWLY DIAGNOSED")).click();
        Thread.sleep(3000);
        lazyLoadPage(driver);
        eyes.check("For Newly Diagnosed", Target.window().fully());
        eyes.closeAsync();
        driver.quit();

    }
}
