package bo.gob.asfi.hibernatedemo2.entity.associations.ManyToManyU;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by fernando on 10/24/16.
 */
@Entity
@Table(name="f_address", schema = "public")
public class FAddress
{
	@Id
	@GeneratedValue
	private Long id;

	private String street;

	@Column(name = "number")
	private String number;

	public FAddress() {
	}

	public FAddress(String street, String number) {
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
}
