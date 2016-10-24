package bo.gob.asfi.hibernatedemo2.entity.associations.OneToManyB;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fernando on 10/19/16.

 */


@Entity
@Table(name="c_customer", schema = "public")
@NamedQueries({
	@NamedQuery(
		name = "CfindAllCustomersWithPhones",
		query = "select c from CCustomer c left join fetch c.phones"),
	@NamedQuery(
		name = "CfindAllCustomers",
		query = "select c from CCustomer c")
})


@NamedNativeQueries({
	@NamedNativeQuery(
		name = "CfindAllCustomersWithPhonesNativeSql",
		query = "select * from c_customer limit 20",
		resultClass = CCustomer.class
	)
})

public class CCustomer
{
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name="id")
	Integer id;


	@Column(name="name",length = 50, nullable=false)
	String name;

	@Column(name="location")
	String location;

	@Column(name="date")
	@Temporal(TemporalType.TIMESTAMP)
	Date date;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name="customer_id")
	private List<CPhone> phones = new ArrayList<>();

	public CCustomer()
	{

	}

	public CCustomer(Integer id, String name, String location, Date date, List<CPhone> phones)
	{
		this.id = id;
		this.name = name;
		this.location = location;
		this.date = date;
		this.phones = phones;
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	public List<CPhone> getPhones()
	{
		return phones;
	}

	public void setPhones(List<CPhone> phones)
	{
		this.phones = phones;
	}
}
