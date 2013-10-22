package src;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.params.HttpMethodParams;

import src.communication.Args;
//import src.communication.EBStats;
import src.storage.StorageThread;
import src.trans.Transition;
import src.util.CharSetStrPattern;
import src.util.CharStrPattern;
import src.util.StrStrPattern;
import src.util.URLUtil;

/**
 * @author duanzhiquan
 * 
 */
public abstract class EB extends Thread {
	
	protected long sessionStart = 0;
	protected long sessionEnd = 0;
	protected int sessionLen = 1;
	protected boolean Ordered = false;
	
	public boolean isVIP;
	
	//debug0807
	protected int threadNum = -1;
	
	/**
	 * used to store harmful request number of the EB
	 */
	int overToleranceCount = 0;
	
	protected Args m_args;

	/**
	 * CUSTOMER_ID. See TPC-W Spec.
	 */
	public int cid;
	/**
	 *ESSION_ID. See TPC-W Spec.
	 */
	public String sessionID;
	/**
	 * Shopping ID.
	 */
	public int shopID;
	/**
	 * Customer first name.
	 */
	public String fname = null;
	/**
	 * Customer last name.
	 */
	public String lname = null;

	/**
	 * Transition probabilities.
	 */
	public int[][] transProb;
	/**
	 * EB transitions.
	 */
	public Transition[][] trans;
	/**
	 * 
	 */
	public Transition curTrans;
	/**
	 * Current state.
	 */
	public int curState;
	/**
	 *Next HTTP request.
	 */
	public String nextReq;
	/**
	 * Received HTML
	 */
	public String html;
	/**
	 * HTML from a previous page.
	 */
	public String prevHTML;
	/**
	 * Number of transitions. -1 implies continuous
	 */
	public int maxTrans;
	/**
	 * 
	 */
	public byte[] buffer = new byte[4096];

	/**
	 * 
	 */
	public boolean toHome;

	/**
	 * 
	 */
	public Random rand = new Random();

	/**
	 * Think time-scaling.
	 */
	public double tt_scale;	//think time

	/**
	 * Tolerance_scale.
	 */
	public double tolerance_scale;
	/**
	 * 
	 */
	public int retry;

	/**
	 * 
	 */
	public static final int NO_TRANS = 0;
	/**
	 * 
	 */
	public static final int MIN_PROB = 1;
	/**
	 * 
	 */
	public static final int MAX_PROB = 9999;
	/**
	 * 
	 */
	public static final int ID_UNKNOWN = -1;

	/**
	 * 
	 */
	public static String www;
	/**
	 * 
	 */
	public static String homeURL;
	/**
	 * 
	 */
	public static String shopCartURL;
	/**
	 * 
	 */
	public static String orderInqURL;
	/**
	 * 
	 */
	public static String orderDispURL;
	/**
	 * 
	 */
	public static String searchReqURL;
	/**
	 * 
	 */
	public static String searchResultURL;
	/**
	 * 
	 */
	public static String newProdURL;
	/**
	 * 
	 */
	public static String bestSellURL;
	/**
	 * 
	 */
	public static String prodDetURL;
	/**
	 * 
	 */
	public static String custRegURL;
	/**
	 * 
	 */
	public static String buyReqURL;
	/**
	 * 
	 */
	public static String buyConfURL;
	/**
	 * 
	 */
	public static String adminReqURL;
	/**
	 * 
	 */
	public static String adminConfURL;

	/**
	 * 
	 */
	public ArrayList<Integer> m_trace;
	
	public int state = 1;
	
	
	public double rate;
	Cookie[] cookies;
	public long start;
	public long end;
	public HttpClient m_Client;
	public boolean joke = false;

	/**
	 * 
	 */
	public Iterator it;

	/**
	 * 
	 */
	public final StrStrPattern imgPat = new StrStrPattern("<IMG");
	/**
	 * 
	 */
	public final StrStrPattern inputPat = new StrStrPattern(
			"<INPUT TYPE=\"IMAGE\"");
	/**
	 * 
	 */
	public final StrStrPattern srcPat = new StrStrPattern("SRC=\"");
	/**
	 * 
	 */
	public final CharStrPattern quotePat = new CharStrPattern('\"');
	
	public int scriptStep=0;
	public static int url=0;
	public static int parameter=1;
	public static int username=2;
	public static int password=3;
	public static int type=4;
	public String [] params;

