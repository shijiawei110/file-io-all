package com.sjw.file.io.all.oniondb.manager;

import com.sjw.file.io.all.oniondb.file.FileSystemTable;
import com.sjw.file.io.all.oniondb.memory.MemoryCachePutResult;
import com.sjw.file.io.all.oniondb.memory.MemoryCacheTable;
import lombok.Data;

import java.io.IOException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Data
public class MainControllerHolder {

    private String position;

    private MemoryCacheTable memoryCacheTable;

    private FileSystemTable fileSystemTable;

    private ReadWriteLock lock;

    private MainControllerHolder() {
    }

    public MainControllerHolder(String position) {
        this.position = position;
        this.memoryCacheTable = new MemoryCacheTable();
        this.fileSystemTable = new FileSystemTable(position, new FilePositionManager());
        this.lock = new ReentrantReadWriteLock();
    }

    public MemoryCachePutResult memoryCachePut(String key, String value) {
        return memoryCacheTable.put(key, value);
    }

    public String memoryCacheGet(String key) {
        return memoryCacheTable.get(key);
    }

    public void fileWrite(MemoryCachePutResult memoryCachePutResult) throws IOException {
        fileSystemTable.write(memoryCachePutResult);
    }

    public String fileGet(String key) throws IOException {
       return fileSystemTable.get(key);
    }


    public void wLock() {
        lock.writeLock().lock();
    }

    public void wUnLock() {
        lock.writeLock().unlock();
    }

    public void rLock() {
        lock.readLock().lock();
    }

    public void rUnLock() {
        lock.readLock().unlock();
    }

}
