package com.example.b10715.final_pj;

import android.content.Intent;

import java.io.Serializable;

/**
 * Created by MINA on 2017-05-03.
 */

public class Item implements Serializable {
    String id;
    String user_image, user_email;
    String image, text;
    String sitter_name, sitter_age, sitter_sex, sitter_phone_1, sitter_phone_2, sitter_phone_3, sitter_adr, sitter_career_year, sitter_career_month, sitter_pet, sitter_word;
    int sitter_price;

    String getId() {
        return this.id;
    }

    String getUser_image() {
        return this.user_image;
    }

    String getUser_email() {
        return this.user_email;
    }

    String getImage() {
        return this.image;
    }

    String getText() {
        return this.text;
    }

    String getSitter_name() {
        return this.sitter_name;
    }

    String getSitter_age() {
        return this.sitter_age;
    }

    String getSitter_sex() {
        return this.sitter_sex;
    }

    String getSitter_phone_1() {
        return this.sitter_phone_1;
    }

    String getSitter_phone_2() {
        return this.sitter_phone_2;
    }

    String getSitter_phone_3() {
        return this.sitter_phone_3;
    }

    String getSitter_adr() {
        return this.sitter_adr;
    }

    String getSitter_career_year() {
        return this.sitter_career_year;
    }

    String getSitter_career_month() {
        return this.sitter_career_month;
    }


    String getSitter_pet() {
        return this.sitter_pet;
    }

    int getSitter_price() {
        return this.sitter_price;
    }

    String getSitter_word() {
        return this.sitter_word;
    }

    /* PetStagram item값 */
    Item(String id, String user_image, String user_email, String image, String text) {
        this.id = id;
        this.user_image = user_image;
        this.user_email = user_email;
        this.image = image;
        this.text = text;
    }

    /* PetSitter item값 */
    Item(String id, String user_email, String user_image, String sitter_name, String sitter_age, String sitter_sex, String sitter_phone_1, String sitter_phone_2, String sitter_phone_3, String sitter_adr, String sitter_career_year, String sitter_career_month, String sitter_pet, String sitter_word, int sitter_price) {
        this.id = id;
        this.user_email = user_email;
        this.user_image = user_image;
        this.sitter_name = sitter_name;
        this.sitter_age = sitter_age;
        this.sitter_sex = sitter_sex;
        this.sitter_phone_1 = sitter_phone_1;
        this.sitter_phone_2 = sitter_phone_2;
        this.sitter_phone_3 = sitter_phone_3;
        this.sitter_adr = sitter_adr;
        this.sitter_career_year = sitter_career_year;
        this.sitter_career_month = sitter_career_month;
        this.sitter_pet = sitter_pet;
        this.sitter_price = sitter_price;
        this.sitter_word = sitter_word;
    }
}
