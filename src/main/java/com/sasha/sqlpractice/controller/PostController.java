package com.sasha.sqlpractice.controller;


import com.sasha.sqlpractice.model.Label;
import com.sasha.sqlpractice.model.Post;
import com.sasha.sqlpractice.service.PostService;

import java.util.List;

public class PostController {
    PostService postService = new PostService();
    LabelController labelController = new LabelController();


    public Post saveNewPost(String content, List <Label> labels) {
       return postService.saveNewPost(content, labels);
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
