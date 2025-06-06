package com.example.auth_db.service;

import com.example.auth_db.dto.AuthUserDto;
import com.example.auth_db.dto.AuthUser;
import com.example.auth_db.entity.TokenDto;


public interface AuthUserService {
    public AuthUser save(AuthUserDto authUserDto);


    public TokenDto login(AuthUserDto authUserDto);


    public TokenDto validate(String token);
}
