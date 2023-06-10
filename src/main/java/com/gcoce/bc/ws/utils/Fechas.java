package com.gcoce.bc.ws.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author Gabriel Coc Estrada
 * @since 18/05/2023
 */
public class Fechas {
    private final static Logger logger = LoggerFactory.getLogger(Fechas.class);

    public static Date setTimeZoneDateGT(Date dateTransform) {

        if (dateTransform == null) {
            return null;
        }

        try {
            logger.debug("fecha recibida antes de la trasformacion: " + dateTransform);
            TimeZone tzGt = TimeZone.getTimeZone("America/Guatemala");
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formatter.setTimeZone(tzGt);
            String formattedDate = formatter.format(dateTransform);
            logger.debug("fecha trasformada: " + formatter.parse(formattedDate));
            return formatter.parse(formattedDate);
        } catch (ParseException ex) {
            logger.error("error al trasformar la fecha " + ex);
        }
        return dateTransform;
    }

    public static String getDateGuatemala() {
        TimeZone tzGt = TimeZone.getTimeZone("America/Guatemala");
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter.setTimeZone(tzGt);
        String formattedDate = formatter.format(new Date());
        logger.debug("fecha formateada --> " + formattedDate);
        return formattedDate.replace("-", "");
    }
}
