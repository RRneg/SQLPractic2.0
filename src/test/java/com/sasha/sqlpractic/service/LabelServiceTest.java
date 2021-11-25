package com.sasha.sqlpractic.service;

import com.sasha.sqlpractic.model.Label;
import com.sasha.sqlpractic.repository.LabelRepository;
import junit.framework.TestCase;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class LabelServiceTest extends TestCase {
    Label label = new Label();
    List<Label> labels = new ArrayList<>();

    public void setUp(){
        label.setId(1);
        label.setName("John");
        labels.add(label);
    }


    public void testUpdate() {
        LabelRepository labelRepositoryMock = Mockito.mock(LabelRepository.class);
        assertEquals(labelRepositoryMock.update(label), label);

    }

    public void testGetById() {
        LabelRepository labelRepositoryMock = Mockito.mock(LabelRepository.class);
        assertEquals(labelRepositoryMock.getById(label.getId()), label);

    }

    public void testDeleteById() {
        LabelRepository labelRepositoryMock = Mockito.mock(LabelRepository.class);


    }

    public void testGetAll() {
        LabelRepository labelRepositoryMock = Mockito.mock(LabelRepository.class);
        assertEquals(labelRepositoryMock.getAll(), labels);


    }

    public void testSaveNewLabel() {
        LabelRepository labelRepositoryMock = Mockito.mock(LabelRepository.class);
        assertEquals(labelRepositoryMock.save(label), label);

    }
}