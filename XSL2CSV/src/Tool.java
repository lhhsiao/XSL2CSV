
public class Tool {
	public static String trim2OneSpace(String str){
		String result="";
		char[] c_array=str.toCharArray();
		char last_c='x';
		for(char c:c_array){
			if(last_c!=' ' || c!=' '){
				result+=c;
				
			}
			last_c=c;
		}
		
		return result;
	}
}
