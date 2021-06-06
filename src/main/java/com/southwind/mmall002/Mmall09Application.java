package com.southwind.mmall002;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.southwind.mmall002.mapper")
public class Mmall09Application {

	public static void main(String[] args) {
		SpringApplication.run(Mmall09Application.class, args);}

}
