package tests;


import models.ResponceAddModel;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;


import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byTagAndText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static specs.TestSpec.addRequestSpec;
import static specs.TestSpec.addResponseSpec;

@Tag("ApiUiTests")
public class WebShop extends TestBase {


    @Test
    void loginWithApiTest() {


        String authCookieValue = authMetod(login, password);
        String authCookieKey = "NOPCOMMERCE.AUTH";

        open("/Content/jquery-ui-themes/smoothness/images/ui-bg_flat_75_ffffff_40x100.png");
        Cookie authCookie = new Cookie(authCookieKey, authCookieValue);
        getWebDriver().manage().addCookie(authCookie);


        step("Открыть главную страницу", () ->
                open(""));

        step("Проверка, что логин на странице соотвествует введенному", () ->
                $(".account").shouldHave(text(login)));
    }


    @Test
    void loginWithAddApiTest() {

        String authCookieValue = authMetod(login, password);
        String authCookieKey = "NOPCOMMERCE.AUTH";


        open("/Content/jquery-ui-themes/smoothness/images/ui-bg_flat_75_ffffff_40x100.png");
        Cookie authCookie = new Cookie(authCookieKey, authCookieValue);
        getWebDriver().manage().addCookie(authCookie);


        ResponceAddModel responceAdd = step("Отправить запрос", () ->
                given(addRequestSpec)
                        .cookie(authCookieKey, authCookieValue)
                        .when()
                        .post()
                        .then()
                        .spec(addResponseSpec)
                        .extract().as(ResponceAddModel.class));


        step("Открыть главную страницу", () ->
                open(""));

        step("Проверка количества товаров добавленных в корзину на главной странице", () ->
                $(".cart-qty").shouldHave(text(responceAdd.getUpdatetopcartsectionhtml())));

    }

    @Test
    void loginDeleteProductTest() {

        String authCookieValue = authMetod(login, password);
        String authCookieKey = "NOPCOMMERCE.AUTH";

        open("/Content/jquery-ui-themes/smoothness/images/ui-bg_flat_75_ffffff_40x100.png");
        Cookie authCookie = new Cookie(authCookieKey, authCookieValue);
        getWebDriver().manage().addCookie(authCookie);



        ResponceAddModel responceAdd = step("Оотправляем запрос на добавление товара", () ->
                given(addRequestSpec)
                        .cookie(authCookieKey, authCookieValue)
                        .when()
                        .post()
                        .then()
                        .spec(addResponseSpec)
                .extract().as(ResponceAddModel.class));


        step("Открываем главную страницу", () ->
                open("/cart"));

        step("Удаляем книгу из корзины", () -> {
            $(byTagAndText("span", "Remove:")).sibling(0).click();
            $$("input").findBy(attribute("value", "Update shopping cart")).click();
        });
        step("Проверяем, что товар удален из корзины", () ->
                $(".cart-qty").shouldNotHave(text(responceAdd.getUpdatetopcartsectionhtml())));

    }


}
