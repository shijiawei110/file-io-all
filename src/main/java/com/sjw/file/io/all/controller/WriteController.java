package com.sjw.file.io.all.controller;

import com.sjw.file.io.all.futil.BufferIoUtil;
import com.sjw.file.io.all.futil.FileChannelUtil;
import com.sjw.file.io.all.futil.MmapUtil;
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
        StreamIoUtil.instance.sequenceWrite(file, bytes);
    }

    /**
     * buffer-io 顺序写
     */
    @GetMapping("/sequence/buffer-io")
    public void sequenceBuffer(@RequestParam("size") int size) throws IOException {
        byte[] bytes = TxtHelper.buildTxtByK(size);
        File file = FileHelper.getFile(2, SEQUENCE_WRITE);
        BufferIoUtil.instance.sequenceWrite(file, bytes);
    }

    /**
     * filechannel 顺序写
     */
    @GetMapping("/sequence/filechannel")
    public void sequenceFilechannel(@RequestParam("size") int size) throws IOException {
        byte[] bytes = TxtHelper.buildTxtByK(size);
        File file = FileHelper.getFile(3, SEQUENCE_WRITE);
        FileChannelUtil.instance.sequenceWrite(file, bytes);
    }

    /**
     * mmap 顺序写
     */
    @GetMapping("/sequence/mmap")
    public void sequenceMmap(@RequestParam("size") int size) throws IOException {
        byte[] bytes = TxtHelper.buildTxtByK(size);
        File file = FileHelper.getFile(4, SEQUENCE_WRITE);
        MmapUtil.instance.sequenceWrite(file, bytes);
    }

}
