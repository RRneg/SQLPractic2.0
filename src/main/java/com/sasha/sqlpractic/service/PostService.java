package com.sasha.sqlpractic.service;


import com.sasha.sqlpractic.model.Post;
import com.sasha.sqlpractic.repository.PostRepository;
import com.sasha.sqlpractic.repository.jdbc.JDBCPostRepositoryImpl;

import java.util.List;

public class PostService {

    private PostRepository postRepository;

    public PostService(){
    }

    public PostService(PostRepository postRepositoryConstr){
        this.postRepository = postRepositoryConstr;
    }



    public Post getById(Integer id){
        return getPostRepository().getById(id);
    }

    public Post update(Post post){
        return getPostRepository().update(post);
    }

    public void deleteById(Integer id){getPostRepository().deleteById(id);}

    public List<Post> getAll(){
        return getPostRepository().getAll();
    }

    public Post save(Post post){
        return getPostRepository().save(post);
    }

    private PostRepository getPostRepository(){
        PostRepository postRepository = new JDBCPostRepositoryImpl();
        return postRepository;
    }
}
