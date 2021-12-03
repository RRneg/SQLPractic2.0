package com.sasha.sqlpractic.service;

import com.sasha.sqlpractic.model.Label;
import com.sasha.sqlpractic.repository.LabelRepository;
import com.sasha.sqlpractic.repository.jdbc.JDBCLabelRepositoryImpl;

import java.util.List;

public class LabelService {

    private LabelRepository labelRepository;

    public LabelService(){
    }

    public LabelService(LabelRepository labelRepository){
        this.labelRepository = labelRepository;
    }





    public Label update(Label label){return getLabelRepository().update(label);
    }

    public Label getById(Integer id){
        return getLabelRepository().getById(id);
    }

    public void deleteById(Integer id){
        getLabelRepository().deleteById(id);
    }

    public List<Label> getAll(){
        return getLabelRepository().getAll();
    }

    public Label saveNewLabel(Label label){
        return getLabelRepository().save(label);
    }

    private LabelRepository getLabelRepository(){
        LabelRepository labelRepository = new JDBCLabelRepositoryImpl();
        return labelRepository;
    }
}
