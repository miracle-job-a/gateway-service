package com.miracle.memberservice.controller.json;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class JsonController {

    @PostMapping(value = "/company/bno")
    public String bnoCertify(@RequestParam String bno){
        System.out.println(bno);
        return bno;
    }
}
