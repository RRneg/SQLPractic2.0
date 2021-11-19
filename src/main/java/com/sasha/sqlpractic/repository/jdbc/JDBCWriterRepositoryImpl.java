package com.sasha.sqlpractic.repository.jdbc;


import com.sasha.sqlpractic.model.Writer;
import com.sasha.sqlpractic.model.Post;
import com.sasha.sqlpractic.model.PostStatus;
import com.sasha.sqlpractic.repository.WriterRepository;
import com.sasha.sqlpractic.utils.JdbcUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JDBCWriterRepositoryImpl implements WriterRepository {
    Writer writer = new Writer();
    JDBCPostRepositoryImpl jdbcPostRepository = new JDBCPostRepositoryImpl();
    Post post = new Post();


    private List<Post> getPostsByWriterId(Integer id) {
        List<Post> posts = null;
        String sql = "SELECT FROM WRITER_POSTS WHERE WRITER_ID=" + id;
        try (PreparedStatement pstm = JdbcUtils.getPrStatement(sql)) {
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                post = jdbcPostRepository.getById(rs.getInt(2));
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
        String sql = "SELECT from WRITERS WHERE ID =?";
        try (PreparedStatement pstm = JdbcUtils.getPrStatement(sql)) {
            pstm.setInt(1,id);
            ResultSet rs = pstm.executeQuery();
            writer.setId(id);
            writer.setFirstName(rs.getString(2));
            writer.setLastName(rs.getString(3));
            writer.setPosts(getPostsByWriterId(id));

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
        return getAllInternal();
    }

    private List<Writer> getAllInternal() {
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
            int affectedRows = pstm.executeUpdate();
            insertPostsInWriterPosts(affectedRows, writer.getPosts());
            writer.setId(affectedRows);
           }
        catch (SQLException e){e.printStackTrace();}
        return writer;
    }

}
