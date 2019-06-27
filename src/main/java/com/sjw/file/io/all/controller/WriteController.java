package com.sjw.file.io.all.controller;

import com.sjw.file.io.all.futil.StreamIoUtil;
import com.sjw.file.io.all.helper.FileHelper;
import com.sjw.file.io.all.helper.TxtHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

/**
 * @author shijiawei
 * @version Controller.java -> v 1.0
 * @date 2019/5/29
 */
@RestController
@RequestMapping("/write")
@Slf4j
public class WriteController {

    private static final String SEQUENCE_WRITE = "seq-write";



    /**
     * stream 顺序写
     */
    @GetMapping("/sequence/stream-io")
    public void sequenceStream(@RequestParam("size") int size) throws IOException {
        byte[] bytes = TxtHelper.buildTxtByK(size);
        File file = FileHelper.getFile(1, SEQUENCE_WRITE);
        StreamIoUtil.instance.sequenceWrite(file,bytes);
    }
}
