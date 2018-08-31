/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ilc.cnr.it.morphoRules.manager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author andrea
 */
public class ResultTable implements Serializable {
    
//    private ArrayList<String> columns;
    private Map<String, String> row;

//    public ArrayList<String> getColumns() {
//        return columns;
//    }
//
//    public void setColumns(ArrayList<String> columns) {
//        this.columns = columns;
//    }

    public Map<String, String> getRow() {
        return row;
    }

    public void setRow(String key, String value) {
        this.row.put(key, value);
    }
    
    public ResultTable() {
//        columns = new ArrayList();
        row = new HashMap();
    }
   
    
}
