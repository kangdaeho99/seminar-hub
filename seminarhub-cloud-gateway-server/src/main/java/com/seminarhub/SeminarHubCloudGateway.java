package com.seminarhub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * [ 2023-08-18 daeho.kang ]
 * Description : SeminarHubCloudGateway Main
 *
 */
@EnableDiscoveryClient
@SpringBootApplication
@RestController
public class SeminarHubCloudGateway {

    @Autowired
    private DiscoveryClient discoveryClient;

    public static void main(String[] args) {
        System.out.println("Hello world!");
        SpringApplication.run(SeminarHubCloudGateway.class, args);
    }

    @GetMapping("/services")
    public List<String> services(){
        return this.discoveryClient.getServices();
    }
    @GetMapping("/instance")
    public List<List<ServiceInstance>> instance(){
        ArrayList<List<ServiceInstance>> arr = new ArrayList<>();
        for(int i=0;i<discoveryClient.getServices().size();i++){
            arr.add(discoveryClient.getInstances(discoveryClient.getServices().get(i)));
        }
        return arr;
    }


}