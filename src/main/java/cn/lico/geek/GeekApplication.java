package cn.lico.geek;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@MapperScan("cn.lico.geek.modules.*.mapper")
@EnableElasticsearchRepositories(basePackages = "cn.lico.geek.modules.search")
public class GeekApplication {
    public static void main(String[] args) {
        SpringApplication.run(GeekApplication.class,args);
    }
}
