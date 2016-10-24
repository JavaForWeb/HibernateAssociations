package bo.gob.asfi.hibernatedemo2.entity.associations.OneToManyU;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name="b_customer", schema = "public")
@NamedQueries({
	@NamedQuery(
		name = "findAllCustomersWithPhones",
		query = "select c from BCustomer c left join fetch c.phones"),
	@NamedQuery(
		name = "findAllCustomers",
		query = "select c from BCustomer c")
})

@NamedNativeQueries({
	@NamedNativeQuery(
		name = "findAllCustomersWithPhonesNativeSql",
		query = "select * from b_customer limit 20",
		resultClass = BCustomer.class
	)
})

public class BCustomer
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

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name="customer_id")
	private List<BPhone> phones = new ArrayList<>();

	public BCustomer()
	{

	}

	public BCustomer(Integer id, String name, String location, Date date, ArrayList<BPhone> phones)
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

	public List<BPhone> getPhones()
	{
		return phones;
	}

	public void setPhones(List<BPhone> phones)
	{
		this.phones = phones;
	}
}
