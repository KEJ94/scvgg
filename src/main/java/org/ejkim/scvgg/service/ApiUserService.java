package org.ejkim.scvgg.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ejkim.scvgg.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@Service
public class ApiUserService {

    private final RestTemplate restTemplate;

    public ApiUserService() {
        this.restTemplate = new RestTemplate();
        // UTF-8 인코딩 강제 적용
        this.restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }

    public User findUser(String id) {
        String url = "http://127.0.0.1:64324/web-api/v2/aurora-profile-by-toon/"+id+"/30?request_flags=scr_profile";

        // GET 요청 보내기
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // JSON 디코딩
        ObjectMapper objectMapper = new ObjectMapper();
        User user = new User();

        try {
            user = objectMapper.readValue(response.getBody(), User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    public static void main(String[] args) {
        User user = new ApiUserService().findUser("Pay_SEA+");
        System.out.println(user.toString());
    }
}
