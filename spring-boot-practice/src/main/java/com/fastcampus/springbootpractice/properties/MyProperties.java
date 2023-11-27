package com.fastcampus.springbootpractice.properties;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("my")
// @Configuration  // 스캐닝 유도시키는 용도
// -> Configuration 생략 시 메인에 @ConfigurationPropertiesScan 붙여주면 됨
public class MyProperties {

    private final Integer height;

    public MyProperties(Integer height) {
        this.height = height;
    }   // height가 초기에 instance 만들 때 초기화될 수 있게끔 해서 immutable한 골격이 만들어짐

    public Integer getHeight() {
        return height;
    }


}
