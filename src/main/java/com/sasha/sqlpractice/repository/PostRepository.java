package com.sasha.sqlpractice.repository;

import com.sasha.sqlpractice.model.Post;

public interface PostRepository<P, I extends Number> extends GenericRepository<Post, Integer>{
}
