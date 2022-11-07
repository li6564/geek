package cn.lico.geek;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.lico.geek.modules.*.mapper")
public class GeekApplication {
    public static void main(String[] args) {
        SpringApplication.run(GeekApplication.class,args);
    }
}
