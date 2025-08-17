package com.english.enky.controller;

import com.english.enky.dto.JoinDto;
import com.english.enky.service.JoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ResponseBody
@RequiredArgsConstructor
public class JoinController {
    private final JoinService joinService;

    @PostMapping("/join")
    public String joinProcess(JoinDto joinDto){
        joinService.joinProcess(joinDto);

        return "회원가입이 완료되었습니다.";
    }
}
