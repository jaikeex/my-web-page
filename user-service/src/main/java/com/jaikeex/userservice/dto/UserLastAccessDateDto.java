package com.jaikeex.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLastAccessDateDto {
    //private Integer id;
    private String username;
    private Timestamp lastAccessDate;
}
