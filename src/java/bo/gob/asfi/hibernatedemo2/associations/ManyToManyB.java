package bo.gob.asfi.hibernatedemo2.associations;

import bo.gob.asfi.hibernatedemo2.entity.associations.ManyToManyB.GAddress;
import bo.gob.asfi.hibernatedemo2.entity.associations.ManyToManyB.GCustomer;
import bo.gob.asfi.hibernatedemo2.entity.associations.OneToManyB.CCustomer;
import bo.gob.asfi.hibernatedemo2.entity.associations.OneToManyB.CPhone;
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
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by fernando on 10/22/16.
 */
public class ManyToManyB
{
	static Logger log = Logger.getLogger(ManyToManyB.class.getName());
	static Random random = new Random();


	private static void truncateTables()
	{
		System.out.println("truncate tables");
		Session session = DBSession.getInstance().getSession();

		session.beginTransaction();
		SQLQuery query1 = session.createSQLQuery( "truncate table g_customer, g_address, g_customer_g_address;" );
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

		ArrayList<GAddress> addresses = new ArrayList<>(100);
		Session session = DBSession.getInstance().getSession();
		for (int i = 0; i<100; i++) {
			GAddress address = new GAddress("Street " + random.nextInt(100), "" + random.nextInt(99)+10);
			addresses.add(address);
			session.beginTransaction();
			session.save(address);
		}
		session.getTransaction().commit();

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

				GCustomer customer = new GCustomer(name, location, date);

				GAddress address1 = addresses.get( random.nextInt(100));
				customer.addAddress(address1);
				GAddress address2 = addresses.get( random.nextInt(100));
				customer.addAddress(address2);

				// start a transaction
				session = DBSession.getInstance().getSession();
				session.beginTransaction();
				session.save(customer);
				session.getTransaction().commit();

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
			"FROM c_customer as c " +
			"join c_phone as p on (c.id = p.customer_id)" +
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

		List<CCustomer> customers = session.
			getNamedQuery("CfindAllCustomersWithPhonesNativeSql").
			list();

		System.out.printf("|%6s|%-30s|%-30s|%-10s|\n", "id", "name", "location", "number");
		for (CCustomer customer: customers) {

			for (CPhone phone : customer.getPhones() ) {
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

		Query query = session.getNamedQuery("CfindAllCustomers").
			setFirstResult(1).
			setMaxResults(20);

		List<CCustomer> customers = query.list();

		System.out.printf("|%6s|%-30s|%-30s|%-10s|\n", "id", "name", "location", "number");
		for (CCustomer customer: customers) {

			for (CPhone phone : customer.getPhones() ) {
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

		Query query = session.getNamedQuery("CfindAllCustomersWithPhones").
			setMaxResults(20);

		List<CCustomer> customers = query.list();

		System.out.printf("|%6s|%-30s|%-30s|%-10s|\n", "id", "name", "location", "number");
		for (CCustomer customer: customers) {

			for (CPhone phone : customer.getPhones() ) {
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
