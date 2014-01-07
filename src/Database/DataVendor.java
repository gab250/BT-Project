package Database;

import javax.persistence.*;

import java.io.Serializable;
import java.sql.Timestamp;

@Entity(name="data_vendor")
@SequenceGenerator(name="data_vendor_id_seq", sequenceName="data_vendor_id_seq", allocationSize=1)
public class DataVendor implements Serializable
{
	public DataVendor(String name, String websiteUrl, String supportEmail, Timestamp createdDate, Timestamp lastUpdate)
	{
		name_ = name;
		websiteUrl_ = websiteUrl;
		supportEmail_= supportEmail;
		createdDate_ = createdDate;
		lastUpdatedDate_ = lastUpdate;
	}
	
	public DataVendor()
	{
		
	}
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="data_vendor_id_seq")
	private int id_;
	
	@Column(name="name", nullable=false)
	private String name_;
	
	@Column(name="website_url", nullable=true)
	private String websiteUrl_;
	
	@Column(name="support_email", nullable=true)
	private String supportEmail_;
	
	@Column(name="created_date", nullable=false)
	private Timestamp createdDate_;
	
	@Column(name="last_updated_date", nullable=false)
	private Timestamp lastUpdatedDate_;
}
