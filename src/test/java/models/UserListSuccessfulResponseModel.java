package models;

import lombok.Data;

import java.util.List;

@Data
public class UserListSuccessfulResponseModel {

    int     page,
            per_page,
            total,
            total_pages;
    List<Data> data;
    Support support;

    @lombok.Data
    public static class Data {
        int id;
        String  email,
                first_name,
                last_name,
                avatar;
    }

    @lombok.Data
    public static class Support {
        String url, text;
    }
}