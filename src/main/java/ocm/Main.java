package ocm;

import ocm.model.PressRelease;
import ocm.util.RepositoryUtil;
import org.apache.jackrabbit.JcrConstants;
import org.apache.jackrabbit.ocm.exception.RepositoryException;
import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.apache.jackrabbit.ocm.manager.impl.ObjectContentManagerImpl;
import org.apache.jackrabbit.ocm.mapper.Mapper;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.AnnotationMapperImpl;
import org.apache.jackrabbit.ocm.version.Version;
import org.apache.jackrabbit.ocm.version.VersionIterator;

import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.version.VersionException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Main main = new Main();
        main.startTutorial();
    }

    private void startTutorial() {
        try {
            System.out.println("Start the tutorial ...");
            ObjectContentManager ocm = this.getOCM();

            getAllVersions(ocm);
            showAllVersions(ocm);

            System.out.println("Insert a press release in the repository");
            PressRelease pressRelease = new PressRelease();
            pressRelease.setPath("/newtutorial");
            pressRelease.setTitle("This is the first tutorial on OCM");
            pressRelease.setPubDate(new Date());
            pressRelease.setContent("Many Jackrabbit users ask to the dev team to make a tutorial on OCM");

            ocm.insert(pressRelease);
            ocm.save();

            pressRelease.setTitle("Another title");
            ocm.checkout("/newtutorial");
            ocm.update(pressRelease);
            ocm.save();
            ocm.checkin("/newtutorial");

            System.out.println("Retrieve a press release from the repository");
            pressRelease = (PressRelease) ocm.getObject("/newtutorial");
            System.out.println("PressRelease title : " + pressRelease.getTitle());

            System.out.println("Remove a press release from the repository");
            ocm.remove(pressRelease);
            ocm.save();

        } catch (Exception e) {
            throw new RepositoryException(e);
        }
    }

    private void getAllVersions(ObjectContentManager ocm) throws VersionException {
        try {
            VersionIterator versionIterator = ocm.getAllVersions("/newtutorial");

            while (versionIterator.hasNext()) {
                Version version = (Version) versionIterator.next();
                System.out.println("version found : " + version.getName() + " - " + version.getPath() + " - " + version.getCreated().getTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAllVersions(ObjectContentManager ocm) throws VersionException {
        VersionIterator versionIterator = ocm.getAllVersions("/newtutorial");
        PressRelease press;
        while (versionIterator.hasNext()) {
            Version version = (Version) versionIterator.next();
            System.out.println("version found : " + version.getName() + " - " + version.getPath() + " - " + version.getCreated().getTime());
            if (!JcrConstants.JCR_ROOTVERSION.equals(version.getName())) {
                press = (PressRelease) ocm.getObject("/page", version.getName());
                System.out.println("Content : " + press.getContent());
            }
        }
    }

    private Session getLocalSession() {
        Repository repository = RepositoryUtil.getTrancientRepository();
        return RepositoryUtil.login(repository, "admin", "admin");
    }

    private ObjectContentManager getOCM() {
        Session session = getLocalSession();

        List<Class> classes = new ArrayList<Class>();
        classes.add(PressRelease.class);

        Mapper mapper = new AnnotationMapperImpl(classes);
        return new ObjectContentManagerImpl(session, mapper);
    }
}