	/**
	 * 使用url发负载，用于书店应用
	 * @param state 页面标志
	 * @param url
	 * @return boolean
	 */
	public boolean getHTML(int state, String url) {
		//System.out.println(url);
		double tolerance = tolerance(curState);
		html = "";
		int statusCode;

		GetMethod httpget = new GetMethod(url);
		httpget.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false)); 
		//httpget.setParams()
		//HttpMethodParam param
		httpget.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
		if (tolerance != 0) {
			httpget.getParams().setSoTimeout((int) (tolerance * 1000));
		}

		try {
			//start = System.currentTimeMillis();
			statusCode = m_Client.executeMethod(httpget);
			//end = System.currentTimeMillis();
			Cookie[] cc;
			cc = m_Client.getState().getCookies();
			for (int i = 0; i < cc.length; i++) {
				if (cc[i].getName().equalsIgnoreCase("jsessionid"))
					sessionID = cc[i].getValue();
			}
			
			Header[] header;
			if(sessionID == null){
				String cString = null;
				header = httpget.getRequestHeaders();
				for(int i = 0; i < header.length; i++){
					if(header[i].getName().equalsIgnoreCase("cookie"))
						cString = header[i].getValue();
				}
				int indexs = 0;
				if(cString != null){
				   indexs = cString.indexOf("JSESSIONID=");
				   indexs = indexs + "JSESSIONID=".length();
					String nextString = cString.substring(indexs);
					int indexe = 0;
					indexe = nextString.indexOf(";");
					sessionID = nextString.substring(0,indexe);
				}
			}
			if (statusCode != HttpStatus.SC_OK) {
				System.out.println("HTTP response ERROR: " + statusCode+" in "+url);
				return false;
			}
			long ttime = System.currentTimeMillis();
			/*InputStream resStream = httpget.getResponseBodyAsStream();  
	        BufferedReader br = new BufferedReader(new InputStreamReader(resStream));  
	        StringBuffer resBuffer = new StringBuffer();  
	        String resTemp = "";  
	        while((resTemp = br.readLine()) != null){  
	            resBuffer.append(resTemp);  
	        } 
	        int size = resBuffer.length();
	        String response = resBuffer.toString();*/
			BufferedReader bin = new BufferedReader(new InputStreamReader(
					httpget.getResponseBodyAsStream()));
			StringBuilder result = new StringBuilder();
			String s;
			while ((s = bin.readLine()) != null) {
				result.append(s);
			}
			html = new String(result);
	        
			ttime = System.currentTimeMillis() - ttime;
			//System.out.println("getResponse duration time:"+ttime+" size:"+size);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
		finally {
			// always release the connection after we're done
			httpget.releaseConnection();
		}

		if (!m_args.isGetImage()) {
			return true;
		}
		return true;
	}

	/**
	 * 使用脚本信息发负载
	 * @param state 页面标志
	 * @param params 存放脚本中读出的本次访问的url，parameter等信息
	 * @return
	 */
	public boolean getHTML(int state,String [] params) {
		int statusCode;
		//System.out.println("entered getHTML...");
		GetMethod httpget = new GetMethod(params[url]);
		//System.out.println("state:"+state+"url:"+params[url]);
		try
		{
			HttpClient Client=m_Client;
			httpget.setQueryString(getParameters(params[parameter]));
			if (params[username] != null && !params[username].equals(""))
			{
				Client.getState().setCredentials(
					new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM),
					new UsernamePasswordCredentials(params[username], params[password]));
			}
			httpget.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false)); 
			//start = System.currentTimeMillis();
			statusCode = Client.executeMethod(httpget);
