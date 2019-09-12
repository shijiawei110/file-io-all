package com.sjw.file.io.all.jpa.dao;

import com.sjw.file.io.all.jpa.entity.GuokeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author shijiawei
 * @version GuokeDao.java -> v 1.0
 * @date 2019/7/16
 */
public interface GuokeDao extends JpaRepository<GuokeEntity, Long> {

    @Query(nativeQuery = true, value = "select * from  guoke_article where id = ?1 ")
    GuokeEntity getById(long id);

}
