package com.fudan.certserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//启动类
@SpringBootApplication
public class CertserverApplication {

	static String dir = "key";
	static String pubfile = dir + "/pub_key";
	static String mskfile = dir + "/master_key";

	public static void main(String[] args) {

		SpringApplication.run(CertserverApplication.class, args);
	}

}
