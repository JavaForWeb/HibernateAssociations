package bo.gob.asfi.hibernatedemo2.entity.associations.OneToManyB;

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
@Table(name = "c_phone", schema = "public")

public  class CPhone
{

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn(name = "customer_id",
		foreignKey = @ForeignKey(name = "CUSTOMER_ID_FK")
	)
	private CCustomer customer;


	@Column(name = "number")
	private String number;

	public CPhone() {
	}

	public CPhone(String number) {
		this.number = number;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public void setNumber(String number)
	{
		this.number = number;
	}

	public String getNumber() {
		return number;
	}

	public CCustomer getCustomer()
	{
		return customer;
	}

	public void setCustomer(CCustomer customer)
	{
		this.customer = customer;
	}
}
