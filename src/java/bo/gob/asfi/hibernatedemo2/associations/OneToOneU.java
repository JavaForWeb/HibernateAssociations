package bo.gob.asfi.hibernatedemo2.associations;

import bo.gob.asfi.hibernatedemo2.entity.associations.ManyToOne.ACustomer;
import bo.gob.asfi.hibernatedemo2.entity.associations.OneToManyU.BCustomer;
import bo.gob.asfi.hibernatedemo2.entity.associations.OneToManyU.BPhone;
import bo.gob.asfi.hibernatedemo2.entity.associations.OneToOneU.DPhone;
import bo.gob.asfi.hibernatedemo2.entity.associations.OneToOneU.DPhoneDetails;
import bo.gob.asfi.hibernatedemo2.utils.Common;
import bo.gob.asfi.hibernatedemo2.utils.DBSession;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import javax.persistence.FetchType;
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
public class OneToOneU
{
	static Logger log = Logger.getLogger(OneToOneU.class.getName());
	static Random random = new Random();


	private static void truncateTables()
	{
		System.out.println("truncate table d_phonedetails, d_phone");
		Session session = DBSession.getInstance().getSession();

		session.beginTransaction();
		SQLQuery query1 = session.createSQLQuery( "truncate table d_phonedetails, d_phone;" );
		query1.executeUpdate();

		session.getTransaction().commit();

	}

	private static void loadPhone()
	{

		System.out.println("Loading phone...");

		DPhone phone = new DPhone("2" + random.nextInt(9999999));
		DPhoneDetails phoneDetails = new DPhoneDetails("GrandStream", "SIP");

		phone.setDetails(phoneDetails);


		// start a transaction
		Session session = DBSession.getInstance().getSession();
		session.beginTransaction();
		session.save(phone);
		session.save(phoneDetails);

		session.getTransaction().commit();
		log.info( "saved Phone " + phone.getId() + " " + phone.getNumber() + "  " + phoneDetails.getProvider());

	}



	private static void displayPhonesQuery()
	{
		System.out.println("\ntable d_phone, d_phonedetails (Entity Query)" );
		Session session = DBSession.getInstance().getSession();

		session.beginTransaction();

		String sql = "SELECT * " +
			" FROM d_phone limit 20";


		SQLQuery query = session.createSQLQuery(sql);
		query.addEntity(DPhone.class);

		List phones = query.list();

		System.out.printf("|%6s|%-30s|%-30s|\n", "id", "name", "location");

		for (Iterator iterator = phones.iterator(); iterator.hasNext();) {
			DPhone phone = (DPhone)iterator.next();
			System.out.printf("|%6d|%-30s|%-30s|%-30s|\n",
				phone.getId(),
				phone.getNumber(),
				phone.getDetails().getProvider(),
				phone.getDetails().getTechnology()
			);
		}
		session.getTransaction().commit();
	}

	private static void displayPhonesLazyQuery()
	{
		System.out.println("\ntable d_phone, d_phonedetails (Entity Query)" );
		Session session = DBSession.getInstance().getSession();

		session.beginTransaction();

		String sql = "SELECT * " +
			" FROM d_phone limit 20";


		SQLQuery query = session.createSQLQuery(sql);
		query.addEntity(DPhone.class);

		List phones = query.list();

		System.out.printf("|%6s|%-30s|%-30s|\n", "id", "name", "location");

		for (Iterator iterator = phones.iterator(); iterator.hasNext();) {
			DPhone phone = (DPhone)iterator.next();

			String provider = null;
			String technology = null;

			/*
			if (phone.getDetails() != null) {
				provider = phone.getDetails().getProvider();
				technology = phone.getDetails().getTechnology();
			}
			*/
			System.out.printf("|%6d|%-30s|%-30s|%-30s|\n", phone.getId(), phone.getNumber(), provider, technology);
		}
		session.getTransaction().commit();
	}


	static public void run()
	{
		//truncateTables();
		loadPhone();

		//change fetch = FetchType.LAZY to FetchType.EAGER;
		displayPhonesQuery();

		displayPhonesLazyQuery();


	}
}
