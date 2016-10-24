package bo.gob.asfi.hibernatedemo2.entity.associations.ManyToOne;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by fernando on 10/21/16.
 */
@Entity
@Table(name = "a_phone", schema = "public")
public  class APhone
{

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn(name = "customer_id123",
		foreignKey = @ForeignKey(name = "CUSTOMER_ID_FK")
	)
	private ACustomer ACustomer;

	@Column(name = "number")
	private String number;

	public APhone() {
	}

	public APhone(String number) {
		this.number = number;
	}

	public Long getId() {
		return id;
	}

	public String getNumber() {
		return number;
	}

	public ACustomer getACustomer() {
		return ACustomer;
	}

	public void setPerson(ACustomer person) {
		this.ACustomer = person;
	}
}
