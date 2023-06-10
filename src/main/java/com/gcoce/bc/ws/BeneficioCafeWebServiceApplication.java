package com.gcoce.bc.ws;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@SpringBootApplication
public class BeneficioCafeWebServiceApplication {
    private static final Logger logger = LoggerFactory.getLogger(BeneficioCafeWebServiceApplication.class);

    public static void main(String[] args) throws UnknownHostException {
        SpringApplication.run(BeneficioCafeWebServiceApplication.class, args);
        String ip = java.net.InetAddress.getLocalHost().getHostAddress();
        String timeZone = TimeZone.getTimeZone("America/Guatemala").getID();
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.info("Application started and running at ip --------> " + ip);
        logger.info("Application started and running at timezone --> " + timeZone);
        logger.info("Application started and running at date ------> " + dateFormat.format(date));
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Guatemala"));
    }
}
