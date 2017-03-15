package hops;

import org.apache.jackrabbit.commons.JcrUtils;

import javax.jcr.*;
import java.io.FileInputStream;

public class ThirdHop {

    public static void main(String[] args) throws Exception {
        Repository repository = JcrUtils.getRepository();
        Session session = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));

        FileInputStream xml = new FileInputStream("src/main/resources/test.xml");
        try {
            Node root = session.getRootNode();

            if (!root.hasNode("importxml")) {
                System.out.print("Importing xml... ");

                Node node = root.addNode("importxml", "nt:unstructured");

                session.importXML(node.getPath(), xml, ImportUUIDBehavior.IMPORT_UUID_CREATE_NEW);

                session.save();
                System.out.println("done.");
            }

            dump(root);
        } finally {
            session.logout();
        }
    }

    private static void dump(Node node) throws RepositoryException {
        System.out.println(node.getPath());

        if (node.getName().equals("jcr:system")) {
            return;
        }

        PropertyIterator properties = node.getProperties();
        while (properties.hasNext()) {
            Property property = properties.nextProperty();
            if (property.getDefinition().isMultiple()) {
                Value[] values = property.getValues();
                for (Value value : values) {
                    System.out.println(property.getPath() + " = " + value.getString());
                }
            } else {
                System.out.println(property.getPath() + " = " + property.getString());
            }
        }

        NodeIterator nodes = node.getNodes();
        while (nodes.hasNext()) {
            dump(nodes.nextNode());
        }
    }

}
