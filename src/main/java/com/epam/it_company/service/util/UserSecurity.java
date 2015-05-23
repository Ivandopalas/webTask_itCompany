package com.epam.it_company.service.util;


import com.epam.it_company.service.ServiceException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class.  Provides security of holding data in database.
 */
public class UserSecurity {
    private final static String EXCEPTION_MESSAGE = "Security error";

    private final static UserSecurity instance = new UserSecurity();

    private UserSecurity(){}

    public static UserSecurity getInstance(){
        return instance;
    }

    /**
     * Encode password with hashcode algorithm.
     * @param password income password you want to encode.
     * @return encoded password.
     */
    public String encodePassword(String password) throws ServiceException {
        StringBuffer encodedPassword = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            for (byte b : digest) {
                encodedPassword.append(String.format("%02x", b & 0xff));
            }
        } catch (NoSuchAlgorithmException ex) {
            throw new ServiceException(EXCEPTION_MESSAGE,ex);
        }
        return encodedPassword.toString();
    }
}
