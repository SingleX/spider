package net.singlex;

import net.singlex.simple.GithubRepoPageProcessor;
import net.singlex.simple.SingleXProcessor;
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
//        initGithub();
        initSingleX();
    }

    void initGithub(){
        System.out.println("github page init ...");
        Spider.create(new GithubRepoPageProcessor()).addUrl("https://github.com/code4craft").thread(1).run();

    }

    void initSingleX(){
        Spider.create(new SingleXProcessor()).addUrl("http://www.singlex.net/wp-admin/").thread(1).run();
    }
}
