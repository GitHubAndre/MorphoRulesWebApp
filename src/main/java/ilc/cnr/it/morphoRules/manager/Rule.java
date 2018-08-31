/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ilc.cnr.it.morphoRules.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author andrea
 */
public class Rule implements Serializable {

    private static ArrayList<MorphoRule> rulesMap = new ArrayList();

    static {
        loadRules("noun", "N");
        loadRules("verb", "V");
        loadRules("adjective", "A");
    }

    public static ArrayList<Form> getFlexedForms(String lemma, String pos, String morphoClass) {
        ArrayList<Form> flexedForms = new ArrayList<Form>();
        for (MorphoRule mr : rulesMap) {
            if (mr.morphoClass.equals(morphoClass)) {
                flexedForms.add(getFlexedForm(lemma, mr));
            }
        }
        return flexedForms;
    }

    private static Form getFlexedForm(String lemma, MorphoRule mr) {
        Form f = new Form();
        f.setMorphoTag(mr.trait);
        if (mr.remove == 0) {
            f.setStem(lemma.toLowerCase());
            f.setFlexion("");
        } else {
            f.setStem(lemma.substring(0, (lemma.length() - mr.remove)).toLowerCase());
            f.setFlexion(accentRecover(mr.add).toLowerCase());
        }
        return f;
    }

    private static String accentRecover(String s) {
        if (s.endsWith("A'")) {
            return s.replace("A'", "À");
        } else {
            if (s.endsWith("E'")) {
                return s.replace("E'", "È");
            } else {
                if (s.endsWith("I'")) {
                    return s.replace("I'", "Ì");
                } else {
                    if (s.endsWith("O'")) {
                        return s.replace("O'", "Ò");
                    } else {
                        if (s.endsWith("U'")) {
                            return s.replace("U'", "Ù");
                        }
                    }
                }
            }
        }
        return s;
    }

    private static void loadRules(String pos, String feat) {
        try (InputStream input = Rule.class.getResourceAsStream("/" + pos + "Rules.txt")) {
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            String line = null;
            while ((line = br.readLine()) != null) {
                String l[] = line.split(" ");
                // ORIGINAL
//                MorphoRule mr = new MorphoRule(feat + l[4], l[5], l[1], Integer.parseInt(l[3]));
                MorphoRule mr = new MorphoRule(l[4], l[5], l[1], Integer.parseInt(l[3]));
                rulesMap.add(mr);
            }
            br.close();

        } catch (IOException ex) {
            Logger.getLogger(Rule.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static class MorphoRule {

        private String trait;
        private String morphoClass;
        private String add;
        private int remove;

        public MorphoRule(String morphoClass, String trait, String add, int remove) {
            this.morphoClass = morphoClass;
            this.trait = trait;
            this.add = add;
            this.remove = remove;
        }

    }

}
