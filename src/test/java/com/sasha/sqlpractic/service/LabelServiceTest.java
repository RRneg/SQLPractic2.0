package com.sasha.sqlpractic.service;


import com.sasha.sqlpractic.model.Label;
import com.sasha.sqlpractic.repository.LabelRepository;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class LabelServiceTest {

    private final LabelRepository labelRepository = mock(LabelRepository.class);
    private LabelService serviceUnderTest = new LabelService(labelRepository);

    @Test
    public void testUpdate() {
        Label label = getLabel();
        when(labelRepository.update(any())).thenReturn(label);
        assertEquals(serviceUnderTest.update(label), label);
    }

    @Test
    public void testGetById() {
        Label label = getLabel();
        when(labelRepository.getById(anyInt())).thenReturn(label);
        assertEquals(labelRepository.getById(label.getId()), label);
    }

    @Test
    public void testDeleteById() {
        doNothing().when(labelRepository).deleteById(anyInt());
        serviceUnderTest.deleteById(anyInt());
        Mockito.verify(labelRepository, Mockito.times(1)).deleteById(anyInt());
    }

    @Test
    public void testGetAll() {
        List<Label> labels = List.of(getLabel());
        when(labelRepository.getAll()).thenReturn(labels);
        assertEquals(serviceUnderTest.getAll(), labels);
    }

    @Test
    public void testSave() {
        Label label = getLabel();
        when(labelRepository.save(label)).thenReturn(label);
        assertEquals(serviceUnderTest.saveNewLabel(label), label);
    }

    private Label getLabel(){
        Label label = new Label();
        label.setId(1);
        label.setName("John");
        return label;
    }
}

