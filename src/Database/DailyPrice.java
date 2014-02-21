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

@Entity(name="daily_price")
@SequenceGenerator(name="data_vendor_id_seq", sequenceName="data_vendor_id_seq", allocationSize=1)
public class DailyPrice implements Serializable
{

   public DailyPrice(Timestamp priceDate, 
		   			 Timestamp createdDate,
		   			 Timestamp lastUpdatedDate)
   {
	   priceDate_ = priceDate;
	   createdDate_ = createdDate;
	   lastUpdatedDate_ = lastUpdatedDate;
   }
   
   public DailyPrice()
   {
	   
   }
   
   @Id
   @Column(name="id")
   @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="data_vendor_id_seq")
   private int id_;
   
   @Column(name="price_date")
   private Timestamp priceDate_;
   
   @Column(name="created_date", nullable=false)
   private Timestamp createdDate_;
	
   @Column(name="last_updated_date", nullable=false)
   private Timestamp lastUpdatedDate_;
   
   @Column(name="open_price", nullable=true)
   private float openPrice_;
   
   @Column(name="high_price", nullable=true)
   private float highPrice_;
   
   @Column(name="low_price", nullable=true)
   private float lowPrice_;
   
   @Column(name="close_price", nullable=true)
   private float closePrice_;
   
   @Column(name="adj_close_price", nullable=true)
   private float adjClosePrice_;
   
   @Column(name="volume", nullable=true)
   private long volume_;
   
   @ManyToOne
   @JoinColumn(name="data_vendor_id", referencedColumnName="id")
   private DataVendor dataVendor_;
   
   @ManyToOne
   @JoinColumn(name="symbol_id", referencedColumnName="id")
   private Symbol symbol_;
   
   
   public void setOpen(float openPrice)
   {
	   openPrice_=openPrice;
   }
   
   public void setHigh(float highPrice)
   {
	   highPrice_=highPrice;
   }
   
   public void setLow(float lowPrice)
   {
	   lowPrice_=lowPrice;
   }
   
   public void setClose(float closePrice)
   {
	   closePrice_=closePrice;
   }
   
   public void setAdjClose(float adjClose)
   {
	   adjClosePrice_=adjClose;
   }
   
   public void setVolume(long volume)
   {
	   volume_=volume;
   }
   
   public void setVendor(DataVendor vendor)
   {
	   dataVendor_=vendor;
   }
   
   public void setSymbol(Symbol symbol)
   {
	   symbol_=symbol;
   }
   
   public float getLow()
   {
	   return lowPrice_;
   }
   
   public float getHigh()
   {
	   return highPrice_;
   }
   
   public float getOpen()
   {
	   return openPrice_;
   }
   
   public float getClose()
   {
	   return  closePrice_;
   }
   
}
