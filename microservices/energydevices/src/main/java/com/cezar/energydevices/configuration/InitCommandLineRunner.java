package com.cezar.energydevices.configuration;

import com.cezar.energydevices.service.EndUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitCommandLineRunner implements CommandLineRunner {
    @Autowired
    EndUserService endUserService;

    @Override
    public void run(String... args) throws Exception {
        endUserService.addInitialData();
        System.out.println("InitCommandLineRunner has been executed.");
    }
}