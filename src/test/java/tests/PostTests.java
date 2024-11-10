package tests;

import models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.UserSpec.*;

@DisplayName("POST-запросы")
public class PostTests extends TestBase {

    @DisplayName("Проверка возвращаемых данных при создании нового пользователя")
    @Test
    void createUserTest() {

        CreateUserRequestModel request = new CreateUserRequestModel();
        request.setName("roma");
        request.setJob("qa_engineer");

        CreateUserSuccessfulResponseModel response = step("Сделать запрос", ()->
            given()
                    .spec(postUserRequestSpec)
                    .body(request)

            .when()
                    .post("/users")

            .then()
                    .spec(successfulCreateUserResponseSpec)
                    .extract().as(CreateUserSuccessfulResponseModel.class)
        );

        step("Проверить ответ", ()-> {
            assertThat(response.getName()).isEqualTo(request.getName());
            assertThat(response.getJob()).isEqualTo(request.getJob());
            assertThat(response.getId()).isNotEmpty();
            assertThat(response.getCreatedAt()).isNotEmpty();
        });
    }

    @DisplayName("Проверка возвращаемых данных при регистрации нового пользователя")
    @Test
    void successfulRegistrationTest() {

        RegistrationRequestModel request = new RegistrationRequestModel();
        request.setEmail("eve.holt@reqres.in");
        request.setPassword("pistol");

        RegistrationSuccessfulResponseModel response = step("Сделать запрос", ()->
            given()
                    .spec(postUserRequestSpec)
                    .body(request)

            .when()
                    .post("/register")

            .then()
                    .spec(successfulRegistrationUserResponseSpec)
                    .extract().as(RegistrationSuccessfulResponseModel.class)
        );

        step("Проверить ответ", ()-> {
            assertThat(response.getId()).isNotEqualTo(0);
            assertThat(response.getToken()).isNotEmpty();
        });
    }

    @DisplayName("Проверка возвращаемых данных при не корректной регистрации нового пользователя (регистрация без пароля)")
    @Test
    void unsuccessfulRegistrationMissingPasswordTest() {

        RegistrationRequestModel request = new RegistrationRequestModel();
        request.setEmail("eve.holt@reqres.in");

        RegistrationMissingEmailOrPasswordResponseModel response = step("Сделать запрос", ()->
            given()
                    .spec(postUserRequestSpec)
                    .body(request)

            .when()
                    .post("/register")

            .then()
                    .log().status()
                    .log().body()
                    .spec(errorRegistrationResponseSpec)
                    .extract().as(RegistrationMissingEmailOrPasswordResponseModel.class)
        );

        step("Проверить ответ", ()->
            assertThat(response.getError()).isEqualTo("Missing password")
        );
    }

    @DisplayName("Проверка возвращаемых данных при не корректной регистрации нового пользователя (регистрация без емейла)")
    @Test
    void unsuccessfulRegistrationMissingEmailTest() {

        RegistrationRequestModel request = new RegistrationRequestModel();
        request.setPassword("pistol");

        RegistrationMissingEmailOrPasswordResponseModel response = step("Сделать запрос", ()->
            given()
                    .spec(postUserRequestSpec)
                    .body(request)

            .when()
                    .post("/register")

            .then()
                    .spec(errorRegistrationResponseSpec)
                    .extract().as(RegistrationMissingEmailOrPasswordResponseModel.class)
        );

        step("Проверить ответ", ()->
            assertThat(response.getError()).isEqualTo("Missing email or username")
        );
    }
}