package com.sasha.sqlpractic.repository.jdbc;

import com.sasha.sqlpractic.model.Label;
import com.sasha.sqlpractic.repository.LabelRepository;
import com.sasha.sqlpractic.utils.JdbcUtils;


import java.sql.*;
import java.util.List;

public class JDBCLabelRepositoryImpl implements LabelRepository {

    private final static String GET_BY_ID_QUERY = "SELECT from LABELS where ID = %d";


    public Label update(Label label) {
        String sql = "INSERT LABELS(NAME) VALUE(?) WHERE ID= ?";
        try (PreparedStatement pstm = JdbcUtils.getPrStatement(sql)) {
            pstm.setString(1, label.getName());
            pstm.setInt(2, label.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return label;
    }



    public Label save(Label label){
        String sql = "INSERT LABELS(NAME) VALUES(?)";
        try (PreparedStatement pstm = JdbcUtils.getPrStatementBackId(sql)) {
            pstm.setString(1, label.getName());
            pstm.execute();
            ResultSet rs = pstm.getGeneratedKeys();
            int id = 0;
            if(rs.next()){
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
        String sql = String.format(GET_BY_ID_QUERY, id);
        try (PreparedStatement pstm = JdbcUtils.getPrStatement(sql)) {
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();
            label.setId(rs.getInt(1));
            label.setName(rs.getString(2));
            return label;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
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
        List<Label> labels = null;
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
