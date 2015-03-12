/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2015 ICM-UW
 * 
 * CoAnSys is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * CoAnSys is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
 */
package pl.edu.icm.coansys.commons.reparser;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Lukasz Bolikowski (bolo@icm.edu.pl)
 *
 */
public class RegexpParser {

    private static final Logger log = LoggerFactory.getLogger(RegexpParser.class);
    private NodeCategory mainNodeCategory;
    private static final String NAME = "name";
    private static final String NODE = "node";
    private static final String REGEXP = "regexp";
    private static final String TEMPLATE = "template";

    @SuppressWarnings("unchecked")
    private void loadConfiguration(String resource, String mainNode) {
        Iterator<String> iter = null;
        URL url = this.getClass().getClassLoader().getResource(resource);
        Configuration cfg;
        try {
            cfg = new PropertiesConfiguration(url);
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }

        /* Collect all nodes */
        Map<String, NodeCategory> nodes = new HashMap<String, NodeCategory>();
        iter = (Iterator<String>) cfg.getKeys(NODE);
        while (iter.hasNext()) {
            String nodeId = iter.next();
            nodeId = nodeId.replaceFirst("^" + NODE + "\\.", "").replaceFirst("\\..*$", "");
            if (!nodes.containsKey(nodeId)) {
                nodes.put(nodeId, new NodeCategory());
            }
        }
        log.debug("Found nodes: " + nodes.keySet());
        if (!nodes.containsKey(mainNode)) {
            throw new IllegalArgumentException("Main node not found in the configuration file.  The required node is: " + mainNode);
        }

        for (Map.Entry<String, NodeCategory> entry : nodes.entrySet()) {
            NodeCategory node = entry.getValue();
            String id = entry.getKey();
            node.setId(id);

            String regexp = cfg.getString(NODE + "." + id + "." + REGEXP);
            if (regexp != null) {
                node.setRegexp(regexp);
            }

            String name = cfg.getString(NODE + "." + id + "." + NAME);
            if (name != null) {
                node.setName(name);
            }

            iter = (Iterator<String>) cfg.getKeys(NODE + "." + id + "." + TEMPLATE);
            while (iter.hasNext()) {
                String type = iter.next();
                type = type.replaceFirst("^" + NODE + "\\." + id + "\\." + TEMPLATE + "\\.?+", "");
                List<String> templates = (List<String>) cfg.getList(NODE + "." + id + "." + TEMPLATE + "." + type);

                for (String template : templates) {
                    Template t = new Template();
                    t.setType(type);

                    String[] fields = template.split("\\s+");
                    for (String field : fields) {
                        NodeCategory n = nodes.get(field);
                        if (n == null) {
                            log.warn("Node '" + field + "' does not exist.  Referenced in template: " + template);
                        }
                        t.addField(n);
                    }
                    node.addTemplate(t);
                }
            }
        }
        mainNodeCategory = nodes.get(mainNode);
    }

    public RegexpParser(String resource, String mainNode) {
        loadConfiguration(resource, mainNode);
    }

    public Node parse(String text) {
        return mainNodeCategory.match(text);
    }
}
