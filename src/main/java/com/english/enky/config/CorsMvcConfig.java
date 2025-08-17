//package com.english.enky.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class CorsMvcConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry corsRegistry) {
//        // 프론트 서버가 백엔드의 모든 api 접근 권한을 줌.
//        corsRegistry.addMapping("/**")
//                .allowedOrigins("http://localhost:3000");
//    }
//}
