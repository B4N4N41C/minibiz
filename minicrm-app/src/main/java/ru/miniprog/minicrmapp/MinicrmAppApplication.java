package ru.miniprog.minicrmapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.modulith.Modulithic;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.modulith.docs.Documenter;
import org.springframework.web.bind.annotation.RestController;
import ru.miniprog.minicrmapp.kanban.api.KanbanBoardController;
import org.springframework.context.event.EventListener;


import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

@SpringBootApplication
@Modulithic
public class MinicrmAppApplication {
	Logger log = LoggerFactory.getLogger(MinicrmAppApplication.class);


	public static void main(String[] args) {
		SpringApplication.run(MinicrmAppApplication.class, args);
	}

	@EventListener
	public void onApplicationEvent(ServletWebServerInitializedEvent event) {
		try {
			int port = event.getWebServer().getPort();
			String localIp = getLocalIpAddress();
			String externalIp = getExternalIpAddress();

			log.info("=================================================");
			log.info("Приложение запущено!");
			log.info("Локальный доступ: http://localhost:{}", port);
			log.info("Доступ по локальной сети: http://{}:{}", localIp, port);
			if (externalIp != null) {
				log.info("Доступ из интернета: http://{}:{}", externalIp, port);
			}
			log.info("=================================================");
		} catch (Exception e) {
			log.error("Ошибка при определении IP-адресов: {}", e.getMessage());
		}
	}

	private String getLocalIpAddress() throws Exception {
		Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		while (interfaces.hasMoreElements()) {
			NetworkInterface iface = interfaces.nextElement();
			if (iface.isLoopback() || !iface.isUp()) continue;

			Enumeration<InetAddress> addresses = iface.getInetAddresses();
			while (addresses.hasMoreElements()) {
				InetAddress addr = addresses.nextElement();
				if (addr.getHostAddress().startsWith("192.168.") ||
						addr.getHostAddress().startsWith("10.") ||
						addr.getHostAddress().startsWith("172.")) {
					return addr.getHostAddress();
				}
			}
		}
		return "127.0.0.1";
	}


	private String getExternalIpAddress() {
		try {
			return InetAddress.getByName("checkip.amazonaws.com").getHostAddress();
		} catch (Exception e) {
			log.warn("Не удалось определить внешний IP-адрес: {}", e.getMessage());
			return null;
		}
	}

}

@RestController
@RequestMapping("/sys")
class MinicrmAppApplicationController {

	private final ApplicationModules modules;

	public MinicrmAppApplicationController() {
		this.modules = ApplicationModules.of(MinicrmAppApplication.class);
	}

	@GetMapping("/gen-doc")
	public ResponseEntity<String> generateDocumentation() {
		try {
			new Documenter(modules).writeDocumentation();
			return ResponseEntity.ok("Documentation generated!");
		} catch (Exception e) {
			return ResponseEntity.internalServerError()
					.body("Generation failed: " + e.getMessage());
		}
	}

	@GetMapping("/moduleth")
	public String getMethodName() {
		final ApplicationModules modules = ApplicationModules.of(MinicrmAppApplication.class);
		return modules.toString();
	}

}
