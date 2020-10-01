package com.tarasiuk.project.hotel.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Email {

    private String from;
    private String to;
    private String subject;
    private HashMap<String, String> model;
}
