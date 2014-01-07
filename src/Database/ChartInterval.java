package Database;

import javax.persistence.*;

import java.io.Serializable;
import java.sql.Timestamp;

@Entity(name="CHART_INTERVAL")
public class ChartInterval implements Serializable 
{
	public enum TimeFrame {MONTH,DAY,HOUR,MINUTE,SECOND};
	public enum Currency {USD,EUR,JPY,CAD};

	@Id 
	private String id_;
	
	@Column(name="START_TIME",nullable=false)
	private Timestamp startTime_;
	
	@Column(name="TIME_FRAME",nullable=false)
	@Enumerated(EnumType.STRING)
	private TimeFrame timeFrame_;
	
	@ManyToOne
	private Stock stock_;
	
	@Column(name="CURRENCY")
	@Enumerated(EnumType.STRING)
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
	
	public ChartInterval()
	{
		timeFrame_ = null;
		startTime_ = null;
		currency_ = null;
	}
	
	public void setSymbol(Stock stock)
	{
		stock_ = stock;
	}
	
	public void setHigh(float high)
	{
		high_ = high;
	}
	
	public void setLow(float low)
	{
		low_ = low;
	}
	
	public void setOpen(float open)
	{
		open_=open;
	}
	
	public void setClose(float close)
	{
		close_=close;
	}
	
	public void setVolume(float volume)
	{
		volume_=volume;
	}
	
	public void setAdjclose(float adjclose)
	{
		adjClose_=adjclose;
	}
	
	public void setKey()
	{
		id_= stock_.getSymbol() + "_" + stock_.getExchange().toString() + "_" + timeFrame_.toString() + "_" + startTime_.toString();
	}

}
