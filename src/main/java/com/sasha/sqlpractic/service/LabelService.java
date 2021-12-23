package com.sasha.sqlpractic.service;

import com.sasha.sqlpractic.model.Label;
import com.sasha.sqlpractic.repository.LabelRepository;
import com.sasha.sqlpractic.repository.jdbc.JDBCLabelRepositoryImpl;

import java.util.List;

public class LabelService {

    private LabelRepository labelRepository;

    public LabelService() {
        this.labelRepository = new JDBCLabelRepositoryImpl();
    }

    public LabelService(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }


    public Label update(Label label) {
        return labelRepository.update(label);
    }

    public Label getById(Integer id) {
        return labelRepository.getById(id);
    }

    public void deleteById(Integer id) {
        labelRepository.deleteById(id);
    }

    public List<Label> getAll() {
        return labelRepository.getAll();
    }

    public Label saveNewLabel(Label label) {
        return labelRepository.save(label);
    }

}
