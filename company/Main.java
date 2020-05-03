package com.company;

import com.company.cache.config.CacheConfiguration;
import com.company.cache.service.DataService;

public class Main {

    public static void main(String[] args) {
	// write your code here

        CacheConfiguration config = new CacheConfiguration();
        config.number_of_levels = 3;
        config.capacity_each_layer = 3;
        config.read_time = 1;
        config.write_time = 2;

        DataService dataService = new DataService(config);
        int write = dataService.write("10", "100");
        System.out.println(write);

        dataService.write("11", "100");
        dataService.write("12", "100");
        dataService.write("13", "100");

        int readTime = dataService.read("11");
        System.out.println("10 "+readTime);

    }
}
