package com.sasha.sqlpractic.service;


import com.sasha.sqlpractic.model.Writer;
import com.sasha.sqlpractic.repository.WriterRepository;
import com.sasha.sqlpractic.repository.jdbc.JDBCWriterRepositoryImpl;

import java.util.List;

public class WriterService {

    private WriterRepository writerRepositoryConstr;

    public WriterService(){}

    public WriterService(WriterRepository writerRepositoryConstr){
        this.writerRepositoryConstr = writerRepositoryConstr;
    }


    JDBCWriterRepositoryImpl jdbcWriterRepository = new JDBCWriterRepositoryImpl();

    public Writer getById(Integer id){
        return jdbcWriterRepository.getById(id);
    }

    public Writer update(Writer writer){
        return jdbcWriterRepository.update(writer);
    }

    public void deleteById(Integer id){
        jdbcWriterRepository.getById(id);
    }

    public List<Writer> getAll(){
        return jdbcWriterRepository.getAll();
    }

    public Writer save(Writer writer){
        return jdbcWriterRepository.save(writer);
    }
}