//			if  (statusCode  !=  HttpStatus.SC_OK)
//				System.err.println( " Method failed:  "   +  httpget.getStatusLine());
			//end = System.currentTimeMillis();
			//Caution:此处没有接受server端返回的数据就返回了
			// consume the response entity
			long ttime = System.currentTimeMillis();
			InputStream resStream = httpget.getResponseBodyAsStream();  
	        BufferedReader br = new BufferedReader(new InputStreamReader(resStream));  
	        StringBuffer resBuffer = new StringBuffer();  
	        String resTemp = "";
	        while((resTemp = br.readLine()) != null){  
	            resBuffer.append(resTemp);
	        } 
	        int size = resBuffer.length();
	        String response = resBuffer.toString();
	        
			ttime = System.currentTimeMillis() - ttime;
			//System.out.println("getResponse duration time:"+ttime+" size:"+size);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		finally {
			httpget.releaseConnection();
		}
		return true;
	}
	/**
	 * 计算下一次要访问的页面url
	 * @return true: replay==true && it.hasNext==false;
	 * 
	 */
	public boolean nextState() {
		int i;
		if (!(m_args.isReplay())) {
			i = rand.nextInt(MAX_PROB - MIN_PROB + 1) + MIN_PROB;
			if (m_args.isRecord()) {
				m_trace.add(i);
			}
		} else {
			if (it.hasNext())
				i = (Integer) it.next();
			else
				return false;
		}
		if(m_args.isScript())
		{
			int tempStep=scriptStep%(m_args.getscriptURL().size());
				nextReq=m_args.getscriptURL().get(tempStep);
				params[url]=m_args.getscriptURL().get(tempStep);
				params[parameter]=m_args.getscriptParam().get(tempStep);
				params[username]=m_args.getscriptUser().get(tempStep);
				params[password]=m_args.getscriptPass().get(tempStep);
				params[type]=m_args.getscriptType().get(tempStep);
				curState=m_args.getscriptState().get(tempStep);
				//System.out.println(m_args.getscriptURL().get(scriptStep));
				//added by wuyulong on 20110826
				//if next request would be a page type, use statistical module
				if(m_args.getscriptType().get((tempStep+1)%(m_args.getscriptURL().size())).equals("page")) {
					scriptStep = m_args.getStatJump().getNextJump(scriptStep%(m_args.getscriptURL().size()));
				}
				else {
					scriptStep++;
				}
				//added end
				return true;
		}
		for (int j = 0; j < transProb[curState].length; j++) {
			if (transProb[curState][j] >= i) {
				curTrans = trans[curState][j];
				nextReq = curTrans.request(this, html);
				toHome = trans[curState][j].toHome();
				curState = j;
				return true;
			}
		}
		return false;
		
		
	}

	/**
	 * think time obey an Negative exponential distribution. TPC-W spec for
	 * Think Time (Clause 5.3.2.1)
	 * 
	 * @return think time.
	 */

	public long thinkTime() {
		double r = rand.nextDouble();
		long result;
		if (r < (4.54e-5)) {
			
			result = 70000L;
		} else {
			result = ((long) (-7000 * Math.log(r)));
		}
		return ((long) (result * tt_scale));

	}
	/**w
	 * @return tolerance time.
	 */
	public double tolerance(int cur) {

		int LONG = 80;
		int SHORT = 8;

		int[] tolerance = { SHORT, LONG, LONG, SHORT, LONG, LONG, LONG, SHORT,
				SHORT, LONG, LONG, SHORT, SHORT, SHORT, LONG };

		return tolerance_scale * tolerance[cur];

	}
	/**
	 * Adds CUSTOMER_ID and SHOPPING_ID fields to HTTP request, if they are
	 * known.
	 * 
	 * @param i
	 * @return addID URL
	 */
	public String addIDs(String i) {
		if (sessionID != null) {
			i = URLUtil.addSession(i, URLUtil.field_sessionID, "" + sessionID);
		}
		if (cid != ID_UNKNOWN) {
			i = URLUtil.addField(i, URLUtil.field_cid, "" + cid);
		}
		if (shopID != ID_UNKNOWN) {
			i = URLUtil.addField(i, URLUtil.field_shopID, "" + shopID);
		}
		return i;
	}

	/**
	 * @param html
	 * @param tag
	 * @return id
	 */
	public int findID(String html, StrStrPattern tag) {
		int id;
		// Find the tag string.
		int i = tag.find(html);
		if (i == -1) {
			return (EB.ID_UNKNOWN);
		}
		i = i + tag.length();

		// Find the digits following the tag string.
		int j = CharSetStrPattern.digit.find(html.substring(i));
		if (j == -1) {
			return (EB.ID_UNKNOWN);
		}

		// Find the end of the digits.
		j = j + i;
		int k = CharSetStrPattern.notDigit.find(html.substring(j));
		if (k == -1) {
			k = html.length();
		} else {
			k = k + j;
		}

		id = Integer.parseInt(html.substring(j, k));

		return id;
	}
	/**
	 * 根据脚本中读出的parameter字符串转化为查询对，要改分隔符！
	 * @param parameter
	 * @return
	 */
	public static NameValuePair[] getParameters(String parameter)
	{
		Set<NameValuePair> res = new HashSet<NameValuePair>();
		ArrayList<String> paramList=new ArrayList<String>();
		int i=parameter.indexOf(";");
		if(i>0)
		{
			while(true)
			{
				if(i>0)
				{
					String param=parameter.substring(0, parameter.indexOf(";"));
					paramList.add(param);
					parameter=parameter.substring(parameter.indexOf(";")+1);
					i=parameter.indexOf(";");
				}
				else
				{
					paramList.add(parameter);
					break;
				}
			}
		}
		Iterator<String> paramIter=paramList.iterator();
		while (paramIter.hasNext())
		{
			String token = paramIter.next();
			int index = token.indexOf('=');
			res.add(new NameValuePair(token.substring(0, index), token.substring(index+1)));
		}
		return res.toArray(new NameValuePair[res.size()]);
		
	}

}
