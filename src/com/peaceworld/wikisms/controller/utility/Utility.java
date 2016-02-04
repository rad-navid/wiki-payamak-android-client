package com.peaceworld.wikisms.controller.utility;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.peaceworld.wikisms.model.entity.SequenceHolder;
import com.peaceworld.wikisms.model.entity.dao.SequenceHolderDao;

public class Utility {
	
	public static final String USER_IDENTIFIER_KEY="USER_IDENTIFIER";
	
	public static String getInfo(Context context)
	{
		String serviceName = Context.TELEPHONY_SERVICE;
		TelephonyManager m_telephonyManager = (TelephonyManager) context.getSystemService(serviceName);
		String IMEI = m_telephonyManager.getDeviceId();
		String IMSI = m_telephonyManager.getSubscriberId();
		String SimOperator = m_telephonyManager.getSimOperator();
		String SimOperatorName = m_telephonyManager.getSimOperator();
		String countryCode = m_telephonyManager.getSimCountryIso();
		String androidOS = Build.VERSION.RELEASE;
		String androidSDK = Build.VERSION.SDK;
		String MANUFACTURER = Build.MANUFACTURER;
		String PRODUCT = Build.PRODUCT;
		
		
		
		return "IMEI:"+IMEI+", IMSI:"+IMSI+", SimOperator:"+SimOperator+" , SimOperatorName:"+SimOperatorName+
				" , countryCode:"+countryCode+" , androidOS:"+androidOS+" , androidSDK:"+androidSDK+
				" , MANUFACTURER:"+MANUFACTURER+" , PRODUCT:"+PRODUCT;
	}
	
	public static long getUserIdentifier(Context context)
	{
		 SharedPreferences sharedPreferences=context.getSharedPreferences(USER_IDENTIFIER_KEY, Context.MODE_PRIVATE);
		 long userIdentifier=sharedPreferences.getLong(USER_IDENTIFIER_KEY, -1);
		 if(userIdentifier>1000)
			 return userIdentifier;
		 
		 TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		 String  deviceId = telephonyManager.getDeviceId(); 
		 try{
			String partialId=deviceId.length()>15 ? deviceId.substring(0,15):deviceId;
			userIdentifier=Long.parseLong(partialId);
		 }catch(Exception ex)
		 { 
		 }

		 if(userIdentifier<1000)
			 userIdentifier=(long)(Math.random()*Math.pow(10, 15));
		 
		 Editor editor=sharedPreferences.edit();
		 editor.putLong(USER_IDENTIFIER_KEY, userIdentifier);
		 editor.commit();
		 
		 return userIdentifier;
		 
	}
	
	public static long generateUniqueId(long id,Context context)
	{
		long userIdentifier=getUserIdentifier(context);
		long a=(userIdentifier << 13)  &  0x7FFFFFFFFFFFE000L;
		long b=id & 0x1FFFL;
		long newId=a|b;
		return newId;
		
		
	}

	public static long generateUniqueIdForTable(String tableName,Context context)
	{
		SequenceHolderDao sequenceDao=new SequenceHolderDao(context);
		
		SequenceHolder example=new SequenceHolder();
		example.setTableName(tableName);
		SequenceHolder holder= sequenceDao.getByExample(example);
		long tmpId;
		if(holder==null)
		{
			example.setSequence(1);
			sequenceDao.insert(example);
			tmpId= 1;
		}
		else
		{
			long nextId=holder.getSequence()+1;
			holder.setSequence(nextId);
			sequenceDao.update(holder);
			tmpId= nextId;
		}
		long uniqueId=Utility.generateUniqueId(tmpId, context);
		return uniqueId;
	}
	
	public static double getSimilarity(String s1,String s2)
	{
		String big   = s1.length()>=s2.length() ? s1 :s2;
		String small = s1.length()< s2.length() ? s1 :s2;
		
		String[]list1=big.split("\\s+");
		List<String>arr1=(List<String>) Arrays.asList(list1);
		HashSet<String>bigerHashSet=new HashSet<String>(arr1);
		
		
		String[]list2=small.split("\\s+");
		int repeted=0;
		for(int i=0;i<list2.length;i++)
			if(bigerHashSet.contains(list2[i]))
				repeted++;
		
		return (repeted*1.0)/list2.length;
	}
	
	
	
	
}
