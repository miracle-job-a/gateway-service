package com.miracle.memberservice.controller.json;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class JsonController {

    private static final Logger logger = LoggerFactory.getLogger(JsonController.class);

    @PostMapping(value = "/company/bno")
    public String bnoCertify(@RequestParam String bno){
        logger.info(bno);


        return bno;
    }
}
