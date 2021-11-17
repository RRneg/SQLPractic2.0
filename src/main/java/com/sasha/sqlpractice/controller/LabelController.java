package com.sasha.sqlpractice.controller;


import com.sasha.sqlpractice.model.Label;
import com.sasha.sqlpractice.service.LabelService;

import java.util.List;

public class LabelController {
    LabelService labelService = new LabelService();

    public Label saveNewLabel(String name) {
        Label label = new Label();
        label.setName(name);
       return labelService.saveNewLabel(label);
    }

    public List<Label> getAll() {
        return labelService.getAll();
    }

    public Label updateLabel(Label label) {
       return labelService.update(label);
    }

    public void deleteById(Integer id) {
        labelService.deleteById(id);
    }

    public Label getById(Integer id) {
        return labelService.getById(id);
    }
}
