package com.example.productos.service;

import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ComunicacionService {

    @Autowired
    private RestTemplate restTemplate;

    @Retry(name = "miRetry", fallbackMethod = "fallback")
    public String llamarOtroMicroservicio(String url) {
        return restTemplate.getForObject(url, String.class);
    }

    public String fallback(String url, Exception ex) {
        return "Servicio no disponible temporalmente";
    }
}