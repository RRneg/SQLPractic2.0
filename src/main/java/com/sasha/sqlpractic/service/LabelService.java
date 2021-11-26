package com.sasha.sqlpractic.service;

import com.sasha.sqlpractic.model.Label;
import com.sasha.sqlpractic.repository.LabelRepository;
import com.sasha.sqlpractic.repository.jdbc.JDBCLabelRepositoryImpl;

import java.util.List;

public class LabelService {

    private LabelRepository labelRepositoryConstr;

    public LabelService(){
    }

    public LabelService(LabelRepository labelRepositoryConstr){
        this.labelRepositoryConstr = labelRepositoryConstr;
    }



    LabelRepository labelrepository = new JDBCLabelRepositoryImpl();

    public Label update(Label label){return labelrepository.update(label);
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
