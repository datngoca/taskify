package com.example.taskify_backend.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean // Định nghĩa một Bean (đối tượng được quản lý bởi Spring)
    public ModelMapper modelMapper() {
        // Tạo đối tượng ModelMapper và có thể cấu hình các quy tắc mapping tại đây
        ModelMapper modelMapper = new ModelMapper();

        // Ví dụ cấu hình sử dụng Strict Matching Strategy
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return modelMapper;
    }
}
