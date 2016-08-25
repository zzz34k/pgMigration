package com.samples.migrate.model;
/**
 * database server object
 * com.samples.migrate.model Server.java
 * @author yangaimin
 * 2016Äê5ÔÂ13ÈÕ
 */
public class Server implements Cloneable{

    private String host = "";
    private String port = "";
    private String db = "";
    private String schema = "";
    private String user = "";
    private String password = "";
    private String table = "";
    private String filter = "";
    
    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getTable() {
        return table;
    }
    
    public void setTable(String table) {
        this.table = table;
    }
    
    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public String getDb() {
        return db;
    }
    public void setDb(String db) {
        this.db = db;
    }
    public String getSchema() {
        return schema;
    }
    public void setSchema(String schema) {
        this.schema = schema;
    }
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
