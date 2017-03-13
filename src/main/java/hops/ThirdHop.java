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

            // Import the XML file unless already imported
            if (!root.hasNode("importxml")) {
                System.out.print("Importing xml... ");
                // Create an unstructured node under which to import the XML
                Node node = root.addNode("importxml", "nt:unstructured");
                // Import the file "test.xml" under the created node
                session.importXML(node.getPath(), xml, ImportUUIDBehavior.IMPORT_UUID_CREATE_NEW);

                session.save();
                System.out.println("done.");
            }

            dump(root);
        } finally {
            session.logout();
        }
    }

    /**
     * Recursively outputs the contents of the given node.
     */
    private static void dump(Node node) throws RepositoryException {
        // First output the node path
        System.out.println(node.getPath());
        // Skip the virtual (and large!) jcr:system subtree
        if (node.getName().equals("jcr:system")) {
            return;
        }

        // Then output the properties
        PropertyIterator properties = node.getProperties();
        while (properties.hasNext()) {
            Property property = properties.nextProperty();
            if (property.getDefinition().isMultiple()) {
                // A multi-valued property, print all values
                Value[] values = property.getValues();
                for (int i = 0; i < values.length; i++) {
                    System.out.println(property.getPath() + " = " + values[i].getString());
                }
            } else {
                // A single-valued property
                System.out.println(property.getPath() + " = " + property.getString());
            }
        }

        // Finally output all the child nodes recursively
        NodeIterator nodes = node.getNodes();
        while (nodes.hasNext()) {
            dump(nodes.nextNode());
        }
    }

}
