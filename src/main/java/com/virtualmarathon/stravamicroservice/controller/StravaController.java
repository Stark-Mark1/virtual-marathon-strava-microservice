package com.virtualmarathon.stravamicroservice.controller;

import com.virtualmarathon.stravamicroservice.repository.StravaInfoRepository;
import com.virtualmarathon.stravamicroservice.service.StravaPublicApiService;
import com.virtualmarathon.stravamicroservice.service.StravaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/strava")
public class StravaController {

    @Autowired
    StravaInfoRepository stravaInfoRepository;

    @Autowired
    StravaPublicApiService stravaPublicApiService;

    @Autowired
    StravaService stravaService;

    @GetMapping("/is-virtual-marathon-authorized")
    public boolean isVirtualMarathonAuthorized(@RequestParam String email){
        return stravaService.isVirtualMarathonAuthorized(email);
    }

    @PostMapping("/authorize-virtual-marathon")
    public String authorizeVirtualMarathon(@RequestParam String email,@RequestParam String userCode){
        return stravaService.authorizeVirtualMarathon(email, userCode);
    }

    @DeleteMapping("/unauthorize-virtual-marathon")
    public String unAuthorizeVirtualMarathon(@RequestParam String email){
        return stravaService.unAuthorizeVirtualMarathon(email);
    }

    @GetMapping("/get-lap-details")
    public Map<String,Object> getLapDetails(@RequestParam String email,@RequestParam long activityId){
        return stravaService.getLapDetails(email,activityId);
    }

}
