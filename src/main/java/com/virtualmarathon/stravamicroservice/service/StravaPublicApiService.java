package com.virtualmarathon.stravamicroservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name="strava",url = "https://www.strava.com/")
public interface StravaPublicApiService {

    @PostMapping("oauth/token")
    public Map<String,Object> generateAccessToken(@RequestBody Map<String,Object> accessTokenRequestBody);

    @PostMapping("api/v3/oauth/token")
    public Map<String,Object> refreshAccessToken(@RequestBody Map<String,Object> refreshTokenRequestBody);

    @GetMapping("api/v3/activities/{id}")
    public Map<String,Object> getActivityDetails(@PathVariable("id") Long activityId, @RequestParam("include_all_efforts") boolean includeAllEfforts, @RequestHeader("Authorization") String authorization);
}
