package org.ejkim.scvgg.controller;

import lombok.RequiredArgsConstructor;
import org.ejkim.scvgg.domain.User;
import org.ejkim.scvgg.service.ApiUserService;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class ApiController {

    private final ApiUserService apiUserService;

    @GetMapping("/api/user/{region}/{id}")
    public @ResponseBody User getGreeting(@PathVariable("region") String region, @PathVariable("id") String id) {
        return apiUserService.findUser(region, id);
    }
}