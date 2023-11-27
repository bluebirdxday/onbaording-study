package com.fastcampus.springbootpractice;

import com.fastcampus.springbootpractice.properties.MyProperties;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

@ConfigurationPropertiesScan
@SpringBootApplication
public class SpringBootPracticeApplication {

    private final Integer height;
    private final Environment environment;
    private final ApplicationContext applicationContext;
    private final MyProperties myProperties;

    public SpringBootPracticeApplication(
            @Value("${my.height}") Integer height,
            Environment environment,
            ApplicationContext applicationContext,
            MyProperties myProperties) {
        this.height = height;
        this.environment = environment;
        this.applicationContext = applicationContext;
        this.myProperties = myProperties;
    }
    // 생성자 주입을 해서 height 초기화시켜 height 필드에 final을 사용할 수 있도록 함
    // 바로 height를 가져다가 쓸 수 있게 됨



    public static void main(String[] args) {
        SpringApplication.run(SpringBootPracticeApplication.class, args);
    }

    @PostConstruct
    public void init(){
        System.out.println("@[Value] " + height);
        System.out.println("[Environment] " + environment.getProperty("my.height")); 
        // -> 틀려도 컴파일 타임에서 잡아내기는 어려움
        System.out.println("[ApplicationContext] " + applicationContext.getEnvironment().getProperty("my.height"));
        System.out.println("[configurationProps] " + myProperties.getHeight());
        // -> 자바코드로 되어있으니 명확해보임, 잘못 쓰면 컴파일 에러로 이어짐
    }

}
