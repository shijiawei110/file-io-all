package com.sjw.file.io.all.utils;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author shijiawei
 * @version LuceneUtil.java -> v 1.0
 * @date 2019/9/17
 */
public class LuceneUtil {
    private static final String INDEX_PATH = "/Users/shijiawei/lucene-index/guoke";


    public static void main(String[] args) throws IOException {
        phraseQuery();
    }

    /**
     * termquery 是基础查询,是不可分割的一组字符串取查询匹配
     */
    private static void termQuery() throws IOException {
        IndexSearcher searcher = getSearcher();
        TermQuery query = new TermQuery(new Term("content", "杂种优势"));
        ScoreDoc[] hits = searcher.search(query, 100).scoreDocs;
        printDoc(hits, searcher);
    }

    /**
     * prefixQuery是前缀查询  比如你查一个 动 ,可以查出 动物,动作 等的索引
     */
    private static void prefixQuery() throws IOException {
        IndexSearcher searcher = getSearcher();
        PrefixQuery query = new PrefixQuery(new Term("title", "科技"));
        ScoreDoc[] hits = searcher.search(query, 100).scoreDocs;
        printDoc(hits, searcher);
    }

    /**
     * NumericRangeQuery 数值范围查询
     */
    private static void numericRangeQuery() throws IOException {
        IndexSearcher searcher = getSearcher();
        Query query = IntPoint.newRangeQuery("articleIdIndex", 449500, 449599);
        ScoreDoc[] hits = searcher.search(query, 100).scoreDocs;
        printDoc(hits, searcher);
    }


    /**
     * PhraseQuery 短语查询
     */
    private static void phraseQuery() throws IOException {
        IndexSearcher searcher = getSearcher();
        //slop是 北京和大学之间最多间隔几个字 比如 北京工业大学就可以被查出来
        PhraseQuery query = new PhraseQuery.Builder()
                .add(new Term("title", "土地"))
                .add(new Term("title", "状况"))
                .setSlop(4)
                .build();
        ScoreDoc[] hits = searcher.search(query, 100).scoreDocs;
        printDoc(hits, searcher);
    }



    private static IndexSearcher getSearcher() throws IOException {
        FSDirectory directory = FSDirectory.open(Paths.get(INDEX_PATH));
        DirectoryReader reader = DirectoryReader.open(directory);
        return new IndexSearcher(reader);
    }

    private static void printDoc(ScoreDoc[] hits, IndexSearcher searcher) throws IOException {
        for (ScoreDoc hit : hits) {
            Document doc = searcher.doc(hit.doc);
            System.out.println("articleId = " + doc.get("articleId") + ", score = " + hit.score + ", title = " + doc.get("title") + ", content = " + doc.get("content"));
        }
    }
}
