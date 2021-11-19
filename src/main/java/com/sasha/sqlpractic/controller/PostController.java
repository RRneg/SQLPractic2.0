package com.sasha.sqlpractic.controller;


import com.sasha.sqlpractic.model.Post;
import com.sasha.sqlpractic.service.PostService;

import java.util.List;

public class PostController {
    PostService postService = new PostService();
    LabelController labelController = new LabelController();


    public Post saveNewPost(Post post) {
       return postService.saveNewPost(post);
    }


    public List<Post> getAllPosts() {
        return postService.getAll();
    }

    public Post update(Post post){
        return postService.update(post);
    }

    public Post getPostById(Integer id) {
        return postService.getById(id);
    }

    public void deleteById(Integer id){
        postService.deleteById(id);
    }
}
