package com.sasha.sqlpractic.repository.jdbc;


import com.sasha.sqlpractic.model.Label;
import com.sasha.sqlpractic.model.Writer;
import com.sasha.sqlpractic.model.Post;
import com.sasha.sqlpractic.model.PostStatus;
import com.sasha.sqlpractic.repository.WriterRepository;
import com.sasha.sqlpractic.utils.JdbcUtils;
import liquibase.pro.packaged.L;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JDBCWriterRepositoryImpl implements WriterRepository {

    JDBCPostRepositoryImpl jdbcPostRepository = new JDBCPostRepositoryImpl();



    private List<Post> getPostsByWriterId(Integer id) {
        List<Post> posts = null;
        String sql = "SELECT FROM WRITER_POSTS WHERE WRITER_ID=?";
        try (PreparedStatement pstm = JdbcUtils.getPrStatement(sql)) {
            pstm.setInt(1,id);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Post post = jdbcPostRepository.getById(rs.getInt(2));
                if (post.getPostStatus() != PostStatus.DELETED)
                    posts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }


    private void insertPostsInWriterPosts(Integer writerId, List<Post> posts) {
        String sql = "INSERT WRITER_POSTS(WRITER_ID, POST_ID) VALUE (?, ?)";
        try (PreparedStatement pstm = JdbcUtils.getPrStatement(sql)) {
            for (Post post : posts) {
                pstm.setInt(1, writerId);
                pstm.setInt(2, post.getId());
                pstm.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Writer getById(Integer id) {
        Writer writer = new Writer();
        Post post = new Post();
        List<Post> posts = new ArrayList<>();
        Label label = new Label();
        List<Label> labels = new ArrayList<>();

        String sql = "SELECT ID, FIRST_NAME, LAST_NAME FROM Writers WHERE ID=?" +
                " JOIN WRITER_LABELS ON Writers.ID = WRITER_POSTS.WRITER_ID " +
                "JOIN POSTS ON WRITER_POSTS.POST_ID=POSTS.ID " +
                "JOIN POST_LABELS ON POST.ID = POST_LABELS.POST_ID " +
                "JOIN LABELS ON POST_LABELS.LABELS_ID = LABELS.ID";
        try (PreparedStatement pstm = JdbcUtils.getPrStatement(sql)) {
            pstm.setInt(1,id);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                writer.setId(rs.getInt(1));
                writer.setFirstName(rs.getString(2));
                writer.setLastName(rs.getString(3));
            }
            int postId = rs.getInt(4);
            if (!rs.wasNull()) {
                post.setId(postId);
                post.setContent(rs.getString(5));
                post.setCreated(rs.getDate(6).toString());
                post.setUpdated(rs.getDate(7).toString());
                post.setPostStatus(PostStatus.valueOf(rs.getString(8)));
            }
            else {
                int labelId = rs.getInt(9);
                if(!rs.wasNull()){
                  label.setId(labelId);
                  label.setName(rs.getString(10));
                  labels.add(label);
                }
                post.setLabels(labels);
            }
            if (!post.getPostStatus().equals(PostStatus.DELETED)){
                posts.add(post);
            }
            writer.setPosts(posts);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return writer;
    }


    public Writer update(Writer writer) {
        String sql = "UPDATE WRITERS SET FIRST_NAME =? , LAST NAME=? WHERE ID = ?";
        try (PreparedStatement pstm = JdbcUtils.getPrStatement(sql)) {
            pstm.setString(1, writer.getFirstName());
            pstm.setString(2, writer.getLastName());
            pstm.setInt(3, writer.getId());
            pstm.executeUpdate();
            insertPostsInWriterPosts(writer.getId(), writer.getPosts());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return writer;
    }

    public void deleteById(Integer id) {
        String sql = "DELETE FROM WRITERS WHERE ID=?";
        try (PreparedStatement pstm = JdbcUtils.getPrStatement(sql)) {
            pstm.setInt(1, id);
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public List<Writer> getAll() {
        Writer writer = new Writer();
        List<Writer> writers = null;
        String sql = "SELECT * FROM WRITERS";
        try (PreparedStatement pstm = JdbcUtils.getPrStatement(sql)) {
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                writer.setId(rs.getInt(1));
                writer.setFirstName(rs.getString(2));
                writer.setLastName(rs.getString(3));
                writer.setPosts(getPostsByWriterId(rs.getInt(1)));
                writers.add(writer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return writers;
    }


    public Writer save(Writer writer) {
        String sql = "INSERT WRITERS FIRST_NAME=?, LAST_NAME=?";
        try(PreparedStatement pstm = JdbcUtils.getPrStatementBackId(sql)){
            pstm.setString(1, writer.getFirstName());
            pstm.setString(2, writer.getLastName());
            pstm.execute();
            ResultSet rs = pstm.getGeneratedKeys();
            int id = 0;
            if(rs.next()){
                id = rs.getInt(1);
                writer.setId(id);
            }
            insertPostsInWriterPosts(id, writer.getPosts());
            }
        catch (SQLException e){e.printStackTrace();}
        return writer;
    }

}
