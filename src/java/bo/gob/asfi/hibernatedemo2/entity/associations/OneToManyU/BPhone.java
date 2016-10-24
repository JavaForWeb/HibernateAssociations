package bo.gob.asfi.hibernatedemo2.entity.associations.OneToManyU;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by fernando on 10/21/16.
 */
@Entity
@Table(name="b_phone", schema = "public")
public  class BPhone
{

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "number")
	private String number;

	public BPhone() {
	}

	public BPhone(String number) {
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

}
