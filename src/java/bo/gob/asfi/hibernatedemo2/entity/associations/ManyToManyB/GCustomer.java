package bo.gob.asfi.hibernatedemo2.entity.associations.ManyToManyB;

import bo.gob.asfi.hibernatedemo2.entity.associations.ManyToManyU.FAddress;

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
@Table(name="g_customer", schema = "public")
@NamedQueries({
	@NamedQuery(
		name = "GfindAllCustomersWithPhones",
		query = "select c from CCustomer c left join fetch c.phones"),
	@NamedQuery(
		name = "GfindAllCustomers",
		query = "select c from CCustomer c")
})


@NamedNativeQueries({
	@NamedNativeQuery(
		name = "GfindAllCustomersWithPhonesNativeSql",
		query = "select * from g_customer limit 20",
		resultClass = GCustomer.class
	)
})


public class GCustomer
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
	@Temporal(TemporalType.TIMESTAMP) Date date;

	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<GAddress> addresses = new ArrayList<>();

	public GCustomer()
	{

	}

	public GCustomer(String name, String location, Date date)
	{
		this.id = null;
		this.name = name;
		this.location = location;
		this.date = date;
		this.addresses = new ArrayList<>();
	}

	public List<GAddress> getAddresses() {
		return addresses;
	}

	public void addAddress(GAddress address) {
		addresses.add( address );
		address.getOwners().add( this );
	}

	public void removeAddress(GAddress address) {
		addresses.remove( address );
		address.getOwners().remove( this );
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}
		GCustomer customer = (GCustomer) o;
		return Objects.equals(name, customer.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash( name );
	}

}
