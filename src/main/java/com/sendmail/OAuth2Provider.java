/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sendmail;

import java.security.Provider;
/**
 *
 * @author vaan
 */
public class OAuth2Provider extends Provider{
    
    public OAuth2Provider() {
      super("Google OAuth2 Provider", 1.0,
            "Provides the XOAUTH2 SASL Mechanism");
      put("SaslClientFactory.XOAUTH2",
          "com.sendmail.OAuth2SaslClientFactory"); //if you change the package, remember to replace the correct package of OAuth2SaslClientFactory
    }
}
