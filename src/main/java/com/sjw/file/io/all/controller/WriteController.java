package com.sjw.file.io.all.controller;

import com.sjw.file.io.all.futil.*;
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
    public void sequenceStream(@RequestParam("size") int size, @RequestParam("num") int num) throws IOException {
        doWrite(size, num, StreamIoUtil.instance, 1, SEQUENCE_WRITE, true);
    }

    /**
     * buffer-io 顺序写
     */
    @GetMapping("/sequence/buffer-io")
    public void sequenceBuffer(@RequestParam("size") int size, @RequestParam("num") int num) throws IOException {
        doWrite(size, num, BufferIoUtil.instance, 2, SEQUENCE_WRITE, true);
    }

    /**
     * filechannel 顺序写
     */
    @GetMapping("/sequence/filechannel")
    public void sequenceFilechannel(@RequestParam("size") int size, @RequestParam("num") int num) throws IOException {
        doWrite(size, num, FileChannelUtil.instance, 3, SEQUENCE_WRITE, true);
    }

    /**
     * mmap 顺序写
     */
    @GetMapping("/sequence/mmap")
    public void sequenceMmap(@RequestParam("size") int size, @RequestParam("num") int num) throws IOException {
        doWrite(size, num, MmapUtil.instance, 4, SEQUENCE_WRITE, true);
    }


    private void doWrite(int size, int num, FileStandardUtil fileStandardUtil, int type, String mode, boolean isSeq) throws IOException {
        File file = FileHelper.getFile(type, mode);
        long duration = 0;
        for (int i = 0; i < num; i++) {
            byte[] bytes = TxtHelper.buildTxtByK(size);
            if (isSeq) {
                long time = fileStandardUtil.sequenceWrite(file, bytes);
                duration += time;
            } else {
                long time = fileStandardUtil.randomWrite(file, bytes);
                duration += time;
            }
        }
        log.info("do {} completed -> duration = {} ms", mode, duration);

    }

}
