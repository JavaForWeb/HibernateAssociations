package bo.gob.asfi.hibernatedemo2.entity.associations.ManyToManyU;

import bo.gob.asfi.hibernatedemo2.entity.associations.ManyToManyB.GAddress;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by fernando on 10/24/16.
 */

@Entity
@Table(name="f_customer", schema = "public")
@NamedQueries({
	@NamedQuery(
		name = "FCustomersHQLwithLAZY",
		query = "select c from FCustomer c"),
	@NamedQuery(
	name = "FCustomersHQLwithJoin",
	query = "select c from FCustomer c left join fetch FAddress")
})


@NamedNativeQueries({
	@NamedNativeQuery(
		name = "FfindAllCustomersWithAddressNativeSql",
		query = "select * from f_customer limit 20",
		resultClass = FCustomer.class
	)
})


public class FCustomer
{
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name="id")
	Integer id;


	@Column(name="name",length = 50, nullable=false)
	String name;

	@Column(name="location",length = 50, nullable=false)
	String location;

	@Column(name="date")
	@Temporal(TemporalType.TIMESTAMP) Date date;

	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<FAddress> addresses = new ArrayList<>();

	public FCustomer()
	{

	}

	public FCustomer(String name, String location, Date date)
	{
		this.id = null;
		this.name = name;
		this.location = location;
		this.date = date;
		this.addresses = new ArrayList<>();
	}

	public List<FAddress> getAddresses() {
		return addresses;
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

	public void setAddresses(List<FAddress> addresses)
	{
		this.addresses = addresses;
	}
}
