package com.sasha.sqlpractic.service;

import com.sasha.sqlpractic.model.Writer;
import com.sasha.sqlpractic.repository.WriterRepository;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

public class WriterServiceTest {


private final WriterRepository writerRepository = mock(WriterRepository.class);
private WriterService writerUnderTest = new WriterService(writerRepository);

    @Test
    public void testGetById(){
        Writer writer = getWriter();
        when(writerRepository.getById(anyInt())).thenReturn(writer);
        assertEquals(writerUnderTest.getById(anyInt()), writer);
    }

    @Test
    public void testUpdate(){
        Writer writer = getWriter();
        when(writerRepository.update(writer)).thenReturn(writer);
        assertEquals(writerUnderTest.update(writer), writer);
    }

    @Test
    public  void testDeleteById(){
        doNothing().when(writerRepository).deleteById(anyInt());
        writerUnderTest.deleteById(anyInt());
        Mockito.verify(writerRepository, Mockito.times(1)).deleteById(anyInt());
    }


    @Test
    public void testGetAll(){
        List<Writer> writers = List.of(getWriter());
        when(writerRepository.getAll()).thenReturn(writers);
        assertEquals(writerUnderTest.getAll(), writers);
    }

    @Test
    public void testSave(){
        Writer writer = getWriter();
        when(writerRepository.save(writer)).thenReturn(writer);
        assertEquals(writerUnderTest.save(writer), writer);
    }

    private Writer getWriter(){
        Writer writer = new Writer();
        writer.setId(1);
        return writer;
    }


}
