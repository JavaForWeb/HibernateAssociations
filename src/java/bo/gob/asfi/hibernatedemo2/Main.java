package bo.gob.asfi.hibernatedemo2;

import bo.gob.asfi.hibernatedemo2.entity.Account;
import bo.gob.asfi.hibernatedemo2.entity.associations.ManyToOne.ACustomer;
import bo.gob.asfi.hibernatedemo2.entity.associations.ManyToOne.APhone;
import bo.gob.asfi.hibernatedemo2.entity.Transfer;
import bo.gob.asfi.hibernatedemo2.utils.Common;
import bo.gob.asfi.hibernatedemo2.utils.DBSession;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

public class Main {
    static Logger log = Logger.getLogger(Main.class.getName());
    static final String appTitle = "PostgreSQL tutorial with Hibernate";

    static Random random = new Random();

    private static void initHibernate()
    {
        Common.displayTitle(appTitle);

        Properties config = Common.readConfig();

        log.info("Starting PostgreSQL tutorial ");

        DBSession.getInstance().initFactory(config);

    }

    private static void initHibernateDebug()
    {
        Properties config = Common.readConfig();

        log.info("Starting Hibernate Factory with Debug enabled ");
        config.setProperty("show_sql", "true");
        DBSession.getInstance().initFactory(config);

    }

    private static BufferedReader getBufferedReader( String filename) {
        BufferedReader br = null;

        log.info("reading file " + filename);

        try {
            br = new BufferedReader(new FileReader(filename));
        } catch(IOException e) {
            log.error(e.getMessage());
            System.exit(0);
        }
        return br;
    }

    private static void loadAccounts()
    {
        System.out.println("Loading accounts...");

        String filename = "./src/resources/fixtures/accounts.txt";

        BufferedReader br = getBufferedReader(filename);
        String line;
        Integer count = 0;

        try {
            Common.startTimer();
            while ((line = br.readLine())!=null) {

                String[] fields = line.split("\\|");

                Integer id         = Integer.parseInt(fields[0]);
                String name        = Common.sanitify(fields[1]);
                String screenName  = Common.sanitify(fields[2]);
                String location    = Common.sanitify(fields[3]);
                String description = Common.sanitify(fields[4]);
                String date        = Common.sanitify(fields[5]);
                Integer balance    = Integer.parseInt(fields[6]);

                Account account = new Account(id, name, screenName, location, description, balance, date);
                log.info( "saving account " + id + " " + name + " " + balance);

                // start a transaction
                Session session = DBSession.getInstance().getSession();
                session.beginTransaction();
                session.save(account);
                session.getTransaction().commit();
                count++;
                if (count % 1000 == 0) {
                    Common.endTimer();
                    System.out.format("    %d %5.1f\n", count, Common.elapsedTime()/1000.0);
                }
            }

            Common.endTimer();
            System.out.format("    %d %5.1f\n", count, Common.elapsedTime()/1000.0);
        } catch(IOException e) {
            log.error(e.getMessage());
            //System.exit(0);
        }
        finally {
        }

    }

