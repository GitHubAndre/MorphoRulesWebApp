/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ilc.cnr.it.morphoRules.manager;

/**
 *
 * @author andrea
 */
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class TraitComparator implements Comparator<Form> {
    
    private static final Map<Integer, String> myMap;
    
    static {
        Map<Integer, String> aMap = new HashMap<Integer, String>();
        aMap.put(1, "hasF");
        aMap.put(2, "hasS1IP");
        aMap.put(3, "hasS2IP");
        aMap.put(4, "hasS3IP");
        aMap.put(5, "hasP1IP");
        aMap.put(6, "hasP2IP");
        aMap.put(7, "hasP3IP");
        aMap.put(8, "hasS1II");
        aMap.put(9, "hasS2II");
        aMap.put(10, "hasS3II");
        aMap.put(11, "hasP1II");
        aMap.put(12, "hasP2II");
        aMap.put(13, "hasP3II");
        aMap.put(14, "hasS1IR");
        aMap.put(15, "hasS2IR");
        aMap.put(16, "hasS3IR");
        aMap.put(17, "hasP1IR");
        aMap.put(18, "hasP2IR");
        aMap.put(19, "hasP3IR");
        aMap.put(20, "hasS1IF");
        aMap.put(21, "hasS2IF");
        aMap.put(22, "hasS3IF");
        aMap.put(23, "hasP1IF");
        aMap.put(24, "hasP2IF");
        aMap.put(25, "hasP3IF");
        aMap.put(26, "hasS1CP");
        aMap.put(27, "hasS2CP");
        aMap.put(28, "hasS3CP");
        aMap.put(29, "hasP1CP");
        aMap.put(30, "hasP2CP");
        aMap.put(31, "hasP3CP");
        aMap.put(32, "hasS1CI");
        aMap.put(33, "hasS2CI");
        aMap.put(34, "hasS3CI");
        aMap.put(35, "hasP1CI");
        aMap.put(36, "hasP2CI");
        aMap.put(37, "hasP3CI");
        aMap.put(38, "hasS1DP");
        aMap.put(39, "hasS2DP");
        aMap.put(40, "hasS3DP");
        aMap.put(41, "hasP1DP");
        aMap.put(42, "hasP2DP");
        aMap.put(43, "hasP3DP");
        aMap.put(44, "hasS2MP");
        aMap.put(45, "hasP2MP");
        aMap.put(46, "hasMS_PP");
        aMap.put(47, "hasMP_PP ");
        aMap.put(48, "hasFS_PP ");
        aMap.put(49, "hasFP_PP");
        aMap.put(50, "hasMS_PR");
        aMap.put(51, "hasMP_PR");
        aMap.put(52, "hasFS_PR");
        aMap.put(53, "hasFP_PR");
        aMap.put(54, "hasG");
        myMap = Collections.unmodifiableMap(aMap);
    }
    
    public TraitComparator() {
    }
    
    @Override
//    public int compare(Map<String, String> o1, Map<String, String> o2) {
//        return o1.get(orderingField).compareToIgnoreCase(o2.get(orderingField));
//    }

    public int compare(Form o1, Form o2) {
        String s1 = o1.getMorphoTag();
        String s2 = o2.getMorphoTag();
        return Integer.compare(getKey(s2), getKey(s1));
        
    }
    
    private int getKey(String s) {
        for (Map.Entry<Integer, String> e : myMap.entrySet()) {
            if (e.getValue().equals(s)) {
                return e.getKey();
            }
        }
        return 0;
    }
    
}
