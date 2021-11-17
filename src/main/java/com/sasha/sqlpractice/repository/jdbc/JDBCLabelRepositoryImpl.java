package com.sasha.sqlpractice.repository.jdbc;

import com.sasha.sqlpractice.model.Label;
import com.sasha.sqlpractice.repository.LabelRepository;
import com.sasha.sqlpractice.utils.JdbcUtils;
import liquibase.pro.packaged.L;

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


    @Override
    public Label create(Label label) {
        String sql = "INSERT LABELS(NAME) VALUES(?)";
        try (PreparedStatement pstm = JdbcUtils.getPrStatementBackId(sql)) {
            pstm.setString(1, label.getName());
            label.setId(pstm.executeUpdate());
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
        return getAllInternal();
    }

    private List<Label> getAllInternal() {
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
