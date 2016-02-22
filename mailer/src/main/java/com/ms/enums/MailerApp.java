/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.enums;

/**
 *
 * @author mySolutions
 */
public enum MailerApp {

    DENTAL("smtp.gmail.com", "atenciondental@dgac.gob.cl", "Rfgh1637", "<!DOCTYPE html \"-//w3c//dtd xhtml 1.0 transitional //en\" \"http://www.w3.org/tr/xhtml1/dtd/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head>\n" +
"    <!--[if gte mso 9]><xml>\n" +
"     <o:OfficeDocumentSettings>\n" +
"      <o:AllowPNG/>\n" +
"      <o:PixelsPerInch>96</o:PixelsPerInch>\n" +
"     </o:OfficeDocumentSettings>\n" +
"    </xml><![endif]-->\n" +
"    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" +
"    <meta name=\"viewport\" content=\"width=device-width\">\n" +
"    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=9; IE=8; IE=7; IE=EDGE\">\n" +
"    <title>Template Base</title>\n" +
"    \n" +
"</head>\n" +
"<body style=\"width: 100% !important;min-width: 100%;-webkit-text-size-adjust: 100%;-ms-text-size-adjust: 100% !important;margin: 0;padding: 0;background-color: #FFFFFF\">\n" +
"<img class=\"center\" style=\"outline: none;text-decoration: none;-ms-interpolation-mode: bicubic;clear: both;display: block;border: none;height: auto;line-height: 100%;margin: 0 auto;float: none;width: 402px;max-width: 402px\" align=\"center\" border=\"0\" src=\"https://drive.google.com/file/d/0B-8RAON7l1E7SFBXdlVNR3JmUm8/view?usp=sharing\" alt=\"Image\" title=\"Image\" width=\"402\"></body></html>",
    
    
    
    ""),
    CCRR("smtp.gmail.com", "centrorecreacional@dgac.gob.cl", "Hjtq8567", "s", "");

    private String provider;
    private String user;
    private String password;
    private String topTemplate;
    private String bottomTemplate;

    private MailerApp(String provider, String user, String password, String topTemplate, String bottomTemplate) {
        this.provider = provider;
        this.user = user;
        this.password = password;
        this.topTemplate = topTemplate;
        this.bottomTemplate = bottomTemplate;
    }

    public String getProvider() {
        return provider;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getTopTemplate() {
        return topTemplate;
    }

    public String getBottomTemplate() {
        return bottomTemplate;
    }

}
