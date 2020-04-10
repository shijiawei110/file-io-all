package com.sjw.file.io.all.oniondb.manager;

import com.google.common.collect.Maps;
import com.sjw.file.io.all.oniondb.common.ParamConstans;
import com.sjw.file.io.all.oniondb.file.FileSystemTable;
import com.sjw.file.io.all.oniondb.index.DenseIndex;
import com.sjw.file.io.all.oniondb.memory.MemoryCacheTable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * hash桶位置总管理者
 */
@Component
public class PositionManager {

    private Map<String, MemoryCacheTable> memoryCacheTables;

    private Map<String, FileSystemTable> fileSystemTables;

    /**
     * 内存表初始化
     */
    @PostConstruct
    public void init() {
        //内存表
        memoryCacheTables = Maps.newHashMap();
        //磁盘表
        fileSystemTables = Maps.newHashMap();
        //init
        for (int i = 0; i < ParamConstans.HASH_SIZE; i++) {
            String position = String.valueOf(i);
            memoryCacheTables.put(position, new MemoryCacheTable());
            fileSystemTables.put(position, new FileSystemTable(position, new DenseIndex(), new FilePositionManager()));
        }
    }


    public MemoryCacheTable getMemoryCacheTable(int position) {
        return memoryCacheTables.get(String.valueOf(position));
    }

    public FileSystemTable getFileSystemTable(int position) {
        return fileSystemTables.get(String.valueOf(position));
    }


}
