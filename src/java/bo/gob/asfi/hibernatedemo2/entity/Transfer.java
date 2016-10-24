package bo.gob.asfi.hibernatedemo2.entity;;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Created by fernando on 10/19/16.
 */

@Entity
@Table(name="transfer", schema = "public")
public class Transfer
{
	@Id
	@Column(name="id")
	private Integer id;

	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn(name = "source",
		foreignKey = @ForeignKey(name = "SOURCE_ID_FK")
	)
	private Account source;

	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn(name = "target",
		foreignKey = @ForeignKey(name = "TARGET_ID_FK")
	)
	private Account target;

	@Column(name="description")
	private String description;

	@Column(name="amount")
	private Integer amount;

	@Column(name="status")
	private String status;

	public Transfer()
	{
	}

	public Transfer(Integer id, Account source, Account target, Integer amount, String description, String status)
	{
		this.id = id;
		this.source = source;
		this.target = target;
		this.description = description;
		this.amount = amount;
		this.status = status;
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Account getSource()
	{
		return source;
	}

	public void setSource(Account source)
	{
		this.source = source;
	}

	public Account getTarget()
	{
		return target;
	}

	public void setTarget(Account target)
	{
		this.target = target;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Integer getAmount()
	{
		return amount;
	}

	public void setAmount(Integer amount)
	{
		this.amount = amount;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}
}
