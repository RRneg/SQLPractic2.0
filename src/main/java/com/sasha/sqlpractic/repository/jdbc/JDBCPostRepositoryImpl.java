package com.sasha.sqlpractic.repository.jdbc;


import com.sasha.sqlpractic.model.Label;
import com.sasha.sqlpractic.model.PostStatus;
import com.sasha.sqlpractic.model.Post;
import com.sasha.sqlpractic.repository.PostRepository;
import com.sasha.sqlpractic.utils.JdbcUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JDBCPostRepositoryImpl implements PostRepository {

    public Post getById(Integer id) {
        String sql = "SELECT ID, CONTENT, CREATED, UPLOADED, POST_STATUS" +
                " FROM POSTS WHERE ID = ? " +
                "JOIN POST_LABELS ON POST.ID = POST_LABELS.POST_ID " +
                "JOIN LABELS ON POST_LABELS.LABELS_ID = LABELS.ID";
        Post post = new Post();
        try (PreparedStatement pstm = JdbcUtils.getPrStatement(sql)) {
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();
            if (rs.getString(6).equals(PostStatus.DELETED)) {
                post = null;
                System.out.println("Запись с ID = " + id + " удалена или не существует...");
            } else {
                List<Label> labels = new ArrayList<>();
                post.setId(rs.getInt(1));
                post.setContent(rs.getString(2));
                post.setCreated(rs.getDate(3).toString());
                post.setUpdated(rs.getDate(4).toString());
                while (rs.next()) {
                    labels.add(new Label(rs.getInt(6), rs.getString(7)));
                }
                post.setLabels(labels);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }

    public Post update(Post post) {
        String sql = "UPDATE POSTS CONTENT=? WHERE ID = ?";
        try (PreparedStatement pstm = JdbcUtils.getPrStatement(sql)) {
            pstm.setString(1, post.getContent());
            pstm.setInt(2, post.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        insertLabelsInPostLabels(post.getId(), post.getLabels());
        return post;
    }

    public void deleteById(Integer id) {
        String sql = "UPDATE POSTS(POST_STATUS) VALUE(DELETE) WHERE ID =?";
        try (PreparedStatement pstm = JdbcUtils.getPrStatement(sql)) {
            pstm.setInt(1, id);
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public List<Post> getAll() {
        List<Post> posts = new ArrayList<>();
        Post post = new Post();
        Label label = new Label();
        List<Label> labels = new ArrayList<>();
        String sql = "SELECT from POSTS WHERE POST_STATUS <> DELETE" +
                "JOIN POST_LABELS ON POST_LABELS.POST_ID = POSTS.ID " +
                "JOIN LABELS ON LABELS.ID = POST_LABEL.LABELS_ID";
        try (PreparedStatement pstm = JdbcUtils.getPrStatement(sql)) {
            ResultSet rs = pstm.executeQuery();
            int size = getResultSetRowCount(rs);
            while (rs.next()) {

                int postId = rs.getInt(1);
                if (!rs.wasNull()) {
                    labels.clear();
                    if (rs.getRow() != 1) {
                        posts.add(post);
                    }
                    post.setId(postId);
                    post.setContent(rs.getString(2));
                    post.setCreated(rs.getDate(3).toString());
                    post.setUpdated(rs.getDate(4).toString());
                    post.setPostStatus(PostStatus.valueOf(rs.getString(5)));
                }
                int labelId = rs.getInt(6);
                if (!rs.wasNull()) {
                    label.setId(labelId);
                    label.setName(rs.getString(7));
                    labels.add(label);
                }
                post.setLabels(labels);
                if (rs.getRow() == size) {
                    posts.add(post);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return posts;
    }


    private int getResultSetRowCount(ResultSet rs) {
        int size = 0;
        try {
            rs.last();
            size = rs.getRow();
            rs.beforeFirst();
        } catch (SQLException ex) {
            return 0;
        }
        return size;
    }


    public Post save(Post post) {
        String sql = "INSERT POSTS CONTENT=?, POST_STATUS=?";
        String sql1 = "SELECT FROM POSTS WHERE ID = ?";
        String content = post.getContent();
        List<Label> labels = post.getLabels();
        post.setPostStatus(PostStatus.ACTIVE);
        try (PreparedStatement pstm = JdbcUtils.getPrStatmentBackIdCreated(sql)) {
            pstm.setString(1, content);
            pstm.setString(2, PostStatus.ACTIVE.toString());
            pstm.execute();
            ResultSet rs = pstm.getGeneratedKeys();
            int id = 0;
            String created = null;
            if (rs.next()) {
                id = rs.getInt(1);
                created = rs.getDate(2).toString();
            }
            insertLabelsInPostLabels(id, labels);
            post.setId(id);
            post.setCreated(created);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }

    private void insertLabelsInPostLabels(Integer postId, List<Label> labels) {
        for (Label label : labels) {
            String sql = "INSERT POST_LABELS (LABEL_ID, POSTS_ID) " +
                    "VALUE (?, ?)";
            try (PreparedStatement pstm = JdbcUtils.getPrStatement(sql)) {
                pstm.setInt(1, postId);
                pstm.setInt(2, label.getId());
                pstm.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
