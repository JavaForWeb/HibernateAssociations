package bo.gob.asfi.hibernatedemo2.associations;

import bo.gob.asfi.hibernatedemo2.entity.associations.ManyToOne.ACustomer;
import bo.gob.asfi.hibernatedemo2.entity.associations.ManyToOne.APhone;
import bo.gob.asfi.hibernatedemo2.entity.associations.OneToManyU.BCustomer;
import bo.gob.asfi.hibernatedemo2.entity.associations.OneToManyU.BPhone;
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
public class OneToManyU
{
	static Logger log = Logger.getLogger(OneToManyU.class.getName());
	static Random random = new Random();


	private static void truncateTables()
	{
		System.out.println("truncate tables");
		Session session = DBSession.getInstance().getSession();

		session.beginTransaction();
		SQLQuery query1 = session.createSQLQuery( "truncate table b_customer, b_phone;" );
		query1.executeUpdate();

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

				ArrayList<BPhone> phones = new ArrayList<>();
				BCustomer customer = new BCustomer(null, name, location, date, phones);

				//add some phones to this customer
				Integer nPhones = random.nextInt(5);
				ArrayList<BPhone> BPhones = new ArrayList<>(nPhones);
				for (int i = 0; i< nPhones;i++) {
					BPhone phone = new BPhone("2" + random.nextInt(9999999));
					customer.getPhones().add(phone);
				}

				// start a transaction
				Session session = DBSession.getInstance().getSession();
				session.beginTransaction();
				session.save(customer);
				session.getTransaction().commit();
				log.info( "saved ACustomer " + customer.getId() + " " + name + " and " + nPhones + " phones");

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
		System.out.println("\nscalar query (returns List<Map>)");
		Session session = DBSession.getInstance().getSession();

		session.beginTransaction();

		String sql = "SELECT c.id, name, location, number " +
			"FROM b_customer as c " +
			"join b_phone as p on (c.id = p.customer_id)" +
			" limit 20";
		SQLQuery query = session.createSQLQuery(sql);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		//query.setResultTransformer(Criteria.PROJECTION);
		List data = query.list();

		System.out.printf("|%6s|%-30s|%-30s|%-10s|\n", "id", "name", "location", "number");
		for(Object object : data) {
			Map row = (Map)object;
			System.out.printf("|%6d|%-30s|%-30s|%-10s|\n",
				row.get("id"),
				row.get("name"),
				row.get("location"),
				row.get("number")
			);
		}
		session.getTransaction().commit();
	}

	private static void displayCustomersEntityQuery()
	{
		System.out.println("\nNative NamedQuery to returns Customer using LAZY" );
		Session session = DBSession.getInstance().getSession();

		session.beginTransaction();

		//String sql = "SELECT * FROM a_customer limit 20";
		//SQLQuery query = session.createSQLQuery(sql);
		List<BCustomer> customers = session.
			getNamedQuery("findAllCustomersWithPhonesNativeSql").
			list();

		System.out.printf("|%6s|%-30s|%-30s|%-10s|\n", "id", "name", "location", "number");
		for (BCustomer customer: customers) {

			for (BPhone phone : customer.getPhones() ) {
				System.out.printf("|%6d|%-30s|%-30s|%-10s|\n",
					customer.getId(),
					customer.getName(),
					customer.getLocation(),
					phone.getNumber()
				);
			}
		}

		session.getTransaction().commit();
	}

	private static void displayCustomersHQL()
	{
		System.out.println("\nNamed HQL query using LAZY fetch" );

		Session session = DBSession.getInstance().getSession();
		session.beginTransaction();

		Query query = session.getNamedQuery("findAllCustomers").
			setFirstResult(1).
			setMaxResults(20);

		List<BCustomer> customers = query.list();

		System.out.printf("|%6s|%-30s|%-30s|%-10s|\n", "id", "name", "location", "number");
		for (BCustomer customer: customers) {

			for (BPhone phone : customer.getPhones() ) {
				System.out.printf("|%6d|%-30s|%-30s|%-10s|\n",
					customer.getId(),
					customer.getName(),
					customer.getLocation(),
					phone.getNumber()
				);
			}
		}

		session.getTransaction().commit();
	}

	private static void displayCustomersNamedQuery()
	{
		System.out.println("\nNamed HQL query using join fetch to avoid LAZY fetchs" );

		Session session = DBSession.getInstance().getSession();
		session.beginTransaction();

		String hql = "from BCustomer  ";
		Query query = session.getNamedQuery("findAllCustomersWithPhones").
			setMaxResults(20);

		List<BCustomer> customers = query.list();

		System.out.printf("|%6s|%-30s|%-30s|%-10s|\n", "id", "name", "location", "number");
		for (BCustomer customer: customers) {

			for (BPhone phone : customer.getPhones() ) {
				System.out.printf("|%6d|%-30s|%-30s|%-10s|\n",
					customer.getId(),
					customer.getName(),
					customer.getLocation(),
					phone.getNumber()
				);
			}
		}

		session.getTransaction().commit();
	}


	static public void run()
	{
		truncateTables();
		loadCustomers();

		displayCustomersScalarQuery();
		displayCustomersEntityQuery();
		displayCustomersHQL();
		displayCustomersNamedQuery();


	}
}
