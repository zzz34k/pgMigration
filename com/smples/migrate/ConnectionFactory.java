package com.baidu.bee;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ConnectionFactory {

    /**
     * connection cache
     */
    private static Map<String,Connection> cache = new HashMap<String,Connection>();
    
    /**
     * is driver loaded
     */
    public static boolean driverLoaded = false;
    
    /**
     * 
     * @param host
     * @param dbname
     * @param port
     * @param user
     * @param password
     * @return
     */
    public static Connection getConnection(String host, String dbname, String port, String user, String password) {
        String key = host + dbname + port;
        //System.out.println(key);
        //System.out.println(cache.toString());
        if (cache.containsKey(key)) {
            return cache.get(key);
        }
        
        if (!ConnectionFactory.driverLoaded) {
            try {
                // 加载MySql的驱动类
                Class.forName("org.postgresql.Driver").newInstance();
                ConnectionFactory.driverLoaded = true;
            } catch (Exception e) {
                System.out.println("找不到驱动程序类 ，加载驱动失败！");
                e.printStackTrace();
            }
        }
        
        Connection conn = null;
        try{
            conn = DriverManager.getConnection("jdbc:postgresql://" + host + ":" + port + "/" + dbname, user, password);
            cache.put(key, conn);
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return conn;
    }
    
    public static void close() {
        Iterator<String> it = cache.keySet().iterator();
        while(it.hasNext()) {
            String key = it.next();
            Connection conn = cache.get(key);
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
