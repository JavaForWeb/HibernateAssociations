package bo.gob.asfi.hibernatedemo2.entity.associations.ManyToManyB;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by fernando on 10/24/16.
 */
@Entity
@Table(name="g_address", schema = "public")
public class GAddress
{
	@Id
	@GeneratedValue
	private Long id;

	private String street;

	@Column(name = "number")
	private String number;

	@ManyToMany(mappedBy = "addresses")
	private List<GCustomer> owners = new ArrayList<>();

	public GAddress() {
	}

	public GAddress(String street, String number) {
		this.street = street;
		this.number = number;
	}

	public Long getId() {
		return id;
	}

	public String getStreet() {
		return street;
	}

	public String getNumber() {
		return number;
	}

	public List<GCustomer> getOwners() {
		return owners;
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}
		GAddress address = (GAddress) o;
		return Objects.equals(street, address.street) &&
			Objects.equals( number, address.number );
	}

	@Override
	public int hashCode() {
		return Objects.hash( street, number );
	}
}
