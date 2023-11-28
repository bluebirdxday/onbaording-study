package com.fastcampus.springboot.getinline.config;

import com.fastcampus.springboot.getinline.repository.EventRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

    @Bean
    public EventRepository eventRepository(){
        return new EventRepository() {};
    } //-> 구현자를 bean으로 등록할 수 있는 방법을 스프링에게 가르쳐줌
}
