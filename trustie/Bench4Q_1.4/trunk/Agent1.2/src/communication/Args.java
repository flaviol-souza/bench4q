/**
 * 存放公用的参数
 */
package src.communication;

import java.util.ArrayList;

import src.statistic.StaticJumpAdapter;

/**
 * @author duanzhiquan
 * 
 */
public class Args implements Sendable {

	private static final long serialVersionUID = 5555852443194024065L;
	private String testName;
	private String testDescription;
	private String rbetype;//open or closed
	private double interval;
	private int prepair;
	private int cooldown;
	private String out;
	private String mix;
	private double slow;
	private boolean getImage;
	private String baseURL;//书店应用的根地址
	private String DBURL;
	private int WebPort;
	private int DBPort;
	private boolean UseEJB;
	private double p_s_to_l;
	private double p_l_to_s;
	private double lambda_short;
	private double lambda_long;
	private double rate;

	private boolean MoniWeb;
	private boolean MoniDB;


	private double tolerance;
	private int retry;
	private double thinktime;
	private boolean replay;
	private boolean record;
	private String time;
	
	/*
	 * 是否按脚本发负载
	 */
	private boolean script;
	

	private ArrayList<TestPhase> testPhase;
	
	//脚本的信息
	/*
	 * 链接
	 */
	private ArrayList<String> scriptURL;
	/*
	 * 被测端页面需要的参数
	 */
	private ArrayList<String> scriptParam;
	/*
	 * user name
	 */
	private ArrayList<String> scriptUser;
	/*
	 * password
	 */
	private ArrayList<String> scriptPass;
	/*
	 * 标志这个url属于哪个页面
	 */
	private ArrayList<Integer> scriptState;
	/*
	 * 页面类型，page表示页面本身，others代表页面附带的js脚本，图片等
	 */
	private ArrayList<String> scriptType;
	/*
	 * 页面的链表
	 */
	private ArrayList<String> scriptPage;
	/*
	 * 每个页面的thinkTime(延迟访问时间)
	 */
	private ArrayList<String> scriptSleep;
	
	/*
	 * 租户名，要写到cookie中去
	 */
	private String tenant;
	
	/*
	 * 按照脚本初始化的概率跳转模型
	 */
	private StaticJumpAdapter statJump;
	
	public StaticJumpAdapter getStatJump() {
		return statJump;
	}


	/**
	 * constructor
	 */
	public Args() {
		testPhase = new ArrayList<TestPhase>();
		// initiate the argument
		rbetype = "closed";
		interval = 1;
		prepair = 0;
		cooldown = 300;
		out = "out";
		mix = "shopping";
		slow = 1.0;
		getImage = false;
		tolerance = 1.0;
		thinktime = 1.0;
		baseURL = "http://localhost:8080";
		
		script=false;
		statJump = new StaticJumpAdapter();
	}

	/**
	 * @return rbe type
	 */
	public String getRbetype() {
		return rbetype;
	}

	/**
	 * @param rbetype
	 */
	public void setRbetype(String rbetype) {
		this.rbetype = rbetype;
	}

	/**
	 * @return prepair
	 */
	public int getPrepair() {
		return prepair;
	}

	/**
	 * @param prepair
	 */
	public void setPrepair(int prepair) {
		this.prepair = prepair;
	}

	/**
	 * @return cooldown
	 */
	public int getCooldown() {
		return cooldown;
	}

	/**
	 * @param cooldown
	 */
	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	/**
	 * @return out name
	 */
	public String getOut() {
		return out;
	}

	/**
	 * @param out
	 */
	public void setOut(String out) {
		this.out = out;
	}

	/**
	 * @return mix
	 */
	public String getMix() {
		return mix;
	}

	/**
	 * @param mix
	 */
	public void setMix(String mix) {
		this.mix = mix;
	}

	/**
	 * @return slow rate
	 */
	public double getSlow() {
		return slow;
	}

	/**
	 * @param slow
	 */
	public void setSlow(double slow) {
		this.slow = slow;
	}

	/**
	 * @return base URL
	 */
	public String getBaseURL() {
		return baseURL;
	}

	/**
	 * @param baseURL
	 */
	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

	/**
	 * @return tolerance time
	 */
	public double getTolerance() {
		return tolerance;
	}

	/**
	 * @param tolerance
	 */
	public void setTolerance(double tolerance) {
		this.tolerance = tolerance;
	}

	/**
	 * @return think time
	 */
	public double getThinktime() {
		return thinktime;
	}

	/**
	 * @param thinktime
	 */
	public void setThinktime(double thinktime) {
		this.thinktime = thinktime;
	}

	/**
	 * @return whether get Image
	 */
	public boolean isGetImage() {
		return getImage;
	}

	/**
	 * @param getImage
	 */
	public void setGetImage(boolean getImage) {
		this.getImage = getImage;
	}

	/**
	 * @return retry time
	 */
	public int getRetry() {
		return retry;
	}

	/**
	 * @param retry
	 */
	public void setRetry(int retry) {
		this.retry = retry;
	}

	/**
	 * @return testPhase
	 */
	public ArrayList<TestPhase> getEbs() {
		return testPhase;
	}

