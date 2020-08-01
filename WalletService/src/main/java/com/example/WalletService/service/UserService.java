package com.example.WalletService.service;

import com.example.WalletService.Model.User;
import com.example.WalletService.Model.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private RestTemplate restTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    final String uri = "http://127.0.0.1:9016/users";

    public List<User> getAllUser(){
        ResponseEntity<UserResponse> entity = restTemplate.getForEntity(uri,UserResponse.class);
        LOGGER.info(entity.getHeaders().toString());
        if(entity.getStatusCode().equals(HttpStatus.NOT_FOUND)){
            return null;
        }else
            return entity.getBody().getList();
    }

    public User getAUser(String id){
        Map<String,String> map = new HashMap<String, String>();
        map.put("id",id);
        ResponseEntity<User> entity = restTemplate.getForEntity(uri+"/{id}",User.class,map);
        if(entity.getStatusCode().equals(HttpStatus.NOT_FOUND)){
            return null;
        }else
            return entity.getBody();
    }
}
