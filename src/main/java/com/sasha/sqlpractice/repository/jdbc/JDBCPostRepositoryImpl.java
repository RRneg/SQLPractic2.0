package com.sasha.sqlpractice.repository.jdbc;


import com.sasha.sqlpractice.model.Label;
import com.sasha.sqlpractice.model.PostStatus;
import com.sasha.sqlpractice.model.Post;
import com.sasha.sqlpractice.repository.PostRepository;
import com.sasha.sqlpractice.utils.JdbcUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JDBCPostRepositoryImpl implements PostRepository<Post, Integer> {

    private Post post = new Post();
    private List<Label> labels = null;
    private JDBCLabelRepositoryImpl jdbcLabelRepository = new JDBCLabelRepositoryImpl();

    public List<Label> getLabelsByPostId(Integer id) {
        String sql = "SELECT LABELS_ID FROM POST_LABELS WHERE POST_ID=" + id;
        try (PreparedStatement pstm = JdbcUtils.getPrStatement(sql)) {
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                labels.add(jdbcLabelRepository.getById(rs.getInt(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return labels;
    }


    public Post getById(Integer id) {
        String sql = "SELECT from POSTS WHERE ID =?";
        try (PreparedStatement pstm = JdbcUtils.getPrStatement(sql)) {
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();
            if (rs.getString(6).equals(PostStatus.DELETED)) {
                post = null;
                System.out.println("Запись с ID = " + id + " удалена или не существует...");
            } else {
                post.setId(rs.getInt(1));
                post.setContent(rs.getString(2));
                post.setCreated(rs.getDate(3).toString());
                post.setUpdated(rs.getDate(4).toString());
                post.setLabels(getLabelsByPostId(id));
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
        return getAllInternal();
    }

    private List<Post> getAllInternal() {
        List<Post> posts = null;
        String sql = "SELECT from POSTS WHERE POST_STATUS <> DELETE";
        try (PreparedStatement pstm = JdbcUtils.getPrStatementBackId(sql)) {
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                post.setId(rs.getInt(1));
                post.setContent(rs.getString(2));
                post.setCreated(rs.getDate(3).toString());
                post.setUpdated(rs.getDate(4).toString());
                post.setLabels(getLabelsByPostId(rs.getInt(1)));
                posts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return posts;
    }

    public Post saveNew(String content, List<Label> labels) {
        String sql = "INSERT POSTS CONTENT=?, POST_STATUS=?";
        String sql1 = "SELECT FROM POSTS WHERE ID = ?";
        Post post = new Post();
        post.setContent(content);
        post.setLabels(labels);
        post.setPostStatus(PostStatus.ACTIVE);
        try (PreparedStatement pstm = JdbcUtils.getPrStatementBackId(sql)) {
            pstm.setString(1, content);
            pstm.setString(2, PostStatus.ACTIVE.toString());
            int affectedRows = pstm.executeUpdate();
            insertLabelsInPostLabels(affectedRows, labels);
            post.setId(affectedRows);

            try (PreparedStatement pstm1 = JdbcUtils.getPrStatement(sql1)) {
                pstm1.setInt(1, affectedRows);
                ResultSet rs = pstm1.executeQuery();
                post.setCreated(rs.getDate(3).toString());
            }
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
