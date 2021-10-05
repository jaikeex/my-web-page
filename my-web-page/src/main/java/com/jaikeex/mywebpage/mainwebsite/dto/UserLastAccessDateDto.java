package com.jaikeex.mywebpage.mainwebsite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLastAccessDateDto {

    private String username;
    private Timestamp lastAccessDate;

    public UserLastAccessDateDto(String username) {
        this.username = username;
        this.lastAccessDate = new Timestamp(System.currentTimeMillis() + 82800000L);
    }

}
