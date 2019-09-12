package com.sjw.file.io.all.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.FSDirectory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author shijiawei
 * @version LuceneController.java -> v 1.0
 * @date 2019/5/29
 */
@RestController
@RequestMapping("/lucene")
@Slf4j
public class LuceneController {


    /**
     * stream 顺序写
     */
    @GetMapping("/search")
    public void getIndex(@RequestParam("str") String str) throws IOException {
        String usrDir = System.getProperty("user.dir") + "/index";
        FSDirectory directory = FSDirectory.open(Paths.get(usrDir));
        DirectoryReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        TermQuery query = new TermQuery(new Term("title", "迷思"));
        try {
            ScoreDoc[] hits = searcher.search(query, 10).scoreDocs;
            for (ScoreDoc hit : hits) {
                Document doc = searcher.doc(hit.doc);
                log.info("title = {} , content = {}", doc.get("title"), doc.get("content"));
            }
        } finally {
            directory.close();
            reader.close();
        }
    }

}
