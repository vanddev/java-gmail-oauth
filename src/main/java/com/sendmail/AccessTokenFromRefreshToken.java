/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * For OAuth2 authentication, this program generates
 * access token from a previously acquired refresh token.
 */
package com.sendmail;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.LinkedHashMap;
import java.io.DataOutputStream;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.fasterxml.jackson.databind.ObjectMapper;
/**
 *
 * @author vaan
 */
public class AccessTokenFromRefreshToken {
    public static String getAccessToken() {

        HttpURLConnection conn = null;
        String accessToken = null;

        try {

            URL url = new URL("https://accounts.google.com/o/oauth2/token");

            /**
             * Replace the values of client_id, client_secret, refresh_token with the values to your app
             */
            Map<String,Object> params = new LinkedHashMap<>();
            params.put("client_id", "-----------.apps.googleusercontent.com");
            params.put("client_secret", "------client-secret------");
            params.put("refresh_token", "------refresh-token------");
            params.put("grant_type", "refresh_token");

            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String,Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            conn.setRequestProperty("Content-Length",
                                String.valueOf(postDataBytes.length));
            conn.setRequestProperty("Content-language", "en-US");
            conn.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream (
                            conn.getOutputStream());
            wr.write(postDataBytes);
            wr.close();

            StringBuilder sb = new StringBuilder();
            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            for ( int c = in.read(); c != -1; c = in.read() ) {
                sb.append((char)c);
            }

            String respString = sb.toString();

            // Read access token from json response
            ObjectMapper mapper = new ObjectMapper();
            AccessTokenObject accessTokenObj = mapper.readValue(respString,
                                                        AccessTokenObject.class);
            accessToken = accessTokenObj.getAccessToken();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(conn != null) {
                conn.disconnect(); 
            }
        }

        return(accessToken);
    }
}
