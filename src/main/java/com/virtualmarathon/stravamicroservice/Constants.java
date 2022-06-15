package com.virtualmarathon.stravamicroservice;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class Constants {
    public static final long CLIENT_ID=84133L;

    public static final String CLIENT_SECRET="55b4569e671b7c8261c37c6810461e85eedccd42";

    public static LocalDateTime getCurrentTime(){
        return LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
    }
}
