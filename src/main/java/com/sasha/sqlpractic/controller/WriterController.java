package com.sasha.sqlpractic.controller;



import com.sasha.sqlpractic.model.Writer;
import com.sasha.sqlpractic.service.WriterService;

import java.util.List;

public class WriterController {
    private WriterService writerService = new WriterService();


    public Writer newWriter(Writer writer) { return writerService.saveNewWriter(writer);}

    public Writer update(Writer writer) {return writerService.update(writer);}

    public void delete(Integer id) {writerService.deleteById(id);
    }

    public List<Writer> getAll() {return writerService.getAll();}


    public Writer getById(Integer id) {return writerService.getById(id);}
}
