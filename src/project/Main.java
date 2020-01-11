package project;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSParser;
public class Main {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		Scanner scan = new Scanner(System.in);
		RSSItems rss = new RSSItems();
		String zikankasegi = "..........................";
		System.out.println("ようこそRSSGamerリーダーへ");
		System.out.println("少々お待ちくださいませ。");
		try {
            Thread.sleep(10 * 500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

		for (int i = 0; i<100; i++) {
			System.out.println(zikankasegi);
			try {
	            Thread.sleep(10* 10);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }

		}
		System.out.println();
		while (true) {

			System.out.println("1:フィードを表示");
			System.out.println("2:ソースを追加");
			System.out.println("3:ソースを削除");
			System.out.println("4:ソースをすべて表示");
			System.out.println("アプリケーションを終了したい場合は、ctrl + z を入力してください。");
			int key = scan.nextInt();
			switch(key) {
			case 1://Show all RSS
				for (int i = 0; i < rss.sources.size(); i++) {
					rss.serverConect(rss.sources.get(i));
				}
				break;

			case 2://Add source
				System.out.println("追加したいフィードのURLを入力してください。");
				String addURL = scan.next();
				rss.addSource(addURL);
				break;

			case 3://Remove source
				System.out.println("削除したいフィードのURLを入力してください。");
				String removeURL = scan.next();
				rss.removeSource(removeURL);
				break;

			case 4://Show all sources
				System.out.println("すべてのフィードのソースを表示します。");
				rss.showAllSources();
				break;
			}
		}



	}


}

 class RSSItems {
	ArrayList<String> sources = new ArrayList<String>();
	public RSSItems() {
		sources.add("https://www.4gamer.net/rss/index.xml");
		sources.add("https://www.inside-games.jp/rss/index.rdf");
	}

	public void serverConect(String yourURL) {//インターネット上の特定のサーバーに接続
		try {
			URL url = new URL (yourURL);
			URLConnection connection = url.openConnection();
			connection.connect();
			InputStream inputStream = connection.getInputStream();
			Document document = this.buildDocument(inputStream, "utf-8");
			this.show(document.getDocumentElement());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public Document buildDocument (InputStream inputStream, String encoding) {//DOMTreeを構築するメソッド
		Document document = null;
		try {
			//DOM実装(implementation)の用意(Load and Save用)
			DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();//newInstance()はnew 〇〇と同じ意味
			DOMImplementationLS implementation = (DOMImplementationLS)registry.getDOMImplementation("XML 1.0");//読み込み(Load)と保存(Save)のためのDOMの実装(implementation)を表す。
			//読み込み対象の用意
			LSInput input  = implementation.createLSInput();
			input.setByteStream(inputStream);//読み込み方
			input.setEncoding(encoding);//エンコード方式。
			//構文解析器(parser)の用意
			LSParser parser = implementation.createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS, null);//同期モード.
			parser.getDomConfig().setParameter("namespaces", false);//
			//DOMの構築
			document = parser.parse(input);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return document;

	}

	public void show(Node node) {//RSSをコンソールに表示するやつ。
		for (Node current = node.getFirstChild();
				current != null;
				current = current.getNextSibling()) {
			if (current.getNodeType() == Node.ELEMENT_NODE) {//ノードが要素なら
				String nodeName = current.getNodeName();
				if (nodeName != "item" && nodeName != "title" && nodeName != "dc:date" && nodeName != "rdf:li" && nodeName != "items"
						&& nodeName != "rdf:Seq" && nodeName != "link" && nodeName != "channel" && nodeName != "description"
						&& nodeName !="dc:language" && nodeName != "dc:rights") {
					System.out.println(nodeName);
				}
				show(current);
			}else if (current.getNodeType() == Node.TEXT_NODE
					&& current.getNodeValue().trim().length() != 0){//ノードはテキスト？
		System.out.println(current.getNodeValue());

}	else if (current.getNodeType() == Node.CDATA_SECTION_NODE) {// ノードはCDATA?
	System.out.println(current.getNodeValue());
}//HTMLタグなどを含む
;//上記以外のノードでは何もしない。

		}

	}

	public boolean addSource (String url) {// It can add new source.

		if (isExistURL(url)) {
			sources.add(url);
			return true;
		}
		return false;
	}

	public boolean removeSource (String url) {// It can remove source.
		if (sources.contains(url)) {
			int removeIndex = sources.indexOf(url);
			sources.remove(removeIndex);
			return true;
		}
		return false;
	}

	public void showAllSources() {
		System.out.println("############################################################################################");
		for (int i = 0; i < sources.size(); i++) {
			System.out.println(sources.get(i));
		}
		System.out.println("All showed");
	}

	private boolean isExistURL(String urlString) { //Please only access from inner because it is used for addSource method
		URL url;
		int response = 0;

		try {
			url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("HEAD");// Get only an information of header
			connection.connect();// Connecting
			response = connection.getResponseCode();//Get a response code
			connection.disconnect();// Disconnect the server
		} catch (MalformedURLException e) {// If get unvaild format of URL
			e.printStackTrace();//Show informations of between start to end
		} catch (IOException e) { //If fail to any dealing input or output
			e.printStackTrace();//Show informations of between start to end
		}
		return response == HttpURLConnection.HTTP_OK;
	}
}
