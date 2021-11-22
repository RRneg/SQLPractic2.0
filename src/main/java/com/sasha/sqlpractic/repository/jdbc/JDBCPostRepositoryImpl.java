package com.sasha.sqlpractic.repository.jdbc;


import com.sasha.sqlpractic.model.Label;
import com.sasha.sqlpractic.model.PostStatus;
import com.sasha.sqlpractic.model.Post;
import com.sasha.sqlpractic.repository.LabelRepository;
import com.sasha.sqlpractic.repository.PostRepository;
import com.sasha.sqlpractic.utils.JdbcUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JDBCPostRepositoryImpl implements PostRepository {



    private LabelRepository labelRepository = new JDBCLabelRepositoryImpl();

    private List<Label> getLabelsByPostId(Integer id) {
        List<Label> labels = new ArrayList<>();
        String sql = "select ID, NAME from LABELS " +
                "join POST_LABELS on POST_LABELS.POST_ID = ?" +
                " and POST_LABELS.LABELS_ID = LABELS.ID";
        try (PreparedStatement pstm = JdbcUtils.getPrStatement(sql)) {
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                labels.add(new Label(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return labels;
    }


    public Post getById(Integer id) {
       String sql = "SELECT ID, CONTENT, CREATED, UPLOADED, POST_STATUS" +
               " FROM POSTS WHERE ID = ? " +
               "JOIN POST_LABELS ON POST.ID = POST_LABELS.POST_ID " +
               "JOIN LABELS ON POST_LABELS.LABELS.ID = LABELS.ID";
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
                while (rs.next()){
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
        return getAllInternal();
    }

    private List<Post> getAllInternal() {
        Post post = new Post();
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

    public Post save(Post post) {
        String sql = "INSERT POSTS CONTENT=?, POST_STATUS=?";
        String sql1 = "SELECT FROM POSTS WHERE ID = ?";
        String content = post.getContent();
        List<Label> labels = post.getLabels();
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
