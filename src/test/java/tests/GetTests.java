package tests;

import models.SingleUserNotFoundResponseModel;
import models.SingleUserSuccessfulResponseModel;
import models.UserListSuccessfulResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.UserSpec.*;

@DisplayName("GET-запросы")
public class GetTests extends TestBase {

    @DisplayName("Проверка данных одиночного пользователя")
    @Test
    void successfulGetSingleUserTest() {

        SingleUserSuccessfulResponseModel response = step("Сделать запрос", ()->
            given()
                    .spec(getUserRequestSpec)

            .when()
                    .get("/users/2")

            .then()
                    .spec(successfulUserResponseSpec)
                    .extract().as(SingleUserSuccessfulResponseModel.class)
        );

        step("Проверить ответ", ()-> {
            assertThat(response.getData().getId()).isEqualTo(2);
            assertThat(response.getData().getEmail()).isEqualTo("janet.weaver@reqres.in");
            assertThat(response.getData().getFirst_name()).isEqualTo("Janet");
            assertThat(response.getData().getLast_name()).isEqualTo("Weaver");
        });
    }

    @DisplayName("Проверка данных, когда пользователь не найден")
    @Test
    void singleUserNotFoundTest() {

        SingleUserNotFoundResponseModel response = step("Сделать запрос", ()->
            given()
                    .spec(getUserRequestSpec)

            .when()
                    .get("/users/23")

            .then()
                    .spec(notFoundUserResponseSpec)
                    .extract().as(SingleUserNotFoundResponseModel.class)
        );

        step("Проверить ответ", ()->
            assertThat(response).isNotNull()
        );
    }

    @DisplayName("Проверка данных списка всех пользователей")
    @Test
    void getUsersListTest() {

        UserListSuccessfulResponseModel response = step("Сделать запрос", ()->
            given()
                    .spec(getUserRequestSpec)

            .when()
                    .get("/users?page=2")

            .then()
                    .spec(successfulUserResponseSpec)
                    .extract().as(UserListSuccessfulResponseModel.class)
        );

        step("Проверить ответ", ()-> {
            assertThat(response.getPage()).isEqualTo(2);
            assertThat(response.getPer_page()).isEqualTo(6);
            assertThat(response.getTotal()).isEqualTo(12);
            assertThat(response.getTotal_pages()).isEqualTo(2);

            assertThat(response.getData().get(3).getId()).isEqualTo(10);
            assertThat(response.getData().get(3).getEmail()).isEqualTo("byron.fields@reqres.in");
            assertThat(response.getData().get(3).getFirst_name()).isEqualTo("Byron");
            assertThat(response.getData().get(3).getLast_name()).isEqualTo("Fields");

        });
    }
}