package com.sasha.sqlpractic.service;


import com.sasha.sqlpractic.model.Post;
import com.sasha.sqlpractic.repository.PostRepository;
import com.sasha.sqlpractic.repository.jdbc.JDBCPostRepositoryImpl;

import java.util.List;

public class PostService {

    private PostRepository postRepository;

    public PostService() {
        this.postRepository = new JDBCPostRepositoryImpl();
    }

    public PostService(PostRepository postRepositoryConstr) {
        this.postRepository = postRepositoryConstr;
    }


    public Post getById(Integer id) {
        return postRepository.getById(id);
    }

    public Post update(Post post) {
        return postRepository.update(post);
    }

    public void deleteById(Integer id) {
        postRepository.deleteById(id);
    }

    public List<Post> getAll() {
        return postRepository.getAll();
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }


}
