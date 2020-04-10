package com.sjw.file.io.all.oniondb.file;

import com.sjw.file.io.all.oniondb.common.ParamConstans;
import com.sjw.file.io.all.oniondb.exception.OnionDbException;
import com.sjw.file.io.all.oniondb.helper.FileHelper;
import com.sjw.file.io.all.oniondb.helper.NodeSerializeHelper;
import com.sjw.file.io.all.oniondb.index.OnionDbTableIndex;
import com.sjw.file.io.all.oniondb.manager.FilePositionManager;
import com.sjw.file.io.all.oniondb.memory.MemoryCachePutResult;
import com.sjw.file.io.all.oniondb.pojo.DbNodePojo;
import com.sjw.file.io.all.oniondb.utils.ByteUtils;
import com.sjw.file.io.all.oniondb.utils.NumberUtil;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author shijiawei
 * @version FileSystemTable.java -> v 1.0
 * @date 2020/2/28
 * 对应文件系统表
 */
public class FileSystemTable {

    private static final FileChannelImpl fileChannel = FileChannelImpl.getInstance();

    //该实例持有的索引实例
    private OnionDbTableIndex denseIndex;

    //文件位置管理者
    private FilePositionManager filePositionManager;

    //该实例对应的桶的位置
    private String position;

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public FileSystemTable(String position, OnionDbTableIndex denseIndex, FilePositionManager filePositionManager) {
        this.position = position;
        this.denseIndex = denseIndex;
        this.filePositionManager = filePositionManager;
        //读取目前文件位置
        filePositionManager.init(Integer.parseInt(position));
    }

    public void write(MemoryCachePutResult memoryCachePutResult) throws IOException {
        try {
            //协议序列化
            ByteBuffer byteBuffer = NodeSerializeHelper.serializeByteBuffer(memoryCachePutResult.getFullData(), memoryCachePutResult.getFullDataSize());
            lock.writeLock().lock();
            //写入磁盘
            fileChannel.sequenceWrite(getCurrentFile(), byteBuffer);
            //写入索引
            denseIndex.setIndexMap(memoryCachePutResult.getIndexData());
        } finally {
            lock.writeLock().unlock();
        }
    }

    public String get(String key) throws IOException {
        try {
            lock.readLock().lock();
            //取索引offset位置
            Integer offset = denseIndex.getIndex(key);
            if (NumberUtil.invalidNumber(offset)) {
                throw OnionDbException.DB_INDEX_ERROR;
            }
            //读出节点数据
            DbNodePojo dbNodePojo = readFileNodeData(offset, getCurrentFile());
            //节点自检查
            dbNodePojo.checkKey(key);
            return dbNodePojo.getValue();
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * 根据索引获取
     */
    private DbNodePojo readFileNodeData(int offset, File file) throws IOException {
        //读取key长度
        byte[] keySizeBytes = fileChannel.randomRead(file, offset, ParamConstans.KEY_FALG_BYTE_NUM);
        int keySize = ByteUtils.getByteInt(keySizeBytes[0]);
        offset += ParamConstans.KEY_FALG_BYTE_NUM;
        //读取key数据
        byte[] keyData = fileChannel.randomRead(file, offset, keySize);
        offset += keySize;
        //读取value长度
        byte[] valueSizeBytes = fileChannel.randomRead(file, offset, ParamConstans.VALUE_FALG_BYTE_NUM);
        int valueSize = ByteUtils.getBytesInt(valueSizeBytes);
        offset += ParamConstans.VALUE_FALG_BYTE_NUM;
        //读取value数据
        byte[] valueData = fileChannel.randomRead(file, offset, valueSize);
        DbNodePojo dbNodePojo = new DbNodePojo(keySize, keyData, valueSize, valueData);
        return dbNodePojo;
    }

    private File getCurrentFile() {
        return FileHelper.getCurrentFile(Integer.parseInt(position), filePositionManager.getCurrentIndex());
    }

}
