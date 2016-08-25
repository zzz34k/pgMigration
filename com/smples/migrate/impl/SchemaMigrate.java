package com.samples.migrate.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.samples.migrate.ConnectionFactory;
import com.samples.migrate.IMigrate;
import com.samples.migrate.model.Configuration;
import com.samples.migrate.model.Server;

public class SchemaMigrate implements IMigrate {

    /**
     * migrate
     */
    public void migrate(Configuration conf) {
        if (conf == null) {
            return;
        }
        
        Server from = conf.getFromServer();
        Connection conn = ConnectionFactory.getConnection(from.getHost(), from.getDb(), from.getPort(), from.getUser(), from.getPassword());
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT table_name from information_schema.tables where table_schema = '");
        sb.append(from.getSchema() + "'");
        String filter = from.getFilter();
        if (!filter.equals("") && filter.length()>0) {
            sb.append(" AND " + filter);
        }
        sb.append(" ORDER BY table_name");
        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sb.toString());
            Configuration iconf = conf.clone();
            IMigrate migrate = new TableMigrate();
            int index = 1;
            while(rs.next()) {
                String tableName = rs.getString("table_name");
                System.out.println((index++) + "\t" + tableName);
                iconf.getFromServer().setTable(tableName);
                iconf.getToServer().setTable(tableName);
                migrate.migrate(iconf);
            }
        }catch(SQLException | CloneNotSupportedException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
}
