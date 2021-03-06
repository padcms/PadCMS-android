package padcms.dao;

import java.util.Map;

import padcms.dao.application.ApplicationFactory;
import padcms.dao.application.IssueFactory;
import padcms.dao.application.RevisionFactory;
import padcms.dao.application.bean.Application;
import padcms.dao.application.bean.Issue;
import padcms.dao.application.bean.Revision;
import android.database.sqlite.SQLiteDatabase;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.DaoConfig;
import de.greenrobot.dao.IdentityScopeType;



// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DBApplicationSession extends AbstractDaoSession {

    private final DaoConfig applicationFactoryConfig;
    private final DaoConfig revisionFactoryConfig;
    private final DaoConfig issueFactoryConfig;

    private final ApplicationFactory applicationFactory;
    private final RevisionFactory revisionFactory;
    private final IssueFactory issueFactory;
    

    public DBApplicationSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        applicationFactoryConfig = daoConfigMap.get(ApplicationFactory.class).clone();
        applicationFactoryConfig.initIdentityScope(type);

        issueFactoryConfig = daoConfigMap.get(IssueFactory.class).clone();
        issueFactoryConfig.initIdentityScope(type);
        
        revisionFactoryConfig = daoConfigMap.get(RevisionFactory.class).clone();
        revisionFactoryConfig.initIdentityScope(type);

        

        applicationFactory = new ApplicationFactory(applicationFactoryConfig, this);
        issueFactory = new IssueFactory(issueFactoryConfig, this);
        revisionFactory = new RevisionFactory(revisionFactoryConfig, this);
        
        registerDao(Application.class, applicationFactory);
        registerDao(Issue.class, issueFactory);
        registerDao(Revision.class, revisionFactory);
        
    }
    
    public void clear() {
        applicationFactoryConfig.getIdentityScope().clear();
        revisionFactoryConfig.getIdentityScope().clear();
        issueFactoryConfig.getIdentityScope().clear();
    }

    public ApplicationFactory getApplicationFactory() {
        return applicationFactory;
    }

    public IssueFactory getIssueFactory() {
        return issueFactory;
    }
    
    public RevisionFactory getRevisionFactory() {
        return revisionFactory;
    }


}
