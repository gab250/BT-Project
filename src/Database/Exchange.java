package Database;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity(name="exchange")
@SequenceGenerator(name="exchange_id_seq", sequenceName="exchange_id_seq", allocationSize=1)
public class Exchange implements Serializable
{
   public Exchange(String abbreviation,
					String name,
					String city,
					String country,
					String currency,
					Time timeZoneOffset,
					Timestamp createdDate,
					Timestamp lastUpadteDate)
	{
		abbrev_=abbreviation;
		name_=name;
		city_=city;
		country_=country;
		currency_=currency;
		timezoneOffset_=timeZoneOffset;
		createdDate_=createdDate;
		lastUpdateDate_=lastUpadteDate;
	}
		
	public Exchange()
	{
		
	}
		
	@Id
	@Column(name="id")
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="exchange_id_seq")
	private int id_;
	
	@Column(name="abbrev", nullable=false)
	private String abbrev_;
	
	@Column(name="name", nullable=false)
	private String name_;
	
	@Column(name="city", nullable=true)
	private String city_;
	
	@Column(name="country", nullable=true)
	private String country_;
	
	@Column(name="currency", nullable=true)
	private String currency_;
	
	@Column(name="timezone_offset", nullable=true)
	private Time timezoneOffset_;
	
	@Column(name="created_date", nullable=false)
	private Timestamp createdDate_;
	
	@Column(name="last_updated_date", nullable=false)
	private Timestamp lastUpdateDate_;
}
