package com.samples.migrate.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;

import com.samples.migrate.ConnectionFactory;
import com.samples.migrate.IMigrate;
import com.samples.migrate.model.Configuration;
import com.samples.migrate.model.Server;
import com.samples.migrate.model.Table;

/**
 * 
 * com.samples.migrate.impl TableMigrate.java
 * @author yangaimin
 * 2016Äê5ÔÂ13ÈÕ
 */
public class TableMigrate implements IMigrate {

    /**
     * migrate table
     * @param Configuration conf
     */
    public void migrate(Configuration conf) {
        if (conf == null) {
            return;
        }
        
        Server from = conf.getFromServer();
        Server to = conf.getToServer();
        StringBuffer sql = new StringBuffer();
        sql.append("create table ");
        sql.append(to.getTable());
        sql.append(" as select * from dblink('host=");
        sql.append(from.getHost());
        sql.append(" dbname=");
        sql.append(from.getDb());
        sql.append(" port=");
        sql.append(from.getPort());
        sql.append(" user=");
        sql.append(from.getUser());
        sql.append(" password=");
        sql.append(from.getPassword());
        sql.append("',");
        sql.append("'select ");
        sql.append(this.getColumnList(conf));
        sql.append(" from ");
        sql.append(from.getTable());
        sql.append("') as t(" + this.getColumnDefinition(conf) + ");");
        
        Connection conn = ConnectionFactory.getConnection(to.getHost(), to.getDb(), to.getPort(), to.getUser(), to.getPassword());
        try{
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE EXTENSION IF NOT EXISTS dblink");
            //System.out.println(sql.toString());
            stmt.execute(sql.toString());
            stmt.close();
            //System.out.println("migrate done");
        } catch(SQLException e) {
            //e.printStackTrace();
            System.out.println(e.getMessage());
        } /*finally {
            ConnectionFactory.close();
        }*/
    }
    
    private String getColumnList(Configuration conf) {
        Server from = conf.getFromServer();
        StringBuffer columns = new StringBuffer();
        Connection conn = ConnectionFactory.getConnection(from.getHost(), from.getDb(), from.getPort(), from.getUser(), from.getPassword());
        Table table = new Table(from.getTable(), from.getSchema(), conn);
        Map<String,String> fields = table.getFields(conf.getColumns());
        Iterator<String> it = fields.keySet().iterator();
        while(it.hasNext()) {
            String key = it.next();
            if (columns.length()>0) {
                columns.append(",");
            }
            columns.append(key);
        }
        return columns.toString();
    }
    
    /**
     * 
     * @param conf
     * @return
     */
    private String getColumnDefinition(Configuration conf) {
        Server from = conf.getFromServer();
        StringBuffer columns = new StringBuffer();
        Connection conn = ConnectionFactory.getConnection(from.getHost(), from.getDb(), from.getPort(), from.getUser(), from.getPassword());
        Table table = new Table(from.getTable(), from.getSchema(), conn);
        Map<String,String> fields = table.getFields(conf.getColumns());
        Iterator<String> it = fields.keySet().iterator();
        while(it.hasNext()) {
            String key = it.next();
            String val = fields.get(key);
            if (columns.length()>0) {
                columns.append(",");
            }
            if (val.equals("USER-DEFINED")) {
                val = "geometry(Geometry,4326)";
            }
            columns.append(key + " " + val);
        }
        return columns.toString();
    }
    
}
