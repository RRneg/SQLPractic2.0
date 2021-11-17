package com.sasha.sqlpractice.service;


import com.sasha.sqlpractice.model.Label;
import com.sasha.sqlpractice.model.Post;
import com.sasha.sqlpractice.repository.jdbc.JDBCPostRepositoryImpl;

import java.util.List;

public class PostService {
    private JDBCPostRepositoryImpl postRepository = new JDBCPostRepositoryImpl();

    public Post getById(Integer id){
        return postRepository.getById(id);
    }

    public Post update(Post post){
        return postRepository.update(post);
    }

    public void deleteById(Integer id){postRepository.deleteById(id);}

    public List<Post> getAll(){
        return postRepository.getAll();
    }

    public Post saveNewPost(String content, List<Label> labels){
        return postRepository.saveNew(content, labels);
    }
}
