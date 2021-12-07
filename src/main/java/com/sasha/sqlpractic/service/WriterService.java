package com.sasha.sqlpractic.service;


import com.sasha.sqlpractic.model.Writer;
import com.sasha.sqlpractic.repository.WriterRepository;
import com.sasha.sqlpractic.repository.jdbc.JDBCWriterRepositoryImpl;

import java.util.List;

public class WriterService {

    private WriterRepository writerRepository;

    public WriterService(){}

    public WriterService(WriterRepository writerRepository){
        this.writerRepository = writerRepository;
    }




    public Writer getById(Integer id){
        return getWriterRepository().getById(id);
    }

    public Writer update(Writer writer){
        return getWriterRepository().update(writer);
    }

    public void deleteById(Integer id){
        getWriterRepository().deleteById(id);
    }

    public List<Writer> getAll(){
        return getWriterRepository().getAll();
    }

    public Writer save(Writer writer){
        return getWriterRepository().save(writer);
    }

    private WriterRepository getWriterRepository(){
        WriterRepository writerRepository = new JDBCWriterRepositoryImpl();
        return writerRepository;
    }
}
