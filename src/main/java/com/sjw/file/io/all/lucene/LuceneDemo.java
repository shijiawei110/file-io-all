package com.sjw.file.io.all.lucene;

import com.hankcs.lucene.HanLPAnalyzer;
import com.sjw.file.io.all.jpa.dao.GuokeDao;
import com.sjw.file.io.all.jpa.entity.GuokeEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Paths;

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


    @PostConstruct
    private void init() throws IOException {
        // 中文分词分析器
        HanLPAnalyzer analyser = new HanLPAnalyzer();
        // 加入分词器配置
        IndexWriterConfig config = new IndexWriterConfig(analyser);
        // 指定索引的存储目录
//        String usrDir = System.getProperty("user.dir") + "/index";
        String usrDir = "/Users/shijiawei/lucene-index/guoke";
        FSDirectory directory = FSDirectory.open(Paths.get(usrDir));
        // 构造 IndexWriter 对象
        IndexWriter indexWriter = new IndexWriter(directory, config);
        try {
            log.info("-->开始写索引");
            doWrite(indexWriter);
            log.info("-->结束写索引");
        } finally {
            //关闭对象
            indexWriter.close();
            directory.close();
        }
    }


    private void doWrite(IndexWriter indexWriter) throws IOException {
        for (int i = 1; i <= 11748; i++) {
            log.info("开始建立文档 -> id = {}", i);
            GuokeEntity guokeEntity = guokeDao.getById(i);
            Document doc = new Document();
            StringField articleId = new StringField("articleId", String.valueOf(guokeEntity.getArticleId()), Field.Store.YES);
            TextField title = new TextField("title", guokeEntity.getTitle(), Field.Store.YES);
            TextField content = new TextField("content", guokeEntity.getBody(), Field.Store.YES);
            doc.add(articleId);
            doc.add(title);
            doc.add(content);
            indexWriter.addDocument(doc);
            indexWriter.commit();
            log.info("建立文档完成 -> id = {}, articleId = {}", i, guokeEntity.getArticleId());
        }
    }
}
