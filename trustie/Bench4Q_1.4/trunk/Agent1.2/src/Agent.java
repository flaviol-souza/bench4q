package src;

import java.io.*;
//import java.text.SimpleDateFormat;
import java.util.ArrayList;
//import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.jface.dialogs.DialogSettings;

import src.EBClosed;
import src.EB;
import src.storage.*;
import src.communication.Args;

public class Agent extends Thread {

	/*
	 * 测试的开始时间
	 */
	public long m_startTime;
	/*
	 * 测试的结束时间
	 */
	public long m_endTime;
	/*
	 * 测试时间
	 */
	public long m_stdyTime;
	/*
	 * 初次加载的EB数量
	 */
	public int m_baseLoad;
	/*
	 * 租户类型
	 */
	public String m_tenant;
	/*
	 * 准备时间
	 */
	public int m_prepairTime;
	/*
	 * 冷却时间
	 */
	public int m_cooldown;
	/*
	 * 书店应用的根地址
	 */
	public String m_baseURL;
	/*
	 * mix类型（shopping，browsing等）
	 */
	public String m_mix;
	/*
	 * 是否使用脚本发负载
	 */
	public boolean m_script;
	/*
	 * 脚本文件的路径
	 */
	public String m_scriptPath;
	
	/*
	 * 存放公用参数的args类的实例
	 */
	public Args m_args;
	/*
	 * 存放生成的EB
	 */
	private ArrayList<EB> ebs;
	
