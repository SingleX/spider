package net.singlex;

import net.singlex.simple.GithubRepoPageProcessor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import us.codecraft.webmagic.Spider;

@SpringBootApplication
public class SpiderApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpiderApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
    }

    void initGithub() {
        System.out.println("github page init ...");


    }
}
