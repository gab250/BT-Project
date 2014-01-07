package Database;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.*;

@Entity(name="STOCK")
public class Stock implements Serializable 
{
	public enum Exchange {NYSE,NASDAQ};
	
	public Stock(String symbol, Exchange exchange)
	{
		symbol_ = symbol;
		exchange_ = exchange;
	}
	
	public Stock()
	{
		symbol_ =null;
		exchange_ = null;
	}
	
	public String getSymbol()
	{
		return symbol_;
	}
	
	public Exchange getExchange()
	{
		return exchange_;
	}
	
	@Id
	@Column(name="SYMBOL",nullable=false)
	private String symbol_;
	
	@Id
	@Column(name="EXCHANGE",nullable=false)
	@Enumerated(EnumType.STRING)
	private Exchange exchange_;

}
