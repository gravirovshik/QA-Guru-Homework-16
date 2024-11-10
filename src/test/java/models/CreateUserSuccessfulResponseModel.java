package models;

import lombok.Data;

@Data
public class CreateUserSuccessfulResponseModel {
    String name, job, id, createdAt;
}