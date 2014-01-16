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
	
	public Symbol(String ticker, 
				  String instrument,
				  Timestamp createdDate,
				  Timestamp updatedDate)
	{
		ticker_=ticker;
		instrument_=instrument;
		createdDate_=createdDate;
		lastUpdatedDate_=updatedDate;
	}
	
	
	public Symbol()
	{
		
	}
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="symbol_id_seq")
	private int id_;
	
	@ManyToOne
	@JoinColumn(name="exchange_id", referencedColumnName="id")
	private Exchange exchange_;
	
	@Column(name="ticker", nullable=false)
	private String ticker_;
	
	@Column(name="instrument", nullable=false)
	private String instrument_;
	
	@Column(name="name", nullable=true)
	private String name_;
	
	@Column(name="sector", nullable=true)
	private String sector_;
	
	@Column(name="currency", nullable=true)
	private String currency_;
	
	@Column(name="created_date", nullable=false)
	private Timestamp createdDate_;
	
	@Column(name="last_updated_date", nullable=false)
	private Timestamp lastUpdatedDate_;
	
	public void setExchange(Exchange exchange)
	{
		exchange_=exchange;
	}
	
	public void setTicker(String ticker)
	{
		ticker_=ticker;
	}
	
	public String getTicker()
	{
		return ticker_;
	}
	
}
