package com.sasha.sqlpractic.service;

import com.sasha.sqlpractic.model.Post;
import com.sasha.sqlpractic.repository.PostRepository;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

public class PostServiceTest {


    private final PostRepository postRepository = mock(PostRepository.class);
    private PostService postUnderTest = new PostService(postRepository);

    @Test
    public void testGetById(){
        Post post = getPost();
        when(postRepository.getById(anyInt())).thenReturn(post);
        assertEquals(postUnderTest.getById(post.getId()), post);
    }

    @Test
    public void testUpdate(){
        Post post = getPost();
        when(postRepository.update(post)).thenReturn(post);
        assertEquals(postUnderTest.update(post), post);
    }

    @Test
    public void testDeleteById(){
        doNothing().when(postRepository).deleteById(anyInt());
        postUnderTest.deleteById(anyInt());
        Mockito.verify(postRepository, Mockito.times(1)).deleteById(anyInt());
    }

    @Test
    public void testGetAll(){
        List<Post> posts = List.of(getPost());
        when(postRepository.getAll()).thenReturn(posts);
        assertEquals(postUnderTest.getAll(), posts);
    }

    @Test
    public void testSave(){
        Post post = getPost();
        when(postRepository.save(post)).thenReturn(post);
        assertEquals(postUnderTest.save(post), post);
    }

    private Post getPost(){
        Post post = new Post();
        post.setId(1);
        return post;
    }

}
