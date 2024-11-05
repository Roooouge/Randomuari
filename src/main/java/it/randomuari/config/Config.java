package it.randomuari.config;

import java.util.List;
import lombok.experimental.UtilityClass;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

@UtilityClass
public class Config {

    private final Element ROOT;

    static {
        try {
            Document document = new SAXReader().read(Config.class.getClassLoader().getResourceAsStream("config.xml"));
            ROOT = document.getRootElement();
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    public Node getConfig(String path) {
        return ROOT.selectSingleNode(path);
    }

    public List<Node> getConfigs(String path) {
        return ROOT.selectNodes(path);
    }
}
