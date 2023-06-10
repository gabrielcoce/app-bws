package com.gcoce.bc.ws.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Gabriel Coc Estrada
 * @since 6/06/2023
 */
public class HcaptchaError {
    public static final Map<String, String>HCAPTCHA_ERROR_CODE = new HashMap<>();
    static {

        HCAPTCHA_ERROR_CODE.put("missing-input-secret", "The secret parameter is missing");

        HCAPTCHA_ERROR_CODE.put("invalid-input-secret", "The secret parameter is invalid or malformed");

        HCAPTCHA_ERROR_CODE.put("missing-input-response","The response parameter is missing");

        HCAPTCHA_ERROR_CODE.put("invalid-input-response", "The response parameter is invalid or malformed");

        HCAPTCHA_ERROR_CODE.put("bad-request",	"The request is invalid or malformed");

        HCAPTCHA_ERROR_CODE.put("invalid-or-already-seen-response",	"The response parameter has already been checked, or has another issue");

        HCAPTCHA_ERROR_CODE.put("sitekey-secret-mismatch",	"The site key is not registered with the provided secret");

    }
}
