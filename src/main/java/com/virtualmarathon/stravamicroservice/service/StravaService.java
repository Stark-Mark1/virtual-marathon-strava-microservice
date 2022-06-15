package com.virtualmarathon.stravamicroservice.service;

import com.virtualmarathon.stravamicroservice.Constants;
import com.virtualmarathon.stravamicroservice.entity.StravaInfo;
import com.virtualmarathon.stravamicroservice.exception.StravaInfoException;
import com.virtualmarathon.stravamicroservice.repository.StravaInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import static com.virtualmarathon.stravamicroservice.Constants.CLIENT_ID;
import static com.virtualmarathon.stravamicroservice.Constants.CLIENT_SECRET;

@Service
public class StravaService {

    @Autowired
    StravaInfoRepository stravaInfoRepository;

    @Autowired
    StravaPublicApiService stravaPublicApiService;

    Logger LOGGER= LoggerFactory.getLogger(this.getClass());

    public boolean isVirtualMarathonAuthorized(String email){
        return stravaInfoRepository.existsById(email);
    }

    public void refreshToken(String email){
        Map<String,Object> stravaRefreshTokenBody=Map.of("client_id",CLIENT_ID,"client_secret",CLIENT_SECRET,"refresh_token",stravaInfoRepository.findById(email).get().getRefreshToken(),"grant_type","refresh_token");
        Map<String,Object> stravaRefreshTokenResponse = new HashMap<>();
        LOGGER.info("generating refresh token");
        try{
            stravaRefreshTokenResponse = stravaPublicApiService.refreshAccessToken(stravaRefreshTokenBody);
        }catch (Exception e){
            stravaInfoRepository.deleteById(email);
            throw new StravaInfoException("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        StravaInfo stravaInfo=new StravaInfo(email,stravaRefreshTokenResponse.get("access_token").toString(),stravaRefreshTokenResponse.get("refresh_token").toString(),
                Constants.getCurrentTime().plusSeconds(Long.parseLong(stravaRefreshTokenResponse.get("expires_in").toString())));
        stravaInfoRepository.save(stravaInfo);
    }

    public String authorizeVirtualMarathon(String email, String userCode){
        Map<String,Object> stravaAuthorizeBody=Map.of("client_id",CLIENT_ID,"client_secret",CLIENT_SECRET,"code",userCode,"grant_type","authorization_code");
        Map<String,Object> stravaAuthorizationResponse=new HashMap<>();
        try {
            stravaAuthorizationResponse=stravaPublicApiService.generateAccessToken(stravaAuthorizeBody);
            LOGGER.info(String.valueOf(stravaAuthorizationResponse));
        }catch (Exception e){
            stravaInfoRepository.deleteById(email);
            throw new StravaInfoException("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        StravaInfo stravaInfo=new StravaInfo();
        stravaInfo.setEmail(email);
        System.out.println(stravaAuthorizationResponse);
        stravaInfo.setAccessToken(stravaAuthorizationResponse.get("access_token").toString());
        stravaInfo.setRefreshToken(stravaAuthorizationResponse.get("refresh_token").toString());
        stravaInfo.setExpiresAt(Constants.getCurrentTime().plusSeconds(Long.parseLong(stravaAuthorizationResponse.get("expires_in").toString())));
        stravaInfoRepository.save(stravaInfo);
        return "OK";
    }

    public Map<String,Object> getLapDetails(String email,long activityId){
        if(stravaInfoRepository.existsById(email)){
            if(stravaInfoRepository.findById(email).get().getExpiresAt().isBefore(Constants.getCurrentTime())){
                refreshToken(email);
            }
            Map<String, Object> lapDetails=new HashMap<>();
            Map<String,Object> activity = new HashMap<>();
            try{
                activity= stravaPublicApiService.getActivityDetails(activityId,true,"Bearer "+stravaInfoRepository.findById(email).get().getAccessToken());
                LOGGER.info(activity.toString());
            }catch (Exception e){
                stravaInfoRepository.deleteById(email);
                throw new StravaInfoException(e.getMessage(), HttpStatus.BAD_REQUEST);
            }

            lapDetails.put("distance", BigDecimal.valueOf((Double) activity.get("distance")).divide(new BigDecimal(1000),4, RoundingMode.CEILING));
            lapDetails.put("lapCompletedInSeconds",activity.get("elapsed_time"));
            lapDetails.put("activityStartDate",activity.get("start_date_local"));
            return lapDetails;
        }
        else {
            throw new RuntimeException("user not authorised");
        }
    }

    public String unAuthorizeVirtualMarathon(String email) {
        if(stravaInfoRepository.existsById(email)){
            stravaInfoRepository.deleteById(email);
            return "OK";
        }
        throw new StravaInfoException("user is not authorized",HttpStatus.BAD_REQUEST);
    }
}
