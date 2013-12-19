package Database;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;


public class ChartInterval implements Serializable 
{
	public enum TimeFrame {MONTH,DAY,HOUR,MINUTE,SECOND};
	public enum Currency {USD,EUR,JPY,CAD};
	
	@Id 
	@Column(name="START_TIME",nullable=false)
	private Timestamp startTime_;
	
	@Id
	@Column(name="TIME_FRAME",nullable=false)
	private TimeFrame timeFrame_;
	
	@Column(name="CURRENCY")
	private Currency currency_;
	
	@Column(name="OPEN")
	private float open_;
	
	@Column(name="CLOSE")
	private float close_;
	
	@Column(name="HIGH")
	private float high_;
	
	@Column(name="LOW")
	private float low_;
	
	@Column(name="VOLUME")
	private float volume_;
	
	@Column(name="ADJ_CLOSE")
	private float adjClose_;
	
	public ChartInterval(TimeFrame timeFrame, Timestamp start, Currency currency)
	{
		timeFrame_ = timeFrame;
		startTime_ = start;
		currency_ = currency;
	}
	
	public void setHigh(float high)
	{
		high_ = high;
	}
	
	public void setLow(float low)
	{
		low_ = low;
	}
}
