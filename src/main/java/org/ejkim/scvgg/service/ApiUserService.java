package org.ejkim.scvgg.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ejkim.scvgg.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Service
public class ApiUserService {

    private final RestTemplate restTemplate;

    public ApiUserService() {
        this.restTemplate = new RestTemplate();
        // UTF-8 인코딩 강제 적용
        this.restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }

    public User findUser(String region, String id) {
        // 집 PC
        //String url = "http://124.57.148.252:3000/api/user/"+region+"/"+id;
        //ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // 로컬 Macbook
        String baseUrl = "http://host.docker.internal:52596/web-api/v2/aurora-profile-by-toon/{id}/{region}";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("request_flags", "scr_profile");

        URI uri = builder.buildAndExpand(id, region).toUri();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

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


        User user = new ApiUserService().findUser("10", "DAY6");
        System.out.println(user.toString());
    }
}
