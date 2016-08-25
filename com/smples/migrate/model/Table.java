package com.samples.migrate.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class Table {

    private String tableName = "";

    private String schema = "public";

    private Connection conn = null;

    /**
     * 
     * @param tableName
     * @param conn
     */
    public Table(String tableName, Connection conn) {
        this.tableName = tableName;
        this.conn = conn;

    }

    /**
     * 
     * @param tableName
     * @param schema
     * @param conn
     */
    public Table(String tableName, String schema, Connection conn) {
        this.tableName = tableName;
        this.conn = conn;
        this.schema = schema;
    }

    /**
     * 
     * @param columns
     * @return
     */
    public Map<String, String> getFields(String columns) {
        if (columns.equals("*")) {
            return this.getAllFields();
        } else {
            String[] _fields = columns.split(",");
            StringBuilder columnNames = new StringBuilder();
            for (int i=0; i<_fields.length; i++) {
                String field = _fields[i];
                String[]  tokens = field.trim().split(" ");
                if (columnNames.length() >0) {
                    columnNames.append(",");
                }
                columnNames.append("'" + tokens[0] + "'");
            }
            StringBuilder sql = new StringBuilder("select column_name,data_type,character_maximum_length from information_schema.columns where table_schema='");
            sql.append(this.schema);
            sql.append("' AND table_name='");
            sql.append(this.tableName);
            sql.append("' AND column_name IN (");
            sql.append(columnNames.toString());
            sql.append(")");
            return this.getFieldsBySQL(sql.toString());
        }
    }
    
    /**
     * 
     * @return
     */
    public Map<String, String> getAllFields() {
        StringBuilder sql = new StringBuilder("select column_name,data_type,character_maximum_length from information_schema.columns where table_schema='");
        sql.append(this.schema);
        sql.append("' AND table_name='");
        sql.append(this.tableName);
        sql.append("'");
        
        return this.getFieldsBySQL(sql.toString());
    }
    
    /**
     * 
     * @param sql
     * @return
     */
    private Map<String,String> getFieldsBySQL(String sql) {
        Map<String, String> fields = new HashMap<String, String>();
        try {
            Statement stmt = this.conn.createStatement();
            //System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                fields.put(rs.getString("column_name"),rs.getString("data_type") + (rs.getString("character_maximum_length") == null ? "" : "(" + rs.getString("character_maximum_length") + ")"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fields;
    }

}
