package Database;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity(name="symbol")
@SequenceGenerator(name="symbol_id_seq", sequenceName="symbol_id_seq", allocationSize=1)
public class Symbol implements Serializable
{
	@Id
	@Column(name="id")
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="symbol_id_seq")
	private int id_;
	
	@ManyToOne
	@JoinColumn(name="exchange_id", referencedColumnName="id")
	private Exchange exchange_;
	
	@Column(name="ticker")
	private String ticker_;
	
	@Column(name="instrument")
	private String instrument_;
	
	@Column(name="name")
	private String name_;
	
	@Column(name="sector")
	private String sector_;
	
	@Column(name="currency")
	private String currency_;
	
	@Column(name="created_date")
	private Timestamp createdDate_;
	
	@Column(name="last_updated_date")
	private Timestamp lastUpdatedDate_;
	
}
