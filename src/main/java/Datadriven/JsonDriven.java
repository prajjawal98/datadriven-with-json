package Datadriven;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class JsonDriven {

    WebDriver driver;

    @BeforeClass
    void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.MILLISECONDS);
    }

    @AfterClass
    void teardown() {
        driver.close();
    }


    @Test(dataProvider="dp")
    void login(String data) throws InterruptedException {
        String user[] = data.split(",");
       driver.get("http://thedemosite.co.uk/login.php");
       driver.findElement(By.name("username")).sendKeys(user[0]);
        driver.findElement(By.name("password")).sendKeys(user[1]);
        driver.findElement(By.name("FormsButton2")).click();
        Thread.sleep(1000);

        String Exp1 = driver.findElement(By.xpath("/html/body/table/tbody/tr/td[1]/big/blockquote/blockquote/font/center/b")).getText();
        String actual1 = "**Failed Login**";
        System.out.println(Exp1);
        Assert.assertEquals(Exp1,actual1);
        System.out.println("Assertion Success...........");


    }

    @DataProvider(name = "dp")
    public String[] readjson() throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();

        FileReader fileReader = new FileReader("src/Jsonfiles/testdata.json");
        Object object = jsonParser.parse(fileReader);

        JSONObject jsonObject = (JSONObject) object;
        JSONArray array = (JSONArray) jsonObject.get("userlogins");

        String arr[] = new String[array.size()];
        for (int i = 0; i < array.size(); i++) {
            JSONObject users = (JSONObject) array.get(i);
            String username = (String) users.get("username");
            String password = (String) users.get("password");

            arr[i] = username+","+password;
        }
        return arr;
    }
}
