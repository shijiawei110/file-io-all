package com.sjw.file.io.all.controller;

import com.sjw.file.io.all.oniondb.OnionDbApp;
import com.sjw.file.io.all.oniondb.common.OnionDbResult;
import com.sjw.file.io.all.oniondb.request.BatchSetRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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
    public OnionDbResult set(@RequestParam("key") String key, @RequestParam("value") String value) {
        return onionDbApp.set(key, value);
    }

    @PostMapping("/write/batchset")
    public OnionDbResult batchSet(@RequestBody List<BatchSetRequest> requests) {
        return onionDbApp.batchSet(requests);
    }

    @GetMapping("/read/get")
    public OnionDbResult set(@RequestParam("key") String key) {
        return onionDbApp.get(key);
    }
}
