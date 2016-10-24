package bo.gob.asfi.hibernatedemo2.entity.associations.ManyToOne;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by fernando on 10/19/16.

 */


@Entity
@Table(name="a_customer", schema = "public")
public class ACustomer
{
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name="id", unique=true)
	Integer id;


	@Column(name="name",length = 100, nullable=false)
	String name;

	@Column(name="location")
	String location;

	@Column(name="edad", nullable = false)
	Integer edad;

	@Column(name="date")
	@Temporal(TemporalType.TIMESTAMP)
	Date date;

	@Column(name="edad_minima", nullable = true)
	Integer edadMinima;

	@Column(name="edad_maxima", nullable = true)
	Integer edadMaxima;

	public ACustomer()
	{

	}

	public ACustomer(Integer id, String name, String location, Date date)
	{
		this.id = id;
		this.name = name;
		this.location = location;
		this.date = date;
		this.edad = -1;
		this.edadMinima = 0;
		this.edadMaxima = 0;
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

}
