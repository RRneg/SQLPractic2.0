package com.sasha.sqlpractice.service;


import com.sasha.sqlpractice.model.Post;
import com.sasha.sqlpractice.repository.PostRepository;
import com.sasha.sqlpractice.repository.jdbc.JDBCPostRepositoryImpl;

import java.util.List;

public class PostService {
    private PostRepository postRepository = new JDBCPostRepositoryImpl();

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

    public Post saveNewPost(Post post){
        return postRepository.save(post);
    }
}