	/**
	 * @param testPhase
	 */
	public void setEbs(ArrayList<TestPhase> testPhase) {
		this.testPhase = testPhase;
	}

	/**
	 * @param testPhase
	 */
	public void addEB(TestPhase testPhase) {
		this.testPhase.add(testPhase);
	}

	/**
	 * @param index
	 */
	public void deleteEB(int index) {
		this.testPhase.remove(index);
	}

	/**
	 * @return interval
	 */
	public double getInterval() {
		return interval;
	}

	/**
	 * @param interval
	 */
	public void setInterval(double interval) {
		this.interval = interval;
	}

	/**
	 * @return test name
	 */
	public String getTestName() {
		return testName;
	}

	/**
	 * @param testName
	 */
	public void setTestName(String testName) {
		this.testName = testName;
	}

	/**
	 * @return test description
	 */
	public String getTestDescription() {
		return testDescription;
	}

	/**
	 * @param testDescription
	 */
	public void setTestDescription(String testDescription) {
		this.testDescription = testDescription;
	}
	
	/**
	 * @return
	 */
	public boolean isReplay() {
		return replay;
        }
	public String getDBURL() {
		return DBURL;
	}

	/**
	 * @param replay
	 */
	public void setReplay(boolean replay) {
		this.replay = replay;
        }
	public void setDBURL(String dBURL) {
		DBURL = dBURL;
	}
	/**
	 * @return
	 */
	public int getWebPort() {
		return WebPort;
	}

	/**
	 * @param webPort
	 */
	public void setWebPort(int webPort) {
		WebPort = webPort;
	}

	/**
	 * @return
	 */
	public int getDBPort() {
		return DBPort;
	}

	/**
	 * @param dBPort
	 */
	public void setDBPort(int dBPort) {
		DBPort = dBPort;
	}
	
	/**
	 * @return
	 */
	public boolean isUseEJB() {
		return UseEJB;
	}

	/**
	 * @param useEJB
	 */
	public void setUseEJB(boolean useEJB) {
		UseEJB = useEJB;
	}
	
	/**
	 * @return
	 */
	public boolean isMoniWeb() {
		return MoniWeb;
	}

	/**
	 * @param moniWeb
	 */
	public void setMoniWeb(boolean moniWeb) {
		MoniWeb = moniWeb;
	}

	/**
	 * @return
	 */
	public boolean isMoniDB() {
		return MoniDB;
	}

	/**
	 * @param moniDB
	 */
	public void setMoniDB(boolean moniDB) {
		MoniDB = moniDB;
	}

	/**
	 * @return
	 */
	public boolean isRecord() {
		return record;
	}

	/**
	 * @param record
	 */
	public void setRecord(boolean record) {
		this.record = record;
	}
	
	
	/**
	 * @return
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param time
	 */
	public void setTime(String time) {
		this.time = time;
	}
	
	public double getP_s_to_l() {
		return p_s_to_l;
	}

	public void setP_s_to_l(double pSToL) {
		p_s_to_l = pSToL;
	}

	public double getP_l_to_s() {
		return p_l_to_s;
	}

	public void setP_l_to_s(double pLToS) {
		p_l_to_s = pLToS;
	}
	
	public double getLambda_short() {
		return lambda_short;
	}

	public void setLambda_short(double lambdaShort) {
		lambda_short = lambdaShort;
	}

	public double getLambda_long() {
		return lambda_long;
	}

	public void setLambda_long(double lambdaLong) {
		lambda_long = lambdaLong;
	}
	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public boolean isScript() {
		return script;
	}

	public void setScript(boolean script) {
		this.script = script;
	}
	
	public ArrayList<String> getscriptURL() {
		return scriptURL;
	}

	public void setScriptURL(ArrayList<String> scriptURL) {
		this.scriptURL = scriptURL;
	}
	
	public ArrayList<String> getscriptParam() {
		return scriptParam;
	}

	public void setscriptParam(ArrayList<String> scriptParam) {
		this.scriptParam = scriptParam;
	}
	
	public ArrayList<String> getscriptUser() {
		return scriptUser;
	}

	public void setscriptUser(ArrayList<String> scriptUser) {
		this.scriptUser = scriptUser;
	}
	public ArrayList<String> getscriptPass() {
		return scriptPass;
	}

	public void setscriptPass(ArrayList<String> scriptPass) {
		this.scriptPass = scriptPass;
	}

	public ArrayList<Integer> getscriptState() {
		return scriptState;
	}

	public void setscriptState(ArrayList<Integer> scriptState) {
		this.scriptState = scriptState;
	}
	
	public ArrayList<String> getscriptType() {
		return scriptType;
	}

	public void setscriptType(ArrayList<String> scriptType) {
		this.scriptType = scriptType;
	}
	
	public ArrayList<String> getscriptPage() {
		return scriptPage;
	}

	public void setscriptPage(ArrayList<String> scriptPage) {
		this.scriptPage = scriptPage;
	}
	
	public void setscriptSleep(ArrayList<String> scriptSleep) {
		this.scriptSleep = scriptSleep;
	}
	
	public ArrayList<String> getscriptSleep() {
		return scriptSleep;
	}
	
	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}
}
