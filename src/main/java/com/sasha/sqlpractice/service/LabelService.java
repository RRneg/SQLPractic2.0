package com.sasha.sqlpractice.service;

import com.sasha.sqlpractice.model.Label;
import com.sasha.sqlpractice.repository.LabelRepository;
import com.sasha.sqlpractice.repository.jdbc.JDBCLabelRepositoryImpl;

import java.util.List;

public class LabelService {
    LabelRepository labelrepository = new JDBCLabelRepositoryImpl();

    public Label update(Label label){
        return labelrepository.update(label);
    }

    public Label getById(Integer id){
        return labelrepository.getById(id);
    }

    public void deleteById(Integer id){
        labelrepository.deleteById(id);
    }

    public List<Label> getAll(){
        return labelrepository.getAll();
    }

    public Label saveNewLabel(Label label){
        return labelrepository.save(label);
    }
}
