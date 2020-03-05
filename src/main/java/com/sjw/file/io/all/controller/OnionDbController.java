package com.sjw.file.io.all.controller;

import com.sjw.file.io.all.oniondb.OnionDbApp;
import com.sjw.file.io.all.oniondb.common.OnionDbResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author shijiawei
 * @version OnionDbController.java -> v 1.0
 * @date 2020/3/3
 */
@RestController
@RequestMapping("/oniondb")
@Slf4j
public class OnionDbController {

    @Resource
    private OnionDbApp onionDbApp;

    @GetMapping("/write/set")
    public OnionDbResult set(@RequestParam("key") String key, @RequestParam("value") Object value) {
        log.info("onion db set action -> key = {} | value = {}", key, value);
        return onionDbApp.set(key, value);
    }
}
