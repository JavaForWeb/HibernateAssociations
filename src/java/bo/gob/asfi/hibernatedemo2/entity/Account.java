package bo.gob.asfi.hibernatedemo2.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Created by fernando on 10/19/16.

 */


@Entity
@Table(name="account", schema = "public")
public class Account
{
	@Id
	//@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id", unique=true)
	Integer id;


	@Column(name="name")
	String name;

	@Column(name="screen_name")
	String screenName;

	@Column(name="location")
	String location;

	@Column(name="description")
	String description;

	@Column(name="date")
	String date;

	@Column(name="balance")
	Integer balance;

	public Account()
	{

	}

	public Account(Integer id, String name, String screenName, String location, String description, Integer balance, String date)
	{
		this.id = id;
		this.name = name;
		this.screenName = screenName;
		this.location = location;
		this.description = description;
		this.balance = balance;
		this.date = date;
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

	public String getScreenName()
	{
		return screenName;
	}

	public void setScreenName(String screenName)
	{
		this.screenName = screenName;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getDate()
	{
		return date;
	}

	public void setDate(String date)
	{
		this.date = date;
	}

	public Integer getBalance()
	{
		return balance;
	}

	public void setBalance(Integer balance)
	{
		this.balance = balance;
	}
}
