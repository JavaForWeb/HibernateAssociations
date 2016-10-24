package bo.gob.asfi.hibernatedemo2.associations;

import bo.gob.asfi.hibernatedemo2.entity.associations.ManyToOne.ACustomer;
import bo.gob.asfi.hibernatedemo2.entity.associations.ManyToOne.APhone;
import bo.gob.asfi.hibernatedemo2.utils.Common;
import bo.gob.asfi.hibernatedemo2.utils.DBSession;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by fernando on 10/22/16.
 */
public class ManyToOne
{
	static Logger log = Logger.getLogger(ManyToOne.class.getName());
	static Random random = new Random();


	private static void truncateTables()
	{
		System.out.println("truncate table a_customer. a_phone");
		Session session = DBSession.getInstance().getSession();

		session.beginTransaction();
		SQLQuery query1 = session.createSQLQuery( "truncate table a_customer, a_phone;" );
		query1.executeUpdate();

		//session.beginTransaction();
		//SQLQuery query1 = session.createSQLQuery( "truncate table a_phone;" );
		//query1.executeUpdate();

		//SQLQuery query2 = session.createSQLQuery( "truncate table a_customer;" );
		//query2.executeUpdate();

		session.getTransaction().commit();

	}

	private static void loadCustomers()
	{

		System.out.println("Loading customers...");

		BufferedReader br = Common.getBufferedReader("./src/resources/fixtures/accounts.txt");

		String line;
		Integer count = 0;
		SimpleDateFormat df = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
		Common.startTimer();

		try {
			while ((line = br.readLine())!=null) {

				String[] fields = line.split("\\|");

				String name        = Common.sanitify(fields[1]);
				String location    = Common.sanitify(fields[3]);

				//parsing the date
				Date date = null;
				try {
					date = df.parse(fields[5]);
				}catch (ParseException e) {
					log.error(e.getMessage());
				}

				ACustomer ACustomer = new ACustomer(null, name, location, date);

				//add some phones to this customer
				Integer nPhones = random.nextInt(5);
				ArrayList<APhone> APhones = new ArrayList<>(nPhones);
				for (int i = 0; i< nPhones;i++) {
					APhone APhone = new APhone("2" + random.nextInt(9999999));
					APhone.setPerson(ACustomer);
					APhones.add(APhone);
				}

				// start a transaction
				Session session = DBSession.getInstance().getSession();
				session.beginTransaction();
				session.save(ACustomer);
				for (int i = 0; i< nPhones;i++) {
					session.save(APhones.get(i));
				}
				session.getTransaction().commit();
				log.info( "saved ACustomer " + ACustomer.getId() + " " + name + " and " + nPhones + " phones");

				count++;
				if (count == 1000) {
					break;
				}
			}

			Common.endTimer();
			System.out.format("    %d records in %5.1f secs. \n", count, Common.elapsedTime()/1000.0);
		} catch(IOException e) {
			log.error(e.getMessage());
		}
	}


	private static void displayCustomersScalarQuery()
	{
		System.out.println("\ntable customers scalar query");
		Session session = DBSession.getInstance().getSession();

		session.beginTransaction();

		String sql = "SELECT id, name, location FROM a_customer limit 20";
		SQLQuery query = session.createSQLQuery(sql);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List data = query.list();

		System.out.printf("|%6s|%-30s|%-30s|\n", "id", "name", "location");
		for(Object object : data) {
			Map row = (Map)object;
			System.out.printf("|%6d|%-30s|%-30s|\n", row.get("id"), row.get("name"), row.get("location"));
		}
		session.getTransaction().commit();
	}


	private static void displayCustomersEntityQuery()
	{
		System.out.println("\ntable a_customers (Entity Query)" );
		Session session = DBSession.getInstance().getSession();

		session.beginTransaction();

		//String sql = "SELECT id, name, location, date FROM a_customer limit 20";
		String sql = "SELECT * FROM a_customer limit 20";
		SQLQuery query = session.createSQLQuery(sql);
		query.addEntity(ACustomer.class);
		List<ACustomer> customers = (List<ACustomer>)query.list();
		session.getTransaction().commit();

		System.out.printf("|%6s|%-30s|%-30s|\n", "id", "name", "location");
		for (ACustomer customer: customers) {
			System.out.printf("|%6d|%-30s|%-30s|\n", customer.getId(), customer.getName(), customer.getLocation());
		}

		/*
		System.out.printf("|%6s|%-30s|%-30s|\n", "id", "name", "location");
		for (Iterator iterator = customers.iterator(); iterator.hasNext();) {
			ACustomer customer = (ACustomer)iterator.next();
			System.out.printf("|%6d|%-30s|%-30s|\n", customer.getId(), customer.getName(), customer.getLocation());
		}
		*/
	}

	private static void displayCustomersHQL()
	{
		System.out.println("\ntable ACustomers (HQL)" );
		Session session = DBSession.getInstance().getSession();

		session.beginTransaction();

		String hql = "from ACustomer where name = :nombre ";
		Query query = session.createQuery(hql);
		query.setParameter("nombre", "Mariah");
		List<ACustomer> ACustomers = query.list();

		System.out.printf("|%-30s|%-30s|\n", "name", "location");
		for (Iterator iterator = ACustomers.iterator(); iterator.hasNext();) {
			ACustomer ACustomer = (ACustomer)iterator.next();
			System.out.printf("|%-30s|%-30s|\n", ACustomer.getName(), ACustomer.getLocation());
		}
		session.getTransaction().commit();
	}

	static public void run()
	{
		//truncateTables();
		//loadCustomers();

		//displayCustomersScalarQuery();
		//displayCustomersEntityQuery();
		displayCustomersHQL();

	}
}
