package bo.gob.asfi.hibernatedemo2.entity.associations.OneToOneU;

import org.hibernate.annotations.Fetch;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Created by fernando on 10/22/16.
 */

@Entity
@Table(name = "d_phone", schema = "public")
public class DPhone
{
	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "`number`")
	private String number;

	@OneToOne (fetch = FetchType.LAZY)
	@JoinColumn(name = "details_id")
	private DPhoneDetails details;

	public DPhone() {
	}

	public DPhone(String number) {
		this.number = number;
	}

	public Long getId() {
		return id;
	}

	public String getNumber() {
		return number;
	}

	public DPhoneDetails getDetails() {
		return details;
	}

	public void setDetails(DPhoneDetails details) {
		this.details = details;
	}
}
