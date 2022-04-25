package Controller;

import Entity.Template;
import UseCase.TemplateManager;
import UseCase.TemplateNotFoundException;

import java.util.HashMap;
import java.util.Map;

/**
 * A class called TemplateSystem which is a controller of TemplateManager.
 * It has one attribute called tm, which is a TemplateManager.
 *
 * TemplateSystem can be created by the constructor immediately with the given TemplateManager tm.
 *
 * tm: a TemplateManager
 *
 * @author Christine
 * @author Chuanrun Zhang
 * @author Siqing Xu
 */
public class TemplateSystem {
    private TemplateManager tm;

    /**
     * A constructor that create a TemplateSystem with the parameter tm.
     *
     * @param tm a TemplateManager
     */
    public TemplateSystem(TemplateManager tm) {
        this.tm = tm;
    }

    /**
     * Creates a Template with the given TemplateType by tm.
     *
     * @param TemplateType a string which is the type of the template that a user wants to create.
     */
    public void create(String TemplateType) {
        tm.createTemplate(TemplateType);
    }

    /**
     * Get all templates with their information including their attributes stored in tm and
     * return a HashMap with key is the index in front of each element of IdToTemplate, value is the corresponding
     * template id.
     *
     */
    public HashMap<Integer, String> GetTemplateRecords() {
        HashMap<Integer, String> indexToId = new HashMap<>();
        int i = 1;
        Map<String, Template> map = tm.getIdToTemplate();
        for (Map.Entry<String, Template> entry : map.entrySet()) {
            System.out.println(i + ") The template ID is: "  + entry.getKey() + ", and the type is: "
                    + entry.getValue().getTemplateType() + ", Max time of event: "+ entry.getValue().getMaxTimeOfEvent() +
                    ", Min time of event: " + entry.getValue().getMinTimeOfEvent() + ", Min time between event:" +
                    entry.getValue().getMinTimeBtwEvents() + ".");
            indexToId.put(i, entry.getKey());
            i++;
        }
        return indexToId;
    }

    /**
     * Delete a template created by the template id.
     * Return true if the template id deleted successfully. Return false otherwise.
     *
     * @param templateId the id of the template
     */
    public void delete(String templateId) throws TemplateNotFoundException {
        tm.removeTemplate(templateId);
    }
}

