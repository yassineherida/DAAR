import java.io.BufferedReader;
import java.io.IOException;
import java.lang.Exception;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Properties;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.FileInputStream;

public class Crawl {
	public Crawl(){}
	public static void main (String[] args){
		Properties systemProperties = System.getProperties();
		System.setProperty("https.proxyHost","proxy");
		System.setProperty("https.proxyPort","3128");
		System.out.println("Init...");
		System.out.println("==========");
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		vertices.add(new Vertex("wikipedia.org", new ArrayList<String>()));
		vertices.get(0).urls.add("https://fr.wikipedia.org/wiki/Ligue_des_champions_de_l%27UEFA");
		//vertices.add(new Vertex("reddit.com", new ArrayList<String>()));
		//vertices.get(0).urls.add("https://www.reddit.com/r/news/");
		//vertices.get(0).urls.add("https://www-apr.lip6.fr/~buixuan/aaga2019");
		//vertices.get(0).urls.add("https://www-apr.lip6.fr/~buixuan/");
		ArrayList<String> todo = new ArrayList<String>();
		todo.addAll(vertices.get(0).urls);

		System.out.println("Starting with domain: "+vertices.get(0).domain);

		while (!todo.isEmpty() && vertices.size()<20) {
			String url = todo.remove(0);
			System.out.println("   > Checking URL: "+url);
			try {
				
				
				
				HttpURLConnection huc = (HttpURLConnection) (new URL(url)).openConnection();
				HttpURLConnection.setFollowRedirects(false);
				huc.setConnectTimeout(5 * 1000);
				huc.connect();
				InputStream inputStream = huc.getInputStream();

				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
				String inputLine;
				while ((inputLine = bufferedReader.readLine()) != null)
				{
					while (inputLine.contains("href")) {
						inputLine = inputLine.substring(inputLine.indexOf("href"));
						inputLine = inputLine.substring(3);
						inputLine = inputLine.substring(inputLine.indexOf("=")+1);

						String foundUrl = "NONE";
						try {
							if (inputLine.contains("\"")&&!inputLine.substring(0,inputLine.indexOf("\"")).contains("\'")) foundUrl = inputLine.split("\"")[1];
							if (inputLine.contains("\'")&&!inputLine.substring(0,inputLine.indexOf("\'")).contains("\"")) foundUrl = inputLine.split("\'")[1];
						} catch (Exception e) {
							System.out.println("ERROR parsing "+inputLine);
							System.out.println("   > Java fails with message error:");
							System.out.println(e);
							return;
						}

						if (foundUrl != "NONE") {
							String domainName = "NONE";
							if (foundUrl.startsWith("http")) {
								String[] dotParts = foundUrl.split("/")[2].split("\\.");
								int n = dotParts.length;
								if (n>1) domainName = dotParts[n-2]+"."+dotParts[n-1];
							}
							//if other URL format...

							if (domainName != "NONE") {
								boolean newDomain = true;
								for (Vertex v: vertices) if (v.domain.equals(domainName)) {
									newDomain = false;
									v.urls.add(foundUrl);
									break;
								}
								if (newDomain) {
									System.out.println("Found new domain: "+domainName);
									ArrayList<String> urlList = new ArrayList<String>();
									urlList.add(foundUrl);
									vertices.add(new Vertex(domainName, urlList));
									todo.add(domainName+"/index.html");
								}
							}
							todo.add(foundUrl);
						}
					}
				}
			} catch (IOException e){
				System.err.println("   =====");
				System.err.println("   > WARNING:");
				System.err.println("   > When opening:"+url);
				System.err.println("   > Unexpected IOException: "+e);
				System.err.println("   > Skipping URL input:"+url);
				System.err.println("   =====");
			}
		}
		System.out.println("==========");
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("Recap:");
		for (Vertex v: vertices) System.out.println("Found domain: "+v.domain);
		saveToFile("domainNames", vertices);
		return;
	}
	private static void saveToFile(String filename,ArrayList<Vertex> vertices){
		int index=0;
		try {
			while(true){
				BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(filename+((index==0)?"":"-"+Integer.toString(index)))));
				try {
					input.close();
				} catch (IOException e) {
					System.err.println("I/O exception: unable to close "+filename+Integer.toString(index)+".points");
				}
				index++;
			}
		} catch (FileNotFoundException e) {
			printToFile(filename+((index==0)?"":"-"+Integer.toString(index)), vertices);
		}
	}
	private static void printToFile(String filename, ArrayList<Vertex> vertices){
		try {
			PrintStream output = new PrintStream(new FileOutputStream(filename));
			int i=1;
			for (Vertex v: vertices) {
				output.println(Integer.toString(i)+" "+v.domain);
				i++;
			}
			output.close();
		} catch (FileNotFoundException e) {
			System.err.println("I/O exception: unable to create "+filename);
		}
	}
}
class Vertex {
	public String domain;
	public ArrayList<String> urls;
	public Vertex(String domain, ArrayList<String> urls){
		this.domain=domain;
		this.urls=urls;
	}
}