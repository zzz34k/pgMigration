package com.baidu.bee;

import com.baidu.bee.model.Configuration;

public interface IMigrate {
    
    /**
     * perform transfer
     * @param conf
     */
    public void migrate(Configuration conf);
    
}
