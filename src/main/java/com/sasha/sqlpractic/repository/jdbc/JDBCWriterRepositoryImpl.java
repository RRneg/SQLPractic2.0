package com.sasha.sqlpractic.repository.jdbc;


import com.sasha.sqlpractic.model.Label;
import com.sasha.sqlpractic.model.Writer;
import com.sasha.sqlpractic.model.Post;
import com.sasha.sqlpractic.model.PostStatus;
import com.sasha.sqlpractic.repository.PostRepository;
import com.sasha.sqlpractic.repository.WriterRepository;
import com.sasha.sqlpractic.utils.JdbcUtils;
import liquibase.pro.packaged.L;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JDBCWriterRepositoryImpl implements WriterRepository {

    private void insertPostsInWriterPosts(Integer writerId, List<Post> posts) {
        for (Post post : posts) {
            String sql = "INSERT WRITER_POSTS(WRITER_ID, POST_ID) VALUE (?, ?)";
            try (PreparedStatement pstm = JdbcUtils.getPrStatement(sql)) {
                pstm.setInt(1, writerId);
                pstm.setInt(2, post.getId());
                pstm.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public Writer getById(Integer id) {
        Writer writer = new Writer();
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT * FROM writers JOIN writer_posts ON writers.ID = writer_posts.WRITER_ID JOIN posts ON writer_posts.POST_ID=posts.ID JOIN post_labels ON posts.ID = post_labels.POST_ID JOIN labels ON post_labels.LABELS_ID = labels.ID WHERE posts.POST_STATUS not like \"DELETE\" and writers.ID = ?";
        try (PreparedStatement pstm = JdbcUtils.getPrStatement(sql)) {
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                writer.setId(rs.getInt(1));
                writer.setFirstName(rs.getString(2));
                writer.setLastName(rs.getString(3));
                List<Label> labels = new ArrayList<>();
                labels.add(new Label(rs.getInt(13), rs.getString(14)));
                posts.add(new Post(
                        rs.getInt(6),
                        rs.getString(7),
                        rs.getDate(8).toString(),
                        rs.getDate(9).toString(),
                        labels,
                        PostStatus.valueOf(rs.getString(10))));
            }

            while (rs.next()) {
                List<Label> labels1 = new ArrayList<>();
                labels1.add(new Label(rs.getInt(13), rs.getString(14)));
                posts.add(new Post(
                        rs.getInt(6),
                        rs.getString(7),
                        rs.getDate(8).toString(),
                        rs.getDate(9).toString(),
                        labels1,
                        PostStatus.valueOf(rs.getString(10))));

            }
            JDBCPostRepositoryImpl postRepository = new JDBCPostRepositoryImpl();
            writer.setPosts(postRepository.getAllProcessed(posts));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return writer;
    }


    public Writer update(Writer writer) {
        String sql = "UPDATE WRITERS SET FIRST_NAME =? , LAST_NAME=? WHERE ID = ?";
        String sql2 = String.format("DELETE FROM writer_posts where WRITER_ID = %d", writer.getId());
        try (PreparedStatement pstm = JdbcUtils.getPrStatement(sql)) {
            pstm.setString(1, writer.getFirstName());
            pstm.setString(2, writer.getLastName());
            pstm.setInt(3, writer.getId());
            pstm.executeUpdate();
            pstm.execute(sql2);
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
        List<Writer> writers = new ArrayList<>();
        String sql = "SELECT * FROM writers JOIN writer_posts ON writers.ID = writer_posts.WRITER_ID JOIN posts ON writer_posts.POST_ID=posts.ID JOIN post_labels ON posts.ID = post_labels.POST_ID JOIN labels ON post_labels.LABELS_ID = labels.ID WHERE posts.POST_STATUS not like \"DELETE\" order by writers.ID, posts.ID";
        try (PreparedStatement pstm = JdbcUtils.getPrStatement(sql)) {
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                List<Label> labels = new ArrayList<>();
                labels.add(new Label(rs.getInt(13),
                        rs.getString(14)));

                List<Post> posts = new ArrayList<>();
                posts.add(new Post(rs.getInt(6),
                        rs.getString(7),
                        rs.getDate(8).toString(),
                        rs.getDate(9).toString(),
                        labels,
                        PostStatus.valueOf(rs.getString(10))));

                writers.add(new Writer(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        posts));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return getAllProcessed(writers);
    }

    private List<Writer> getAllProcessed(List<Writer> writers) {
        int size = writers.size();
        if (size==0 || size == 1){
            return writers;
        }
        else {
            int i = 1;
            while (i != size) {
                if (writers.get(i - 1).getId() == writers.get(i).getId()) {
                    writers.get(i - 1).getPosts().addAll(writers.get(i).getPosts());
                    writers.remove(i);
                    size = size - 1;
                } else {
                    i = i + 1;
                }
            }
                JDBCPostRepositoryImpl postRepository = new JDBCPostRepositoryImpl();
                for (Writer writer: writers) {
                    writer.setPosts(postRepository.getAllProcessed(writer.getPosts()));
                }
                return writers;
            }

        }



    public Writer save(Writer writer) {
        String sql = "INSERT WRITERS (FIRST_NAME, LAST_NAME) VALUE (?, ?)";
        try (PreparedStatement pstm = JdbcUtils.getPrStatementBackId(sql)) {
            pstm.setString(1, writer.getFirstName());
            pstm.setString(2, writer.getLastName());
            pstm.execute();
            ResultSet rs = pstm.getGeneratedKeys();
            int id = 0;
            if (rs.next()) {
                id = rs.getInt(1);
                writer.setId(id);
            }
            insertPostsInWriterPosts(id, writer.getPosts());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return writer;
    }

}
