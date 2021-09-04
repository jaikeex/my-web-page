package com.jaikeex.mywebpage.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelAttribute {
    private String name;
    private Object value;
}