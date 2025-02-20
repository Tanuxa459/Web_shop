package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.restassured.RestAssured.given;

@Slf4j
public class TestBase {

    String login = "reg@mail.ru",
            password = "qwerty123";

    @BeforeEach
    void listenerSetup() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
    }


    @AfterEach
    void addAttachments() {
        Attach.screenshotAs("Last screenshot");
        Attach.pageSource();
        Attach.browserConsoleLogs();
        Attach.addVideo();

    }

//    @AfterEach
//    void closeWebdriver() {
//        Selenide.closeWebDriver();
//    }

    @BeforeAll
    static void setup() {

        Configuration.baseUrl = "https://demowebshop.tricentis.com";
        RestAssured.baseURI = "https://demowebshop.tricentis.com";


        Configuration.pageLoadStrategy = "eager";

        String remoteHost = System.getProperty("remoteHost", "remoteHost");

        log.info("remoteHost: " + remoteHost);

        String userdata = System.getProperty("userdata", "userdata");

        log.info("userdata: " + userdata);

        Configuration.remote = userdata + remoteHost + "/wd/hub";

        log.info("Configuration.remote: " + Configuration.remote);


        Configuration.browser = System.getProperty("browser", "chrome");
        Configuration.browserVersion = System.getProperty("browserVersion", "126");


        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("selenoid:options", Map.<String, Object>of(
                "enableVNC", true,
                "enableVideo", true
        ));
        Configuration.browserCapabilities = capabilities;
    }


    String authMetod(String login, String password) {


        String authCookieKey = "NOPCOMMERCE.AUTH";
        String authCookieValue = given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("Email", login)
                .formParam("Password", password)
                .when()
                .post("/login")
                .then()
                .log().all()
                .statusCode(302)
                .extract()
                .cookie(authCookieKey);

        return authCookieValue;
    }


}