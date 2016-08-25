package com.samples.migrate.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONObject;

public class Configuration implements Cloneable {
    
    private Server from = null;
    
    private Server to = null;
    
    private String condition = "";
    
    private String columns = "*";
    
    public Server getFromServer() {
        return this.from;
    }
    
    public Server getToServer() {
        return this.to;
    }
    
    public String getCondition() {
        return condition;
    }

    public String getColumns() {
        return columns;
    }

    /**
     * 
     * @param jsonFile
     */
    public Configuration(String jsonFile) {
        int ret = this.loadConfigFromJson(jsonFile);
        if (ret == -1) {
            System.out.println("File not found");
        } else if (ret == -2) {
            System.out.println("Failed to load json file");
        } else {
            System.out.println("Load json file ok");
        }
    }
    
    /**
     * 
     * @param jsonFile
     * @return
     */
    private int loadConfigFromJson(String jsonFile) {
        File file = new File(jsonFile);
        if (!file.exists()) {
            return -1;//file not found
        }
        
        try{
            FileReader fr = new FileReader(file);
            StringBuffer sb = new StringBuffer();
            BufferedReader bf = new BufferedReader(fr);
            String line = null;
            while((line = bf.readLine()) != null) {
                sb.append(line);
            }
            JSONObject obj = new JSONObject(sb.toString());
            if (obj != null) {
                JSONObject from = obj.getJSONObject("from");
                this.from = new Server();
                this.from.setDb(from.getString("db"));
                this.from.setHost(from.getString("host"));
                this.from.setSchema(from.getString("schema"));
                this.from.setTable(from.getString("table"));
                this.from.setUser(from.getString("user"));
                this.from.setPort(from.getString("port"));
                this.from.setPassword(from.getString("password"));
                this.from.setFilter(from.getString("filter"));
                
                JSONObject to = obj.getJSONObject("to");
                this.to = new Server();
                this.to.setDb(to.getString("db"));
                this.to.setHost(to.getString("host"));
                this.to.setSchema(to.getString("schema"));
                this.to.setTable(to.getString("table"));
                this.to.setUser(to.getString("user"));
                this.to.setPort(to.getString("port"));
                this.to.setPassword(to.getString("password"));
                
                this.condition = obj.getString("condition");
                this.columns = obj.getString("columns");
            }
        } catch(IOException e) {
            e.printStackTrace();
            return -2;
        }
        return 1;
    }
    
    /**
     * @throws CloneNotSupportedException 
     * 
     */
    public Configuration clone() throws CloneNotSupportedException {
        Configuration conf = (Configuration) super.clone();
        return conf;
    }
}
