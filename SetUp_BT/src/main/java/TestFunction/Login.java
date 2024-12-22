package TestFunction;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Login {
    String URL_login = "https://hauifood.com/auth/login";
    String URL_dashBoard = "https://hauifood.com/";
    String loginWrongMess = "Tài khoản hoặc mật khẩu không chính xác";
    String loginBlankMess = "This field can not be empty";

    WebDriver driver;

    private WebElement loginButton; // Biến instance để lưu nút Đăng nhập

    public void login(String email, String password) {
        driver.findElement(By.name("email")).sendKeys(email);
        driver.findElement(By.name("password")).sendKeys(password);
        loginButton = driver.findElement(By.xpath("//body/div[@id='root']/div[1]/div[1]/div[2]/div[1]/form[1]/div[4]/button[1]"));
        loginButton.click(); // Gọi click nếu cần thao tác đăng nhập
    }


    @BeforeMethod
    public void setUp(){
        System.setProperty("webdriver.chrome.driver", "C:/Users/Admin/Downloads/chromedriver-win64/chromedriver-win64/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get(URL_login);
    }

    @Test(priority = 0, enabled = true)
    public void loginByUser(){
        login("dinhhlieu22@gmail.com","Lieut2003@@@");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlToBe(URL_dashBoard));
        Assert.assertEquals(driver.getCurrentUrl(), URL_dashBoard);
    }
    @Test(priority = 1, enabled = true)
    public void loginWrongEmail() {
        login("dinhlieu@gmail.com", "Lieut2003@@@");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement loginWrongElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Tài khoản hoặc mật khẩu không chính xác')]")));
        String loginWrong = loginWrongElement.getText().trim();
        Assert.assertEquals(loginWrong, loginWrongMess);  // Kiểm tra với thông báo lỗi tiếng Việt
    }
    @Test(priority = 2, enabled = true)
    public void loginWrongPassword(){
        login("dinhhlieu22@gmail.com","12345678L!");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement loginWrongElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Tài khoản hoặc mật khẩu không chính xác')]")));
        String loginWrong = loginWrongElement.getText().trim();
        Assert.assertEquals(loginWrong, loginWrongMess);
    }

    //Trống email + password chưa fix được
    @Test( enabled = false)
    public void checkRequireEmail() {
        // Gọi hàm login với email rỗng và mật khẩu hợp lệ
        driver.findElement(By.name("email")).sendKeys("");
        driver.findElement(By.name("password")).sendKeys("1234");
        loginButton = driver.findElement(By.xpath("//body/div[@id='root']/div[1]/div[1]/div[2]/div[1]/form[1]/div[4]/button[1]"));

        // Kiểm tra trạng thái của nút Đăng nhập
        Assert.assertFalse(loginButton.isEnabled(), "Nút Đăng nhập không bị disable khi trường email để trống.");
        driver.quit();
    }
    public boolean isButtonClickable(By locator, int timeoutInSeconds) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        wait.until(ExpectedConditions.elementToBeClickable(locator));
        return true; // Nếu phần tử clickable

    }

    @Test( enabled = false)
    public void checkRequirePassword() {
        // Gọi hàm login với email rỗng và mật khẩu hợp lệ
        driver.findElement(By.name("email")).sendKeys("lieu@gmail.com");
        driver.findElement(By.name("password")).sendKeys("");
        loginButton = driver.findElement(By.xpath("//body/div[@id='root']/div[1]/div[1]/div[2]/div[1]/form[1]/div[4]/button[1]"));

        // Kiểm tra trạng thái của nút Đăng nhập
//        Assert.assertTrue(loginButton., "Nút Đăng nhập không bị disable khi trường password để trống.");
        boolean buttonEnable = isButtonClickable(By.xpath("//body/div[@id='root']/div[1]/div[1]/div[2]/div[1]/form[1]/div[4]/button[1]"),1);
        Assert.assertTrue(buttonEnable);
        driver.quit();
//hỏi lại dev chỗ định dạng

    }

    @Test( enabled = false)
    public void checkRequireEmail1() throws InterruptedException {
        driver.findElement(By.name("email")).sendKeys("dinhlieu@gmail.com");
        driver.findElement(By.name("password")).sendKeys("");
        Thread.sleep(3000);
        WebElement button = driver.findElement(By.xpath("/html/body/div/div/div[1]/div[2]/div/form/div[4]/button[1]"));

        // Kiểm tra trạng thái của button
        if (button.isEnabled()) {
            System.out.println("Button is enabled.");
        } else {
            System.out.println("Button is disabled.");
        }
        driver.quit();

    }
    @Test(priority = 3, enabled = true)
    public void checkTextEmail(){
        driver.findElement(By.name("email")).sendKeys("lieu@gmail,com");
        driver.findElement(By.name("password")).click();
        driver.findElement(By.name("email")).click();
        Assert.assertTrue(driver.findElement(By.xpath("//p[contains(text(),'Vui lòng nhập mật khẩu.')]")).isDisplayed());
        Assert.assertEquals(driver.findElement(By.xpath("//p[contains(text(),'Vui lòng nhập mật khẩu.')]")).getText(),"Vui lòng nhập mật khẩu.");

    }

    @Test(priority = 4, enabled = true)
    public void checkTextPassword(){
        driver.findElement(By.name("email")).sendKeys("lieu@gmail,com");
        driver.findElement(By.name("password")).click();
        driver.findElement(By.name("email")).click();
        Assert.assertTrue(driver.findElement(By.xpath("//p[contains(text(),'Vui lòng nhập mật khẩu.')]")).isDisplayed());
        Assert.assertEquals(driver.findElement(By.xpath("//p[contains(text(),'Vui lòng nhập mật khẩu.')]")).getText(),"Vui lòng nhập mật khẩu.");
    }
    @AfterMethod
    public void tearDown(){
        driver.quit();
    }
}



