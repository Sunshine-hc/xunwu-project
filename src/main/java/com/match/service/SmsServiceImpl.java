package com.match.service;

import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements ISmsService {
    @Override
    public ServiceResult<String> sendSms(String telephone) {
        return ServiceResult.of("123456");
    }

    @Override
    public String getSmsCode(String telephone) {
        return "123456";
    }

    @Override
    public void remove(String telephone) {

    }
}
