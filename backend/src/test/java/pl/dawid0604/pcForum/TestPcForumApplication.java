package pl.dawid0604.pcForum;

import org.springframework.boot.SpringApplication;

public class TestPcForumApplication {

	public static void main(String[] args) {
		SpringApplication.from(PcForumApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
