package com.samples.migrate;

import com.samples.migrate.impl.SchemaMigrate;
import com.samples.migrate.impl.TableMigrate;
import com.samples.migrate.model.Configuration;

/**
 * 
 * com.samples.migrate Migrate.java
 * @author yangaimin
 * 2016Äê5ÔÂ13ÈÕ
 */
public class Migrate {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: --type --jsonFile");
            return;
        }
        
        Migrate migrate = new Migrate();
        migrate.run(args[0], args[1]);
        ConnectionFactory.close();
    }
    
    /**
     * 
     * @param type
     * @param jsonFile
     */
    public void run(String type, String jsonFile) {
        IMigrate migrate = null;
        Configuration conf = new Configuration(jsonFile);
        if (type.equals("table")) {
            migrate = new TableMigrate();
        } else if (type.equals("schema")) {
            migrate = new SchemaMigrate();
        }
        migrate.migrate(conf);
    }

}