    private static void loadTransfers()
    {
        /*
        Session session = DBSession.getInstance().getSession();

        session.createSQLQuery()
        String sql = "SELECT first_name, salary FROM EMPLOYEE";
        SQLQuery query = session.createSQLQuery(sql);
        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        List results = query.list();
        */


        System.out.println("Loading transfers...");

        String filename = "./src/resources/fixtures/transfers.txt";

        BufferedReader br = getBufferedReader(filename);
        String line;
        Integer count = 0;

        try {
            Common.startTimer();
            while ((line = br.readLine())!=null) {

                String[] fields = line.split("\\|");
                //199999|427191|166677|48|transfer of 48 from 427191 to 166677
                Integer id         = Integer.parseInt(fields[0]);
                Integer source     = Integer.parseInt(fields[1]);
                Integer target     = Integer.parseInt(fields[2]);
                Integer amount     = Integer.parseInt(fields[3]);
                String description = Common.sanitify(fields[4]);

                String status = "checking";
                String transferDesc = "";

                Session session = DBSession.getInstance().getSession();


                session.beginTransaction();

                Account accountSource = session.get(Account.class, source);
                Account accountTarget = session.get(Account.class, target);


                if (accountSource.getBalance() > amount) {
                    accountSource.setBalance( accountSource.getBalance() - amount);
                    accountTarget.setBalance( accountTarget.getBalance() + amount);

                    transferDesc = String.format("(%d)%s sent %5d to (%d)%s ", accountSource.getId(), accountSource.getName(), amount, accountTarget.getId(), accountTarget.getName());
                    status = "success";

                } else {
                    transferDesc = String.format("(%d)%s try to send %5d to (%d)%s ", accountSource.getId(), accountSource.getName(), amount, accountTarget.getId(), accountTarget.getName());
                    status = "no funds";
                }


                Transfer transfer = new Transfer(id, accountSource, accountTarget, amount, transferDesc, status);
                session.save(transfer);
                // commit the transaction
                session.getTransaction().commit();

                //session second live
                session.beginTransaction();
                accountTarget.setBalance( accountTarget.getBalance() + 1000);
                session.getTransaction().commit();


                log.info( "transfering " + amount + " from " + source + " to " + target + " " );

                count++;
                if (count % 1000 == 0) {
                    Common.endTimer();
                    System.out.format("    %d %5.1f\n", count, Common.elapsedTime()/1000.0);
                }
            }

            Common.endTimer();
            System.out.format("    %d %5.1f\n", count, Common.elapsedTime()/1000.0);
        } catch(IOException e) {
            log.error(e.getMessage());
            //System.exit(0);
        }
        finally {
        }

    }


    private static void displayCustomersScalarQuery()
    {
        System.out.println("\ntable transfer scalar query");
        Session session = DBSession.getInstance().getSession();

        session.beginTransaction();

        //String sql = "SELECT id, amount, source, target FROM transfer limit 20 ";
        String sql = "SELECT transfer.id, amount, source, target, " +
            " A1.name as nameA, A2.name as nameB " +
            " FROM transfer " +
            " join account as A1 on (source = A1.id) " +
            " join account as A2 on (target = A2.id) " +
            " limit 20 ";

        SQLQuery query = session.createSQLQuery(sql);
        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        List data = query.list();

        System.out.printf("|%6s|%-30s|%-30s|%-30s|\n", "id", "amount", "source", "target");
        for(Object object : data) {
            Map row = (Map)object;
            System.out.printf("|%6d|%-30s|%-30s|%-30s|\n",
                row.get("id"),
                row.get("amount"),
                row.get("namea"),
                row.get("nameb")
            );
        }
        session.getTransaction().commit();
    }


    private static void displayTransferEntityQuery()
    {
        System.out.println("\ntable " );
        Session session = DBSession.getInstance().getSession();

        session.beginTransaction();

        //String sql = "SELECT id, name, location, date FROM a_customer limit 20";
        String sql = "SELECT * FROM transfer limit 20";
        SQLQuery query = session.createSQLQuery(sql);
        query.addEntity(Transfer.class);

        List<Transfer> transfers = (List<Transfer>)query.list();


        System.out.printf("|%6s|%-30s|%-30s|%-30s|\n", "id", "amount", "source", "target");
        for (Transfer transfer: transfers) {

            /*
            System.out.printf("|%6d|%-30s|%-30s|%-30s|\n",
                transfer.getId(),
                transfer.getAmount(),
                transfer.getSource().getName(),
                transfer.getTarget().getName()
            );
*/
            System.out.printf("|%6d|%-30s|\n",
                transfer.getId(),
                transfer.getAmount()
            );

        }

        session.getTransaction().commit();

		/*
		System.out.printf("|%6s|%-30s|%-30s|\n", "id", "name", "location");
		for (Iterator iterator = customers.iterator(); iterator.hasNext();) {
			ACustomer customer = (ACustomer)iterator.next();
			System.out.printf("|%6d|%-30s|%-30s|\n", customer.getId(), customer.getName(), customer.getLocation());
		}
		*/
    }


    public static void main(String[] args) {
        System.out.println("Hibernate Demo");
        initHibernateDebug();

        //loadAccounts();

        //loadTransfers();

        displayCustomersScalarQuery();

        displayTransferEntityQuery();

        System.out.println("Done");
    }
}
