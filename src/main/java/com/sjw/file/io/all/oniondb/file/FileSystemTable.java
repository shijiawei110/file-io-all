package com.sjw.file.io.all.oniondb.file;

import com.sjw.file.io.all.oniondb.common.ParamConstans;
import com.sjw.file.io.all.oniondb.helper.NodeSerializeHelper;
import com.sjw.file.io.all.oniondb.index.OnionDbTableIndex;
import com.sjw.file.io.all.oniondb.manager.FilePositionManager;
import com.sjw.file.io.all.oniondb.memory.MemoryCachePutResult;
import com.sjw.file.io.all.oniondb.pojo.DbNodePojo;
import com.sjw.file.io.all.oniondb.utils.ByteUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author shijiawei
 * @version FileSystemTable.java -> v 1.0
 * @date 2020/2/28
 * 对应文件系统表
 */
@Slf4j
public class FileSystemTable {

    private static final FileChannelImpl fileChannel = FileChannelImpl.getInstance();

    //文件位置管理者
    private FilePositionManager filePositionManager;

    //该实例对应的桶的位置
    private String position;

    public FileSystemTable(String position, FilePositionManager filePositionManager) {
        this.position = position;
        this.filePositionManager = filePositionManager;
        //读取目前文件位置
        filePositionManager.init(Integer.parseInt(position));
    }

    public void write(MemoryCachePutResult memoryCachePutResult) throws IOException {
        //协议序列化
        ByteBuffer byteBuffer = NodeSerializeHelper.serializeByteBuffer(memoryCachePutResult.getFullData(), memoryCachePutResult.getFullDataSize());
        //写入磁盘
        fileChannel.sequenceWrite(filePositionManager.getCurrentFile(), byteBuffer);
        //写入索引
        filePositionManager.batchSetIndex(memoryCachePutResult.getIndexData(), memoryCachePutResult.getFullDataSize());
        //检查是否移动文件指针 -> 是的话移动并且创建新的内存索引
        checkFileFullAndForwardIndex(memoryCachePutResult.getFullDataSize());
    }

    public String get(String key) throws IOException {
        //递归 读出节点数据
        DbNodePojo dbNodePojo = searchFileNode(key, filePositionManager.getCurrentIndex());
        if (null == dbNodePojo) {
            //找不到key
            return null;
        }
        //节点自检查
        dbNodePojo.checkKey(key);
        return dbNodePojo.getValue();
    }

    /**
     * 根据key搜索 这个key所在的索引以及文件的index位置是哪个 ：递归
     */
    private DbNodePojo searchFileNode(String key, int currentIndex) throws IOException {
        if (currentIndex <= 0) {
            return null;
        }
        OnionDbTableIndex indexCache = filePositionManager.getIndexCacheByKey(currentIndex);
        Integer offset = indexCache.getIndex(key);
        if (null == offset || offset < 0) {
            return searchFileNode(key, currentIndex - 1);
        }
        return readFileNodeData(offset, filePositionManager.getFileByFileIndex(currentIndex));
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

    /**
     * 检查是否需要更新文件index
     */
    private synchronized void checkFileFullAndForwardIndex(int dataSize) {
        //文件index + 1
        File currentFile = filePositionManager.getCurrentFile();
        if (currentFile.exists() && currentFile.length() > ParamConstans.DB_TABLE_FILE_MAX_BYTE_SIZE) {
            log.info("file full so new a db file -> position = {} fileLength = {} dataSize = {}", position, currentFile.length(), dataSize);
            filePositionManager.forwardIndex();
            //生成新的索引cache
            filePositionManager.createNewIndexCache();
        }
    }


}
