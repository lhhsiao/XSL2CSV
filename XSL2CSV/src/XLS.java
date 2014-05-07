import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class XLS {
	static final int unknow = 0;

	static final int NAT_GEO_MUSIC = 1;

	static final int NGC_HD = 2;

	static final int NEWB = 3;

	static final int FOX_NEWS = 4;

	static final int STGD = 5;
	
	static final int SMIT_HD =6;
	
	static final int SCM_HD =7;
	
	static final int FX_HD=8;
	
	static final int SMIT_HD_2 =9;
	
	static final int SMIT_SD =10;
	
	static final int SCM =11;
	
	static final int SW =12;
	
	static final int V =13;
	
	static final int SCC =14;
	
	static final int NGC =15;
	
	static final int NGC_C =16;
	
	static final int STAR_EC =17;
	
	static final int FOX_HD =18;

	int format = unknow;


	int HeaderRowNumber = -1;
	String filename="";
	String[][][] content = new String[0][0][0];

	String[] headerKeysForNAT_GEO_MUSIC = { "Start Date", "Start Time",
			"Program Title" };

	String[] headerKeysForNGC_HD = { "Schedule Date", "Schedule Start Time",
			"Slot Duration", "EPG Title(Chinese) Max 16 Character",
			"Program Title(Chinese)" };

	String[] headerKeysForNEWB = { "schedule date (yyyy-mm-dd)",
			"start time (hh:mm:ss)", "slot name", "programme name" };

	String[] headerKeysForFoxNews = { "Date", "Time", "Programme" };

	String[] headerKeysForSTGD = { "HK/SIN", "Movie Duration", "Movie Name",
			"Ep no" };
	
	String[] headerKeysSMITHD={"DATE","TIME", "CHINESE TITLE"};
	
	String[] headerKeysSMITHD_2={"DATE","TIME", "CHINESE"};
	
	String[] headerKeysSMITHD_3={"DATE","START", "CHINESE TITLE"};
	
	String[] headerKeysSCMHD={"STAR CHINESE MOVIES (HD)"};
	
	String[] headerKeysSCM={"STAR CHINESE MOVIES"};
	
	String[] headerKeysForFX_HD = { "Schedule Date", "Schedule Start Time",
			"Slot Duration", "Series Name (Chinese)",
			"Episode Title (Chinese)" };
	
	String[] headerKeysForSW={"Star World"};
	
	String[] headerKeysForV={"．Schedule subject to change"};
	
	String[] headerKeysForNGC={"國家地理頻道"};
	
	String[] headerKeysForNGC_C={"National Geographic Taiwan"};
	
	String[] headerKeysForSCC={"< STAR CHINESE CHANNEL(TAIWAN) >"};
	
	String[] headerKeysForSTAREC={"STAR Entertainment Channel"};

	String[] headerKeysForFOXHD={"節目表"};
	
	boolean[] foundItems = new boolean[headerKeysForNGC_HD.length];

	int[] keyHeaderColumnNumber = new int[headerKeysForNGC_HD.length];

	// void checkFormatKeyWord() {
	// for (int i = 0; i < content.length; i++) {
	// for (int j = 0; j < content[i].length; j++) {
	// if (content[i][j].startsWith("National Geographic Channel HD")) {
	// format = NGC_HD;
	// System.out
	// .println("Found A Excel File for National Geographic Channel HD.");
	// return;
	// } else if (content[i][j].startsWith("Nat Geo Music")) {
	// format = NAT_GEO_MUSIC;
	// System.out.println("Found A Excel File for NAT GEO MUSIC.");
	// return;
	// }
	// }
	//
	// }
	//
	// }
	private List sheetNames = new ArrayList();

	void setSheetNames(List l) {
		sheetNames = l;
	}

	void checkFormatByHeaderKeys(String[][] content) {
		String[] headerKeys = headerKeysForNAT_GEO_MUSIC;
		if ((HeaderRowNumber = findHeaderLine(content, headerKeys, "NAT Style")) > -1) {
			format = NAT_GEO_MUSIC;
			return;
		}

		headerKeys = headerKeysForNGC_HD;
		if ((HeaderRowNumber = findHeaderLine(content, headerKeys, "NGC Style")) > -1) {
			format = NGC_HD;
			return;
		}

		headerKeys = headerKeysForNEWB;
		if ((HeaderRowNumber = findHeaderLine(content, headerKeys, "NEWB Style")) > -1) {
			format = NEWB;
			return;
		}

		headerKeys = headerKeysForFoxNews;
		if ((HeaderRowNumber = findHeaderLine(content, headerKeys,
				"Fox News Style")) > -1) {
			format = FOX_NEWS;
			return;
		}

		headerKeys = headerKeysForSTGD;
		if ((HeaderRowNumber = findHeaderLine(content, headerKeys,
				"STGD Style")) > -1) {
			format = STGD;
			return;
		}
		
		headerKeys = headerKeysSMITHD;
		if ((HeaderRowNumber = findHeaderLine(content, headerKeys,
				"SMIT HD Style")) > -1) {
			format = SMIT_HD;
			return;
		}
		headerKeys = headerKeysSMITHD_2;
		if ((HeaderRowNumber = findHeaderLine(content, headerKeys,
				"SMIT HD 2 Style")) > -1) {
			format = SMIT_HD_2;
			return;
		}
		headerKeys = headerKeysSMITHD_3;
		if ((HeaderRowNumber = findHeaderLine(content, headerKeys,
				"SMIT SD Style")) > -1) {
			format = SMIT_SD;
			return;
		}
		headerKeys = headerKeysSCMHD;
		if ((HeaderRowNumber = findHeaderLine(content, headerKeys,
				"SCM HD Style")) > -1) {
			format = SCM_HD;
			return;
		}//headerKeysSCMHD
		headerKeys = headerKeysSCM;
		if ((HeaderRowNumber = findHeaderLine(content, headerKeys,
				"SCM  Style")) > -1) {
			format = SCM;
			return;
		}//headerKeysSCM
		headerKeys = headerKeysForFX_HD;
		if ((HeaderRowNumber = findHeaderLine(content, headerKeys,
				"FX HD Style")) > -1) {
			format = FX_HD;
			return;
		}//headerKeysSCM
		headerKeys = headerKeysForSW;
		if ((HeaderRowNumber = findHeaderLine(content, headerKeys,
				"Star World Style")) > -1) {
			format = SW;
			return;
		}
		headerKeys = headerKeysForV;
		if ((HeaderRowNumber = findHeaderLinebyContaining(content, headerKeys,
		"V Style")) > -1) {
			format = V;
			return;
		}
		headerKeys = headerKeysForSCC;
		if ((HeaderRowNumber = findHeaderLine(content, headerKeys,
		"SCC Style")) > -1) {
			format = SCC;
			return;
		}
		headerKeys = headerKeysForNGC;
		if ((HeaderRowNumber = findHeaderLine(content, headerKeys,
		"NGC Style")) > -1) {
			format = NGC;
			return;
		}
		headerKeys = headerKeysForNGC_C;
		if ((HeaderRowNumber = findHeaderLine(content, headerKeys,
		"NGC Style")) > -1) {
			format = NGC_C;
			return;
		}
		headerKeys = headerKeysForSTAREC;
		if ((HeaderRowNumber = findHeaderLine(content, headerKeys,
		"Star E C Style")) > -1) {
			format = STAR_EC;
			return;
		}
		headerKeys = headerKeysForFOXHD;
		if ((HeaderRowNumber = findHeaderLine(content, headerKeys,
		"FOX HD Style")) > -1) {
			
			format = FOX_HD;
			return;
		}
		format = 0;

	}

	private void setAllBoolean(boolean[] b, boolean value) {
		for (int i = 0; i < b.length; i++) {
			b[i] = value;
		}
	}

	private boolean checkAllBoolean(boolean[] b) {
		boolean result = true;
		for (int i = 0; i < b.length; i++) {
			result &= b[i];
		}
		return result;
	}
	private int findHeaderLinebyContaining(String[][] content, String[] headerKeys,
			String sytleName) {

		foundItems = new boolean[headerKeys.length];
		keyHeaderColumnNumber = new int[headerKeys.length];
		setAllBoolean(foundItems, false);
		int HeaderRowNumber = -1;
		for (int i = 0; i < content.length; i++) {
			if(content[i]!=null)
			for (int j = 0; j < content[i].length; j++) {
				for (int k = 0; k < headerKeys.length; k++) {
					if (content[i][j] != null){
//						System.out.println(content[i][j]);
						if (content[i][j].toUpperCase().indexOf(headerKeys[k].toUpperCase())>-1) {
							keyHeaderColumnNumber[k] = j;
							foundItems[k] = true;
							System.out.println("Found key" + k + ": "
									+ headerKeys[k]);
						}
					}
				}
			}
			if (checkAllBoolean(foundItems)) {
				HeaderRowNumber = i;
				System.out.println("Found header line for " + sytleName + ": "
						+ HeaderRowNumber);
				break;
			} else {
				setAllBoolean(foundItems, false);
			}

		}
		if (HeaderRowNumber < 0) {
			System.out.println("Can not find a header line for " + sytleName
					+ ".");
			return -1;
		}
		return HeaderRowNumber;
	}
	private int findHeaderLine(String[][] content, String[] headerKeys,
			String sytleName) {

		foundItems = new boolean[headerKeys.length];
		keyHeaderColumnNumber = new int[headerKeys.length];
		setAllBoolean(foundItems, false);
		int HeaderRowNumber = -1;
		for (int i = 0; i < content.length; i++) {
			if(content[i]!=null)
			for (int j = 0; j < content[i].length; j++) {
				for (int k = 0; k < headerKeys.length; k++) {
					if (content[i][j] != null){
//						System.out.println(content[i][j]);
						if (content[i][j].toUpperCase().trim().startsWith(headerKeys[k].toUpperCase())) {
							keyHeaderColumnNumber[k] = j;
							foundItems[k] = true;
							System.out.println("Found key" + k + ": "
									+ headerKeys[k]);
						}
					}
				}
			}
			if (checkAllBoolean(foundItems)) {
				HeaderRowNumber = i;
				System.out.println("Found header line for " + sytleName + ": "
						+ HeaderRowNumber);
				break;
			} else {
				setAllBoolean(foundItems, false);
			}

		}
		if (HeaderRowNumber < 0) {
			System.out.println("Can not find a header line for " + sytleName
					+ ".");
			return -1;
		}
		return HeaderRowNumber;
	}
	
	public List parseSCCFormat(String[][][] contents) {
//		 ===========read events from xls=======================
		List txtEventList = new ArrayList();
		StarChineseChannelCalanderReader test = new StarChineseChannelCalanderReader();

		test.setServiceID("SCC");
		txtEventList=test.parseEvents(contents);
		return txtEventList;
	}
	public List parseNGCCFormat(String[][][] contents) {
//		 ===========read events from xls=======================
		List txtEventList = new ArrayList();
		NGCCalanderReader test = new NGCCalanderReader();

		test.setServiceID("NGC");
		txtEventList=test.parseEvents(contents);
		return txtEventList;
	}
	public List parseSTARECFormat(String[][][] contents) {
//		 ===========read events from xls=======================
		List txtEventList = new ArrayList();
//		STARECCalanderReader2 test = new STARECCalanderReader2();
//		test.setServiceID("SGE");
//		test.setInputFile(this.getFilename());
//		txtEventList=test.parseEvents();

		STARECCalanderReader test = new STARECCalanderReader();
		test.setInputFile(this.getFilename());
		test.setServiceID("SGE");
		txtEventList=test.parseEvents(contents);
		return txtEventList;
	}
	public List parseFOXHDFormat(String[][][] contents) {
//		 ===========read events from xls=======================
		List txtEventList = new ArrayList();
//		STARECCalanderReader2 test = new STARECCalanderReader2();
//		test.setServiceID("SGE");
//		test.setInputFile(this.getFilename());
//		txtEventList=test.parseEvents();

		FoxHDCalanderReader2 test = new FoxHDCalanderReader2();
		test.setInputFile(this.getFilename());
		test.setServiceID("FoxHD");
		txtEventList=test.parseEvents();
		return txtEventList;
	}
	public List parseSWFormat() {
		// ===========read events from xls=======================
		List txtEventList = new ArrayList();
		StarWorldCalanderReader test = new StarWorldCalanderReader();
		test.setServiceID("SW TW");
		test.setInputFile(this.getFilename());
		txtEventList=test.parseEvents();
		return txtEventList;
	}
	public List parseSWFormat(String[][][] contents) {
		// ===========read events from xls=======================
		List txtEventList = new ArrayList();
		StarWorldCalanderReader2 test = new StarWorldCalanderReader2();
		test.setServiceID("SW TW");
		test.setInputFile(this.getFilename());
		txtEventList=test.parseEvents(contents);
		return txtEventList;
	}
	public List parseVFormat() {
		// ===========read events from xls=======================
		List txtEventList = new ArrayList();
		VCalanderReader test = new VCalanderReader();
		test.setServiceID("V TW");
		test.setInputFile(this.getFilename());
		txtEventList=test.parseEvents();
		return txtEventList;
	}
	public List parseNGCFormat() {
		// ===========read events from xls=======================
		List txtEventList = new ArrayList();
		NGCDocxReader test = new NGCDocxReader();
		test.setServiceId("NGC");
		txtEventList=test.parseEvents(getFilename());
		return txtEventList;
	}
	public List parseSCMHDFormat(int format) {
		// ===========read events from xls=======================
		List txtEventList = new ArrayList();
		StartMovieCalanderReader test = new StartMovieCalanderReader();
		test.setTimeCellShift(format==SCM?5:3);
		test.setServiceID(format==SCM?"SCM":"SCM HD");
		test.setInputFile(this.getFilename());
		txtEventList=test.parseEvents();
		return txtEventList;
	}
	public List parseSCMHDFormat2(String[][] contents,int format) {
		// ===========read events from xls=======================
		List txtEventList = new ArrayList();
		StartMovieCalanderReader2 test = new StartMovieCalanderReader2();
		test.setTimeCellShift(format==SCM?5:3);
		test.setServiceID(format==SCM?"SCM":"SCM HD");
		txtEventList=test.parseEvents(contents);
		return txtEventList;
	}
	public List parseSMITHDFormat(String[][] content) {
		// ===========read events from xls=======================
		List txtEventList = new ArrayList();

		if (HeaderRowNumber > -1) {
			for (int i = HeaderRowNumber + 1; i < content.length; i++) {
				boolean lossKeyValue = false;
				for (int j = 0; j < keyHeaderColumnNumber.length; j++) {
					if (content[i][keyHeaderColumnNumber[j]] == null) {
						System.out.println("Key Value can not be found at " + i
								+ " " + j);
						lossKeyValue = true;
						break;
					}
				}
				if (lossKeyValue == false) {
					TXTEvent tempEvent = new TXTEvent();
					tempEvent.setName(content[i][keyHeaderColumnNumber[2]]);
//							+ ": " + content[i][keyHeaderColumnNumber[3]]);//

					content[i][keyHeaderColumnNumber[0]] = replaceMonth2Numbers(content[i][keyHeaderColumnNumber[0]]);
//					if (i > 0)
//						if (content[i][keyHeaderColumnNumber[1]]
//								.startsWith("12:"))
//							content[i][keyHeaderColumnNumber[1]] = "00:"+content[i][keyHeaderColumnNumber[1]].substring(content[i][keyHeaderColumnNumber[1]].indexOf(":")+1);
						
					SimpleDateFormat sdf_src = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm");
					try {
//						String fn=getFilename();
//						String year=fn.substring(fn.indexOf("2"),fn.indexOf("2")+4);
						Date d_src = sdf_src
								.parse((content[i][keyHeaderColumnNumber[0]])
										+ " "
										+ content[i][keyHeaderColumnNumber[1]]);
						;
						tempEvent.setStartDate(tempEvent.sdf.format(d_src));
						// System.out.println(sdf.format(d_src));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						continue;
					
					}
					txtEventList.add(tempEvent);
					// SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd
					// HH:mm:ss.0");
					// System.out.println(sdf.format(new Date()));
					// tempEvent.setStartDate(startDate);
				}
			}
		}
		return txtEventList;
	}
	
	public List parseSMITHD_2Format(String[][] content) {
		// ===========read events from xls=======================
		List txtEventList = new ArrayList();
		String[] year_head={"","2012"};
		String year="2012";
		for(int i=0;i<HeaderRowNumber;i++){
			for(int j=0;j<content[i].length;j++){
				if(content[i][j]!=null){
					
					 year_head=content[i][j].replaceAll("]","").split(" ");
					if(year_head.length>1){
						if(year_head[1].startsWith("20")){
							year=year_head[1];
							i=HeaderRowNumber;
							System.out.println("found year: "+year);
							break;
						}
				}
				}
			}
		}
		
		if (HeaderRowNumber > -1) {
			for (int i = HeaderRowNumber + 1; i < content.length; i++) {
				boolean lossKeyValue = false;
				for (int j = 0; j < keyHeaderColumnNumber.length; j++) {
					System.out.println("i: "+i +" ,j"+j);
					if(content[i]!=null)
						if(keyHeaderColumnNumber[j]<content[i].length){
							if (content[i][keyHeaderColumnNumber[j]] == null || content[i][keyHeaderColumnNumber[j]].length()==0 ) {
								System.out.println("Key Value can not be found at " + i
										+ " " + j);
								lossKeyValue = true;
								break;
						}
					}else{
						System.out.println("Key Value can not be found at " + i
								+ " " + j);
						lossKeyValue = true;
						break;
					}
				}
				if (lossKeyValue == false) {
					TXTEvent tempEvent = new TXTEvent();
					tempEvent.setName(content[i][keyHeaderColumnNumber[2]]);
//							+ ": " + content[i][keyHeaderColumnNumber[3]]);//

					content[i][keyHeaderColumnNumber[0]] = replaceMonth2Numbers(content[i][keyHeaderColumnNumber[0]]);
//					if (i > 0)
//						if (content[i][keyHeaderColumnNumber[1]]
//								.startsWith("12:"))
//							content[i][keyHeaderColumnNumber[1]] = "00:"+content[i][keyHeaderColumnNumber[1]].substring(content[i][keyHeaderColumnNumber[1]].indexOf(":")+1);
						
					SimpleDateFormat sdf_src = new SimpleDateFormat(", MM, dd yyyy HH:mm");
					try {
//						String fn=getFilename();
//						String year=fn.substring(fn.indexOf("2"),fn.indexOf("2")+4);
						Date d_src = sdf_src
								.parse(content[i][keyHeaderColumnNumber[0]].substring(content[i][keyHeaderColumnNumber[0]].indexOf(","))
										+ " "+year+" "
										+ content[i][keyHeaderColumnNumber[1]]);
						;
						tempEvent.setStartDate(tempEvent.sdf.format(d_src));
						// System.out.println(sdf.format(d_src));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						continue;
					
					}
					tempEvent.setServiceID("SMIT HD");
					txtEventList.add(tempEvent);
					// SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd
					// HH:mm:ss.0");
					// System.out.println(sdf.format(new Date()));
					// tempEvent.setStartDate(startDate);
				}
			}
		}
		return txtEventList;
	}
	public List parseSMITSDFormat(String[][] content) {
		// ===========read events from xls=======================
		List txtEventList = new ArrayList();
		String[] year_head={"","2012"};
		String year="2012";
		for(int i=0;i<HeaderRowNumber;i++){
			for(int j=0;j<content[i].length;j++){
				if(content[i][j]!=null){
//					System.out.println("checking: "+i+" "+j+" "+content[i][j]);
					 year_head=content[i][j].replaceAll(" ","").split("-");
					if(year_head.length>1){
						if(year_head[1].length()==2){
							year="20"+year_head[1];
							i=HeaderRowNumber;
							System.out.println("found year: "+year);
							break;
						}
				}
				}
			}
		}
		
		if (HeaderRowNumber > -1) {
			for (int i = HeaderRowNumber + 1; i < content.length; i++) {
				boolean lossKeyValue = false;
				for (int j = 0; j < keyHeaderColumnNumber.length; j++) {
					if (content[i][keyHeaderColumnNumber[j]] == null) {
						System.out.println("Key Value can not be found at " + i
								+ " " + j);
						lossKeyValue = true;
						break;
					}
				}
				if (lossKeyValue == false) {
					TXTEvent tempEvent = new TXTEvent();
					tempEvent.setName(content[i][keyHeaderColumnNumber[2]]);
//							+ ": " + content[i][keyHeaderColumnNumber[3]]);//

					content[i][keyHeaderColumnNumber[0]] = replaceMonth2Numbers(content[i][keyHeaderColumnNumber[0]]);
//					if (i > 0)
//						if (content[i][keyHeaderColumnNumber[1]]
//								.startsWith("12:"))
//							content[i][keyHeaderColumnNumber[1]] = "00:"+content[i][keyHeaderColumnNumber[1]].substring(content[i][keyHeaderColumnNumber[1]].indexOf(":")+1);
						
					SimpleDateFormat sdf_src = new SimpleDateFormat(" MM dd yyyy HH:mm");
					try {
//						String fn=getFilename();
//						String year=fn.substring(fn.indexOf("2"),fn.indexOf("2")+4);
						Date d_src = sdf_src
								.parse((content[i][keyHeaderColumnNumber[0]]).substring(content[i][keyHeaderColumnNumber[0]].indexOf(" "))
										+ " "+year+" "
										+ content[i][keyHeaderColumnNumber[1]]);
						;
						tempEvent.setStartDate(tempEvent.sdf.format(d_src));
						// System.out.println(sdf.format(d_src));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						continue;
					
					}
					tempEvent.setServiceID("SMIT SD");
					txtEventList.add(tempEvent);
					// SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd
					// HH:mm:ss.0");
					// System.out.println(sdf.format(new Date()));
					// tempEvent.setStartDate(startDate);
				}
			}
		}
		return txtEventList;
	}


	public List parseSTGDFormat(String[][][] content) {
		// ===========read events from xls=======================
		List txtEventList = new ArrayList();

		if (HeaderRowNumber > -1) {
			for (int k = 0; k < content.length; k++) {
			
			boolean isNextDay=false;
				for (int i = HeaderRowNumber + 1; i < content[k].length; i++) {
					boolean lossKeyValue = false;
					for (int j = 0; j < keyHeaderColumnNumber.length; j++) {
						if (j == 3)
							continue;
						if (content[k][i][keyHeaderColumnNumber[j]] == null
								|| content[k][i][keyHeaderColumnNumber[j]]
										.equals("")) {
							System.out.println("Key Value can not be found at "
									+ i + " " + j);
							lossKeyValue = true;
							break;
						}
					}
					if (lossKeyValue == false) {
						TXTEvent tempEvent = new TXTEvent();
						tempEvent.setName(content[k][i][keyHeaderColumnNumber[2]]);

						// content[i][keyHeaderColumnNumber[0]] =
						// replaceMonth2Numbers(content[i][keyHeaderColumnNumber[0]]);

						if (i > 0)
							if (content[k][i][keyHeaderColumnNumber[0]]
									.endsWith("00:00"))
								// && content[i][keyHeaderColumnNumber[0]]
								// .equalsIgnoreCase(content[i -
								// 1][keyHeaderColumnNumber[0]]))
								content[k][i][keyHeaderColumnNumber[0]] = "24:00";
						SimpleDateFormat sdf_src = new SimpleDateFormat(
								"yyyy_MM_dd HH:mm");
						try {
							String date_str = ((String) sheetNames.get(k));
							date_str = date_str
									.substring(date_str.indexOf("_") + 1);
							System.out.println("date: " + date_str + " "
									+ content[k][i][keyHeaderColumnNumber[0]]);
							Date d_src = sdf_src.parse(date_str + " "
									+ content[k][i][keyHeaderColumnNumber[0]]);
							if(i>1)
							if (isNextDay || Integer
									.parseInt(content[k][i][keyHeaderColumnNumber[0]]
											.substring(
													0,
													content[k][i][keyHeaderColumnNumber[0]]
															.indexOf(":"))) < Integer
															.parseInt(content[k][i-1][keyHeaderColumnNumber[0]]
																					.substring(
																							0,
																							content[k][i-1][keyHeaderColumnNumber[0]]
																									.indexOf(":")))) {
							
								Calendar c1 = Calendar.getInstance();
								c1.setTime(d_src); // 1999 jan 20
								System.out.println("Date is : "
										+ sdf_src.format(c1.getTime()));

								
								c1.add(Calendar.DATE, 1);
								d_src = c1.getTime();
								isNextDay=true;

							}
							tempEvent.setStartDate(tempEvent.sdf.format(d_src));
							// System.out.println(sdf.format(d_src));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							continue;
						
						}
						txtEventList.add(tempEvent);
						// SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd
						// HH:mm:ss.0");
						// System.out.println(sdf.format(new Date()));
						// tempEvent.setStartDate(startDate);
					}
				}
			}
		}
		return txtEventList;
	}

	public List parseFoxNewsFormat(String[][] content) {
		// ===========read events from xls=======================
		List txtEventList = new ArrayList();

		if (HeaderRowNumber > -1) {
			for (int i = HeaderRowNumber + 1; i < content.length; i++) {
				boolean lossKeyValue = false;
				for (int j = 0; j < keyHeaderColumnNumber.length; j++) {
					if (content[i][keyHeaderColumnNumber[j]] == null
							|| content[i][keyHeaderColumnNumber[j]].equals("")) {
						System.out.println("Key Value can not be found at " + i
								+ " " + j);
						lossKeyValue = true;
						break;
					}
				}
				if (lossKeyValue == false) {
					TXTEvent tempEvent = new TXTEvent();
					tempEvent.setName(content[i][keyHeaderColumnNumber[2]]);

					content[i][keyHeaderColumnNumber[0]] = replaceMonth2Numbers(content[i][keyHeaderColumnNumber[0]]);

					if (i > 0)
						if (content[i][keyHeaderColumnNumber[1]]
								.endsWith("00:00")
								&& content[i][keyHeaderColumnNumber[0]]
										.equalsIgnoreCase(content[i - 1][keyHeaderColumnNumber[0]]))
							content[i][keyHeaderColumnNumber[1]] = "24:00";
					SimpleDateFormat sdf_src = new SimpleDateFormat(
							"yyyy/MM/dd HH:mm");
					try {
						// System.out.println("date:
						// "+content[i][keyHeaderColumnNumber[0]]
						// + " "
						// + content[i][keyHeaderColumnNumber[1]]);
						Date d_src = sdf_src
								.parse(replaceMonth2Numbers(content[i][keyHeaderColumnNumber[0]])
										+ " "
										+ content[i][keyHeaderColumnNumber[1]]);
						;
						tempEvent.setStartDate(tempEvent.sdf.format(d_src));
						// System.out.println(sdf.format(d_src));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						try {
							SimpleDateFormat sdf_src2 = new SimpleDateFormat(
									"MM dd, yyyy HH:mm");
							Date d_src2;

							d_src2 = sdf_src2
									.parse(content[i][keyHeaderColumnNumber[0]]
											+ " "
											+ content[i][keyHeaderColumnNumber[1]]);
							tempEvent
									.setStartDate(tempEvent.sdf.format(d_src2));
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							try {
								SimpleDateFormat sdf_src2 = new SimpleDateFormat(
										"dd-MM-yy HH:mm");
								Date d_src2;

								d_src2 = sdf_src2
										.parse(content[i][keyHeaderColumnNumber[0]]
												+ " "
												+ content[i][keyHeaderColumnNumber[1]]);
								tempEvent
										.setStartDate(tempEvent.sdf.format(d_src2));
							} catch (ParseException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
								continue;
							
							}
//							e1.printStackTrace();
//							continue;
						
						}
						;
					}
					txtEventList.add(tempEvent);
					// SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd
					// HH:mm:ss.0");
					// System.out.println(sdf.format(new Date()));
					// tempEvent.setStartDate(startDate);
				}
			}
		}
		return txtEventList;
	}

	public List parseNEWBFormat(String[][] content) {
		// ===========read events from xls=======================
		List txtEventList = new ArrayList();

		if (HeaderRowNumber > -1) {
			for (int i = HeaderRowNumber + 1; i < content.length; i++) {
				boolean lossKeyValue = false;
				for (int j = 0; j < keyHeaderColumnNumber.length; j++) {
					if (content[i][keyHeaderColumnNumber[j]] == null) {
						System.out.println("Key Value can not be found at " + i
								+ " " + j);
						lossKeyValue = true;
						break;
					}
				}
				if (lossKeyValue == false) {
					TXTEvent tempEvent = new TXTEvent();
					tempEvent.setName(content[i][keyHeaderColumnNumber[2]]
							+ ": " + content[i][keyHeaderColumnNumber[3]]);//

					content[i][keyHeaderColumnNumber[0]] = replaceMonth2Numbers(content[i][keyHeaderColumnNumber[0]]);
					if (i > 0)
						if (content[i][keyHeaderColumnNumber[1]]
								.endsWith("00:00:00")
								&& content[i][keyHeaderColumnNumber[0]]
										.equalsIgnoreCase(content[i - 1][keyHeaderColumnNumber[0]]))
							content[i][keyHeaderColumnNumber[1]] = "24:00";
					SimpleDateFormat sdf_src = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm");
					try {
						Date d_src = sdf_src
								.parse(content[i][keyHeaderColumnNumber[0]]
										+ " "
										+ content[i][keyHeaderColumnNumber[1]]);
						Calendar c_src=Calendar.getInstance();
						c_src.setTime(d_src);
						c_src.add(Calendar.HOUR, XLS2CSV.newbTimeshift);
//						tempEvent.setStartDate(tempEvent.sdf.format(d_src));
						tempEvent.setStartDate(tempEvent.sdf.format(c_src.getTime()));
						// System.out.println(sdf.format(d_src));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						continue;
					
					}
					txtEventList.add(tempEvent);
					// SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd
					// HH:mm:ss.0");
					// System.out.println(sdf.format(new Date()));
					// tempEvent.setStartDate(startDate);
				}
			}
		}
		return txtEventList;
	}

	public List parseNATGEOMUSICFormat(String[][] content) {
		// ===========read events from xls=======================
		List txtEventList = new ArrayList();

		if (HeaderRowNumber > -1) {
			for (int i = HeaderRowNumber + 1; i < content.length; i++) {
				boolean lossKeyValue = false;
				for (int j = 0; j < keyHeaderColumnNumber.length; j++) {
					if (content[i][keyHeaderColumnNumber[j]] == null ||content[i][keyHeaderColumnNumber[j]].trim().length()==0) {
						System.out.println("Key Value can not be found at " + i
								+ " " + j);
						lossKeyValue = true;
						break;
					}
				}
				if (lossKeyValue == false) {
					TXTEvent tempEvent = new TXTEvent();
					tempEvent.setName(content[i][keyHeaderColumnNumber[2]]);//
					content[i][keyHeaderColumnNumber[0]] = replaceMonth2Numbers(content[i][keyHeaderColumnNumber[0]]);
					if (i > 0)
						if (content[i][keyHeaderColumnNumber[1]]
								.endsWith("00.00")
								&& content[i][keyHeaderColumnNumber[0]]
										.equalsIgnoreCase(content[i - 1][keyHeaderColumnNumber[0]]))
							content[i][keyHeaderColumnNumber[1]] = "24.00";
					SimpleDateFormat sdf_src = new SimpleDateFormat(
							"dd-MM-yy HH.mm");
					try {
						Date d_src = sdf_src
								.parse(content[i][keyHeaderColumnNumber[0]]
										+ " "
										+ content[i][keyHeaderColumnNumber[1]]);
						;
						tempEvent.setStartDate(tempEvent.sdf.format(d_src));
						// System.out.println(sdf.format(d_src));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						continue;
					}
					txtEventList.add(tempEvent);
					// SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd
					// HH:mm:ss.0");
					// System.out.println(sdf.format(new Date()));
					// tempEvent.setStartDate(startDate);
				}
			}
		}
		System.out.println("parsing done!!");
		return txtEventList;
	}

	public List parseNGCHDFormat(String[][] content) {
		// ===========read events from xls=======================
		List txtEventList = new ArrayList();

		if (HeaderRowNumber > -1) {
			for (int i = HeaderRowNumber + 1; i < content.length; i++) {
				boolean lossKeyValue = false;
				for (int j = 0; j < keyHeaderColumnNumber.length; j++) {
					if (content[i][keyHeaderColumnNumber[j]] == null) {
						System.out.println("Key Value can not be found at " + i
								+ " " + j);
						lossKeyValue = true;
						break;
					}
				}
				if (lossKeyValue == false) {
					TXTEvent tempEvent = new TXTEvent();
					if (content[i][keyHeaderColumnNumber[3]]
							.equalsIgnoreCase(content[i][keyHeaderColumnNumber[4]]))
						tempEvent.setName(content[i][keyHeaderColumnNumber[3]]);
					else
						tempEvent.setName(content[i][keyHeaderColumnNumber[3]]
								+ ":" + content[i][keyHeaderColumnNumber[4]]);//
					content[i][keyHeaderColumnNumber[0]] = replaceMonth2Numbers(content[i][keyHeaderColumnNumber[0]]);
					SimpleDateFormat sdf_src = new SimpleDateFormat(
							"dd/MM/yyyy HH:mm:ss");
					try {
						Date d_src = sdf_src
								.parse(content[i][keyHeaderColumnNumber[0]]
										+ " "
										+ content[i][keyHeaderColumnNumber[1]]);

						tempEvent.setStartDate(tempEvent.sdf.format(d_src));
						// System.out.println(sdf.format(d_src));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						continue;
					
					}
					txtEventList.add(tempEvent);
					// SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd
					// HH:mm:ss.0");
					// System.out.println(sdf.format(new Date()));
					// tempEvent.setStartDate(startDate);
				}
			}
		}
		return txtEventList;
	}

	public List generateTXTEvents() {
		List txtEvents = null;
		switch (format) {
		case NAT_GEO_MUSIC:
			txtEvents = parseNATGEOMUSICFormat(getContent()[0]);
			break;
		case NGC_HD:
			txtEvents = parseNGCHDFormat(getContent()[0]);
			break;
		case NEWB:
			txtEvents = parseNEWBFormat(getContent()[0]);
			break;
		case FOX_NEWS:
			txtEvents = parseFoxNewsFormat(getContent()[0]);
			break;
		case STGD:
			txtEvents = parseSTGDFormat(getContent());
			break;
		case SMIT_HD:

			txtEvents= parseSMITHDFormat(getContent()[0]);
			break;
		case SMIT_HD_2:
		
			txtEvents =parseSMITHD_2Format(getContent()[0]);
			break;
		case SMIT_SD:
			txtEvents =parseSMITSDFormat(getContent()[0]);
			break;
		case SCM_HD:
			if(getFilename().endsWith(".xlsx") || getFilename().endsWith(".XLSX"))
				txtEvents= parseSCMHDFormat2(getContent()[0],SCM_HD);//for SCM(TW) HD
			else
				txtEvents= parseSCMHDFormat(SCM_HD);//for SCM
			break;
		case SCM:
			if(getFilename().endsWith(".xlsx") || getFilename().endsWith(".XLSX"))
				txtEvents= parseSCMHDFormat2(getContent()[0],SCM);//for SCM(TW) HD
			else
				txtEvents= parseSCMHDFormat(SCM);//for SCM
			break;
		case FX_HD:
			txtEvents = parseNGCHDFormat(getContent()[0]);
			break;
		case SW:
			if(getFilename().endsWith(".xlsx") || getFilename().endsWith(".XLSX"))
				txtEvents=parseSWFormat(getContent());
			else
				txtEvents=parseSWFormat();
			break;
		case V:
			txtEvents=parseVFormat();//parseSCCFormat
			break;
		case SCC:
			if(getFilename().endsWith(".xlsx") || getFilename().endsWith(".XLSX"))
				txtEvents=parseSCCFormat(getContent());
			break;
		case NGC:
			if(getFilename().endsWith(".docx") || getFilename().endsWith(".DOCX"))
				txtEvents=parseNGCFormat();
			break;
		case NGC_C:
			if(getFilename().endsWith(".xlsx") || getFilename().endsWith(".XLSX"))
				txtEvents=parseNGCCFormat(getContent());
			break;
		case STAR_EC:
			if(getFilename().endsWith(".xlsx") || getFilename().endsWith(".XLSX"))
				txtEvents=parseSTARECFormat(getContent());
			break;
		case FOX_HD:
			if(getFilename().endsWith(".xls") || getFilename().endsWith(".XLS"))
				txtEvents=parseFOXHDFormat(getContent());
			break;
		default:
			break;

		}

		return txtEvents;
	}

	public String[][][] getContent() {
		return content;
	}

	public void setContent(String[][][] content) {
		this.content = content;
		if (content.length > 0)
			for(int i=0;i<content.length;i++)
			{
				if(content[i]!=null){
					checkFormatByHeaderKeys(content[i]);
					break;
				}
			}
	}

	public int getFormat() {
		return format;
	}

	private String replaceMonth2Numbers(String str) {
		String[][] monthNames = {
				{ "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月",
						"十一月", "十二月" },
				{ "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
						"Sep", "Oct", "Nov", "Dec" } };
		for (int i = 0; i < monthNames.length; i++)
			for (int j = monthNames[i].length-1; j >-1; j--) {
				// System.out.println("str: " + str + " search: "
				// + monthNames[i][j]);
				if (str.indexOf(monthNames[i][j]) > -1) {
					String text = j < 10 ? "0" + (j + 1) : "" + (j + 1);
					str = str.replaceAll(monthNames[i][j], text);
					// System.out.println("replaceMonth2Numbers: " + str);
					return str;
				}
			}
		return str;
	}
	private String replaceWeekDay2Eng(String str) {
		String[][] dayChiNames = {
				{ "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日" }};
		String[] dayEngNames = { "MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN" };
		for (int i = 0; i < dayChiNames.length; i++)
			for (int j = dayChiNames[i].length-1; j >-1; j--) {
				// System.out.println("str: " + str + " search: "
				// + monthNames[i][j]);
				if (str.indexOf(dayChiNames[i][j]) > -1) {
					
					str = str.replaceAll(dayChiNames[i][j], dayEngNames[j]);
					// System.out.println("replaceMonth2Numbers: " + str);
					return str;
				}
			}
		return str;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
}
