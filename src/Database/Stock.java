package Database;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.*;

@Entity(name = "STOCK")
public class Stock implements Serializable 
{
	public enum Exchange {NYSE,NASDAQ};
	
	public Stock(String symbol, Exchange exchange)
	{
		symbol_ = symbol;
		exchange_ = exchange;
	}
	
	public void addChartInterval(ChartInterval interval)
	{
		chart_.add(interval);
	}
	
	@Id
	@Column(name="SYMBOL",nullable=false)
	private String symbol_;
	
	@Id
	@Column(name="EXCHANGE",nullable=false)
	private Exchange exchange_;
	
	@OneToMany
	private List<ChartInterval> chart_;
}
