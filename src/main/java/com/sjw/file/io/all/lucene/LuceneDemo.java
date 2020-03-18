package com.sjw.file.io.all.lucene;

import com.hankcs.lucene.HanLPAnalyzer;
import com.sjw.file.io.all.jpa.dao.GuokeDao;
import com.sjw.file.io.all.jpa.entity.GuokeEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author shijiawei
 * @version LuceneDemo.java -> v 1.0
 * @date 2019/9/10
 */
@Component
@Slf4j
public class LuceneDemo {

    @Resource
    private GuokeDao guokeDao;

    private IndexWriter indexWriter;

    private FSDirectory directory;


//    @PostConstruct
    private void init() throws IOException {
        // 中文分词分析器
        HanLPAnalyzer analyser = new HanLPAnalyzer();
        // 加入分词器配置
        IndexWriterConfig config = new IndexWriterConfig(analyser);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        // 指定索引的存储目录
//        String usrDir = System.getProperty("user.dir") + "/index";
        String usrDir = "/Users/shijiawei/lucene-index/guoke";
        directory = FSDirectory.open(Paths.get(usrDir));
        // 构造 IndexWriter 对象
        indexWriter = new IndexWriter(directory, config);
        ExecutorService pool = Executors.newSingleThreadExecutor();
        pool.execute(this::writeIndex);
    }

    private void writeIndex() {
        log.info("-->开始写索引");
        try {
            doWrite();
            //关闭对象
            indexWriter.close();
            directory.close();
        } catch (Exception e) {
            log.error("写索引error", e);
        }
        log.info("-->结束写索引");
    }


    private void doWrite() throws IOException {
        for (int i = 1; i <= 11748; i++) {
            log.info("开始建立文档 -> id = {}", i);
            GuokeEntity guokeEntity = guokeDao.getById(i);
            if (null == guokeEntity) {
                continue;
            }
            Document doc = new Document();
            //int point只索引不存储，所以加一个只存储不索引的storeField
            IntPoint articleIdIndex = new IntPoint("articleIdIndex", guokeEntity.getArticleId().intValue());
            StoredField articleId = new StoredField("articleId", guokeEntity.getArticleId().intValue());
            TextField title = new TextField("title", guokeEntity.getTitle(), Field.Store.YES);
            TextField content = new TextField("content", guokeEntity.getBody(), Field.Store.YES);
            doc.add(articleId);
            doc.add(articleIdIndex);
            doc.add(title);
            doc.add(content);
            indexWriter.addDocument(doc);
            indexWriter.commit();
            log.info("建立文档完成 -> id = {}, articleId = {}", i, guokeEntity.getArticleId());
        }
    }

}
