package com.blog.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jack Chen
 * */
public class BlogUtil {

	/**
	 * URL_PAGE：cnblogs url
	 * URL_PAGE_DETAIL：详情页url
	 * PAGE_COUNT：页数
	 * urlLists：所有详情页url Set集合（防止重复）
	 * p：匹配模式
	 * */
	public final static String URL_PAGE = "https://www.cnblogs.com/ccylovehs/default.html?page=";
	public final static String URL_PAGE_DETAIL = "https://www.cnblogs.com/ccylovehs/p/([0-9]+.html)";
	public final static int PAGE_COUNT = 3;
	public static Set<String> urlLists = new TreeSet<String>();
	public final static Pattern p = Pattern.compile(URL_PAGE_DETAIL);
	
	
	public static void main(String[] args) throws Exception {
		for(int i = 1;i<=PAGE_COUNT;i++) {
			getUrls(i);
		}
		for(Iterator<String> i = urlLists.iterator();i.hasNext();) {
			createFile(i.next());
		}
	}
	
	/**
	 * @param url
	 * @throws Exception
	 */
	private static void createFile(String url) throws Exception {
		Matcher m = p.matcher(url);
		m.find();
		String fileName = m.group(1);
		String prefix = "C://data//blog//";
		File file = new File(prefix + fileName);
		PrintStream ps = new PrintStream(file);

		URL u = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) u.openConnection();
		conn.connect();
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
		String str;
		
        while((str = br.readLine()) != null){
            ps.println(str);
        }
        ps.close();
        br.close();
        conn.disconnect();
	}
	
	/**
	 * @param idx 
	 * @throws Exception
	 */
	private static void getUrls(int idx) throws Exception{
		URL u = new URL(URL_PAGE+""+idx);
		HttpURLConnection conn = (HttpURLConnection) u.openConnection();
		conn.connect();
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
		String str;
        while((str = br.readLine()) != null){
            if(null != str && str.contains("https://www.cnblogs.com/ccylovehs/p/")) {
            	Matcher m = p.matcher(str);
            	if(m.find()) {
            		System.out.println(m.group(1));
            		urlLists.add(m.group());
            	}
            }
        }
        br.close();
        conn.disconnect();
	}
	
}
