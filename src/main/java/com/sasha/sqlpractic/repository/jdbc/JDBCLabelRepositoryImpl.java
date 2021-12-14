package com.sasha.sqlpractic.repository.jdbc;

import com.sasha.sqlpractic.model.Label;
import com.sasha.sqlpractic.repository.LabelRepository;
import com.sasha.sqlpractic.utils.JdbcUtils;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCLabelRepositoryImpl implements LabelRepository {

    public Label update(Label label) {
        String sql = "UPDATE LABELS SET NAME = ? WHERE ID= ?";
        try (PreparedStatement pstm = JdbcUtils.getPrStatement(sql)) {
            pstm.setString(1, label.getName());
            pstm.setInt(2, label.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return label;
    }


    public Label save(Label label) {
        String sql = "INSERT LABELS(NAME) VALUES(?)";
        try (PreparedStatement pstm = JdbcUtils.getPrStatementBackId(sql)) {
            pstm.setString(1, label.getName());
            pstm.execute();
            ResultSet rs = pstm.getGeneratedKeys();
            int id = 0;
            if (rs.next()) {
                id = rs.getInt(1);
                label.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return label;
    }

    public Label getById(Integer id) {
        Label label = new Label();
        String sql = "SELECT * from LABELS where ID = ?";
        try (PreparedStatement pstm = JdbcUtils.getPrStatement(sql)) {
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();
            rs.next();
            label.setId(rs.getInt(1));
            label.setName(rs.getString(2));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return label;
    }

    public void deleteById(Integer id) {
        String sql = "DELETE from LABELS where ID = ?";
        try (PreparedStatement pstm = JdbcUtils.getPrStatement(sql)) {
            pstm.setInt(1, id);
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Label> getAll() {
        List<Label> labels = new ArrayList<>();
        String sql = "SELECT * from LABELS";

        try (PreparedStatement pstm = JdbcUtils.getPrStatement(sql)) {
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Label label = new Label();
                label.setId(rs.getInt(1));
                label.setName(rs.getString(2));
                labels.add(label);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return labels;

    }
}
