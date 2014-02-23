package Util;

import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.TimeZone;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import Client.Client;
import Database.DailyPrice;
import Database.DataVendor;
import Database.DatabaseCommonQuery;
import Database.Exchange;
import Database.Symbol;
import Dispatcher.DispatcherInterface.Job;

public class Test 
{
 
  public static void main(String[] args) throws IOException 
  {
	  Client client = new Client();
	  
	  String[] re = new String[1];
	  re[0] = "gabriel.laprise@outlook.com";
	  
	  client.sendReport(re);
  }
} 