	/*
	 * 存放存储周期
	 */
	private long m_stoPERIOD=60000;
	/*
	 * 存放更新延迟
	 */
	private long m_stoDELAY=30000;
	/*
	 * think time
	 */
	private double m_thinkTime;
	/**
	 * 构造函数
	 * @param configpath 配置文件的路径
	 * @param scriptpath 脚本文件的路径
	 */
	public Agent(String configpath,String scriptpath) throws Exception
	{
		m_args=new Args();
		
		/*
		 * 读入配置文件内容进行初始化
		 */
		
		DialogSettings settings=new DialogSettings(null);
		try
		{
			settings.load(configpath);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		m_stdyTime=Long.valueOf(settings.get("stdyTime")).longValue();
		m_baseLoad=Integer.valueOf(settings.get("baseLoad")).intValue();
		m_tenant=settings.get("tenant");
		m_prepairTime = Integer.valueOf(settings.get("prepairTime")).intValue();
		m_cooldown = Integer.valueOf(settings.get("cooldown")).intValue();
		m_mix=settings.get("mix");
		m_stoPERIOD = Long.valueOf(settings.get("STOperiod")).longValue();
		m_stoDELAY = Long.valueOf(settings.get("STOdelay")).longValue();
		if(settings.get("isScript").equals("true"))
		{
			m_script=true;
		}
		else
		{
			m_script=false;
			m_baseURL=settings.get("baseURL");
			m_thinkTime=Double.valueOf(settings.get("thinkTime")).doubleValue();
		}
		m_scriptPath=scriptpath;
		
		Init();
	}
	
	public void Init() throws Exception
	{
		
		/*
		 * Init Args
		 */
		m_args.setMix(m_mix);
		m_args.setBaseURL(m_baseURL);
		m_args.setPrepair(m_prepairTime);
		m_args.setCooldown(m_cooldown);
		m_args.setTenant(m_tenant);
		m_args.setScript(m_script);
		m_args.setThinktime(m_thinkTime);
		
		if(m_args.isScript()){
			InitScript(m_scriptPath);
		}
		HttpClientFactory.setRetryCount(m_args.getRetry());
		
		/*
		 * Init EBs
		 */
		ArrayList<Integer> trace=new ArrayList<Integer>();
		
		ebs = new ArrayList<EB>();
		
		//m_baseLoad: define the number of thread
		for(int i=0;i<m_baseLoad;i++)
		{
			if(m_args.isScript()==true)
			{
				EBClosed eb=new EBClosed(m_args,m_scriptPath,i);
				eb.start();
				ebs.add(eb);
			}
			else
			{
				EBClosed eb=new EBClosed(m_args,trace);
				eb.start();
				ebs.add(eb);
			}
			
		}
	}
	/**
	 * 读脚本，把信息存入m_args
	 * @param scriptpath 脚本文件的路径
	 * @throws Exception 
	 */
	public void InitScript(String scriptpath) throws Exception
	{
		ArrayList<String> scriptURL=new ArrayList<String>();
		ArrayList<String> scriptPage=new ArrayList<String>();
		ArrayList<String> scriptParam=new ArrayList<String>();
		//scriptState: 下标表示当前请求在script中的序号，值表示该请求所属页面在script中的序号
		ArrayList<Integer> scriptState=new ArrayList<Integer>();
		ArrayList<String> scriptUser=new ArrayList<String>();
		ArrayList<String> scriptPass=new ArrayList<String>();
		ArrayList<String> scriptType=new ArrayList<String>();
		//added at 20110901, for recorded sleep time from script
		ArrayList<String> scriptSleep=new ArrayList<String>();
		
		File xmlFile = new File(scriptpath);   
        try {   
        	FileInputStream fis = null;   
            fis = new FileInputStream(xmlFile);   
            fis.close();
        } catch (FileNotFoundException e) {   
            e.printStackTrace();
            System.err.println("File is not exsit!");   
        }   
		SAXReader xmlReader = new SAXReader();
		xmlReader.setEncoding("GBK");
		try {
		    Document doc = xmlReader.read(xmlFile);
		    Element root = doc.getRootElement();
		    Element behaviors=root.element("behaviors");
		    List behaviorList = behaviors.elements("behavior" );
		    for (Iterator it_behavior = behaviorList.iterator(); it_behavior.hasNext();) {
		        Element behavior = (Element) it_behavior.next();
		        List sampleList = behavior.elements("sample" );
		        List sleepList = behavior.elements("timer");
		        //consider that the first request of script haven't duration time, add one here for consistency
		        scriptSleep.add("1");
			    for(Iterator it_sample = sampleList.iterator(),
			    		it_sleep = sleepList.iterator(); it_sample.hasNext();) {
			    	String parameter="";
			    	String username="";
			    	String password="";
			    	String url="";
			    	String sleepTime="";
			    	
			    	Element sample = (Element) it_sample.next();
			    	if(it_sample.hasNext()) {
			    		Element sleep = (Element) it_sleep.next();
			    		Element sParams = sleep.element("params");
			    		Element sParam = sParams.element("param");
			    		if(sParam.attribute("name").getValue().equals("duration_arg")) {
			    			Attribute param_value = sParam.attribute("value");
			    			sleepTime = param_value.getValue();
			    		} else {
			    			System.out.println("FATAL ERROR[101]: Script format error.");
			    			System.exit(-1);
			    		}
			    	}
			        Element params=sample.element("params");
			        List paramList = params.elements("param" );
			        for(Iterator it_param=paramList.iterator();it_param.hasNext();) {
			        	Element param = (Element) it_param.next();
			        	Attribute param_name=param.attribute("name" ); 
			        	if(param_name.getValue().equals("parameters"))
			        	{
			        		Attribute param_value=param.attribute("value" ); 
			        		parameter=param_value.getValue();
			        	}
			        	if(param_name.getValue().equals("redirect"))
			        	{
			        		Attribute param_value=param.attribute("value" ); 
			        		username=param_value.getValue();
			        	}
			        	if(param_name.getValue().equals("password"))
			        	{
			        		Attribute param_value=param.attribute("value" ); 
			        		password=param_value.getValue();
			        	}
			        	if(param_name.getValue().equals("uri")){
			        		Attribute param_value=param.attribute("value" ); 
			        		url=param_value.getValue();
			        		//若地址后缀是一下4种中的一种，则scriptType对应位置存"others",若是普通url地址,存"page"
			        		if(url.substring(url.length()-3).equalsIgnoreCase(".js")
			        				||url.substring(url.length()-4).equalsIgnoreCase(".css")
			        				||url.substring(url.length()-4).equalsIgnoreCase(".jpg")
			        				||url.substring(url.length()-4).equalsIgnoreCase(".gif")
			        				||url.substring(url.length()-4).equalsIgnoreCase(".png"))
			        		{
			        			scriptType.add("others");
			        		}			        			
			        		else
			        		{			        			
			        			scriptType.add("page");
			        			//temp: 若scriptPage里已经有了该url地址，则不添加
			        			if(!scriptPage.contains(url))
			        				scriptPage.add(url);
			        		}
			        		scriptURL.add(url);
			        		scriptParam.add(parameter);
					        scriptUser.add(username);
					        scriptPass.add(password);
					        scriptSleep.add(sleepTime);
			        		//System.out.println(url);
			        	}
			        }
			   }  
		    }
		}
		catch(Exception e){
		  	e.printStackTrace();
		  	throw e;
		}
		int prevstate=0;
		for(int i=0;i<scriptURL.size();i++)
		{
			if(scriptType.get(i).equals("page"))
			{
				int j=scriptPage.indexOf(scriptURL.get(i));
				scriptState.add(j);
				prevstate=j;
			}
			if(scriptType.get(i).equals("others"))
			{
				//所有.js, .jpg, .css 结尾请求，均标记为其所属页面的state，即prevstate.
				scriptState.add(prevstate);
			}
		}
		//根据scriptPage与scriptState初始化概率跳转模型
		m_args.getStatJump().init(scriptPage, scriptState);
		
		m_args.setScriptURL(scriptURL);
		m_args.setscriptParam(scriptParam);
		m_args.setscriptState(scriptState);
		m_args.setscriptUser(scriptUser);
		m_args.setscriptPass(scriptPass);
		m_args.setscriptType(scriptType);
		m_args.setscriptPage(scriptPage);
		m_args.setscriptSleep(scriptSleep);
	}
	public void InitStorage(){
		long startTime = System.currentTimeMillis();
		m_startTime=startTime;
		if(m_args.isScript()){
			int scriptSize = m_args.getscriptURL().size();
			int scriptSize_SS = m_args.getscriptPage().size();
			StorageThread.initSto(m_stoPERIOD,m_stoDELAY,scriptSize, scriptSize_SS, m_startTime, m_stdyTime,m_baseLoad);
		}else{
			StorageThread.initSto(m_stoPERIOD,m_stoDELAY,15, 15,m_startTime, m_stdyTime,m_baseLoad);
		}
	}
	public static void main(String[] args) throws Exception{
		//Agent agent=new Agent(args[0],args[1]);
		Agent agent=new Agent("config.xml","Script.xml");
		agent.InitStorage();
		StorageThread.getUniq().start();
		agent.start();
		System.out.println("started...");
	}
	
	public void run()
	{
		long beginTime = System.currentTimeMillis();
		long endTime = beginTime + m_stdyTime * 1000L;

		for (EB eb : ebs) {
			try {
				sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			((EBClosed) eb).setTest(true);
		}

		while ((System.currentTimeMillis() - endTime) < 0) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

		for (EB eb : ebs) {
			((EBClosed) eb).setTerminate(true);
			((EBClosed) eb).interrupt();
		}
		ebs = null;
	}
	

}

