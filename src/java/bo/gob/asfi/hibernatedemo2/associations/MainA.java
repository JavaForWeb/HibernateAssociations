package bo.gob.asfi.hibernatedemo2.associations;

import bo.gob.asfi.hibernatedemo2.utils.Common;
import bo.gob.asfi.hibernatedemo2.utils.DBSession;
import org.apache.log4j.Logger;

import java.util.Properties;

public class MainA
{
    static Logger log = Logger.getLogger(MainA.class.getName());
    static final String appTitle = "Hibernate associations with Postgress";

    private static void initHibernate()
    {
        Common.displayTitle(appTitle);

        Properties config = Common.readConfig();

        log.info("Starting Hibernate Associations tutorial ");

        DBSession.getInstance().initFactory(config);

    }

    private static void initHibernateDebug()
    {
        Properties config = Common.readConfig();

        log.info("Starting Hibernate Factory with Debug enabled ");
        config.setProperty("show_sql", "true");
        DBSession.getInstance().initFactory(config);

    }

    public static void main(String[] args) {
        System.out.println(appTitle);
        initHibernateDebug();
        //initHibernate();

        //a. ManyToOne
        //ManyToOne.run();

        //b.
        //OneToManyU.run();

        //c.
        //OneToManyB.run();

        //d.
        //OneToOneU.run();

        //e.
        //OneToOneB.run();

        //f.
        //ManyToManyU.run();

        //g.
        //ManyToManyB.run();

        System.out.println("Done");
    }
}
