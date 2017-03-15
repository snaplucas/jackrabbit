package ocm.util;

import org.apache.jackrabbit.core.TransientRepository;
import org.apache.jackrabbit.ocm.exception.RepositoryException;

import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

public class RepositoryUtil {

    public static Repository getTrancientRepository() {
        return new TransientRepository();
    }

    public static Session login(Repository repository, String user, String password) throws RepositoryException {
        try {
            return repository.login(new SimpleCredentials(user, password.toCharArray()));
        } catch (Exception e) {
            throw new RepositoryException("Impossible to login ", e);
        }
    }
}
