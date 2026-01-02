package com.project.community.config;

//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.session.web.http.CookieSerializer;
//import org.springframework.session.web.http.DefaultCookieSerializer;
//
//@Configuration
//public class CookieConfig {
//	 @Bean
//	     CookieSerializer cookieSerializer() {
//	        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
//	        
//	        serializer.setSameSite("None");        // Permitir cookies cross-site
//	        serializer.setUseSecureCookie(true);   // Chrome exige Secure para SameSite=None
//	        serializer.setCookiePath("/");         // Válida para toda la app
//
//	        return serializer;
//	    }
//}
