package bo.gob.asfi.hibernatedemo2.entity.associations.OneToOneU;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by fernando on 10/22/16.
 */

@Entity
@Table(name = "d_phonedetails", schema = "public")

public class DPhoneDetails
{
	@Id
	@GeneratedValue
	private Long id;

	private String provider;

	private String technology;

	public DPhoneDetails() {
	}

	public DPhoneDetails(String provider, String technology) {
		this.provider = provider;
		this.technology = technology;
	}

	public String getProvider() {
		return provider;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

}
