package co.edu.uptc.ms_suppliers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = "co.edu.uptc")
@EnableDiscoveryClient
public class MsSuppliersApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsSuppliersApplication.class, args);
	}

}

