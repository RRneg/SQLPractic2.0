package com.sasha.sqlpractic.repository.jdbc;


import com.sasha.sqlpractic.model.Label;
import com.sasha.sqlpractic.model.PostStatus;
import com.sasha.sqlpractic.model.Post;
import com.sasha.sqlpractic.repository.PostRepository;
import com.sasha.sqlpractic.utils.JdbcUtils;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class JDBCPostRepositoryImpl implements PostRepository {

    public Post getById(Integer id) {
        String sql = "SELECT * from POSTS JOIN POST_LABELS ON POST_LABELS.POST_ID = POSTS.ID JOIN LABELS ON LABELS.ID = POST_LABELS.LABELS_ID where POSTS.ID = ? and POSTS.POST_STATUS not like \"DELETE\"";
        Post post = new Post();
        try (PreparedStatement pstm = JdbcUtils.getPrStatement(sql)) {
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();
            List<Label> labels = new ArrayList<>();

            if (rs.next()) {
                post.setId(rs.getInt(1));
                post.setContent(rs.getString(2));
                post.setCreated(rs.getDate(3).toString());
                post.setUpdated(rs.getDate(4).toString());
                post.setPostStatus(PostStatus.valueOf(rs.getString(5)));
                labels.add(new Label(rs.getInt(8), rs.getString(9)));

                while (rs.next()) {
                    labels.add(new Label(rs.getInt(8), rs.getString(9)));
                }
                post.setLabels(labels);
            }
            }
         catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }

    public Post update(Post post) {
        String sql = "UPDATE POSTS SET CONTENT=? WHERE ID = ?";
        String sql2 = String.format("DELETE FROM POST_LABELS WHERE POST_ID = %d", post.getId());
        try (PreparedStatement pstm = JdbcUtils.getPrStatement(sql)) {
            pstm.setString(1, post.getContent());
            pstm.setInt(2, post.getId());
            pstm.executeUpdate();
            pstm.execute(sql2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        insertLabelsInPostLabels(post.getId(), post.getLabels());
        return post;
    }

    public void deleteById(Integer id) {
        String sql = "UPDATE POSTS SET POST_STATUS = 'DELETE' WHERE ID =?";
        try (PreparedStatement pstm = JdbcUtils.getPrStatement(sql)) {
            pstm.setInt(1, id);
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public List<Post> getAll() {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT * FROM writers JOIN writer_posts ON writers.ID = writer_posts.WRITER_ID JOIN posts ON writer_posts.POST_ID=posts.ID JOIN post_labels ON posts.ID = post_labels.POST_ID JOIN labels ON post_labels.LABELS_ID = labels.ID WHERE posts.POST_STATUS not like \"DELETE\" order by writers.ID";
        try (PreparedStatement pstm = JdbcUtils.getPrStatement(sql)) {
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                List<Label> labels = new ArrayList<>();
                labels.add(new Label(rs.getInt(8), rs.getString(9)));
                posts.add(new Post(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getDate(3).toString(),
                        rs.getDate(4).toString(),
                        labels,
                        PostStatus.valueOf(rs.getString(5))));
                while (rs.next()) {
                    List<Label> labels1 = new ArrayList<>();
                    labels1.add(new Label(rs.getInt(8), rs.getString(9)));
                    posts.add(new Post(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getDate(3).toString(),
                            rs.getDate(4).toString(),
                            labels1,
                            PostStatus.valueOf(rs.getString(5))));
                    }
                }
             else {
                System.out.println("The database is empty");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return getAllProcessed(posts);
    }




    public Post save(Post post) {

        String sql = "INSERT POSTS (CONTENT, POST_STATUS) VALUE(?,?)";
        String content = post.getContent();
        List<Label> labels = post.getLabels();
        post.setPostStatus(PostStatus.ACTIVE);
        try (PreparedStatement pstm = JdbcUtils.getPrStatementBackId(sql)) {
            pstm.setString(1, content);
            pstm.setString(2, PostStatus.ACTIVE.toString());
            pstm.execute();
            ResultSet rs = pstm.getGeneratedKeys();
            int id = 0;
            if(rs.next()) {
                id = rs.getInt(1);
                post.setId(id);
            }
            insertLabelsInPostLabels(id, labels);
            String sql2 = String.format("SELECT CREATED FROM POSTS WHERE ID = %d", id);
            PreparedStatement pstm1 = JdbcUtils.getPrStatement(sql2);
            ResultSet rs1 = pstm1.executeQuery();
            if (rs1.next()) {
                post.setCreated(rs1.getDate(1).toString());// необходимо исправить
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }

    private void insertLabelsInPostLabels(Integer postId, List<Label> labels) {
        for (Label label : labels) {
            String sql = "INSERT POST_LABELS (POST_ID, LABELS_ID) " +
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

    private List<Post> getAllProcessed(List<Post> posts) {
        int size = posts.size();
        if(size == 0 || size == 1){
            return posts;
        }
        else {
            int k =1;
            while (k!=size){
                if (posts.get(k-1).getId() == posts.get(k).getId()){
                    posts.get(k-1).getLabels().addAll(posts.get(k).getLabels());
                    posts.remove(k);
                    size = size-1;
                }
                else {k = k+1;}
            }

            return posts;
        }
        }




}
