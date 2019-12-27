package project;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSParser;
public class Main {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		RSSItems rss = new RSSItems("https://www.4gamer.net/rss/pc/pc_news.xml");
		rss.serverConect("https://www.inside-games.jp/rss/index.rdf");

	}


}

 class RSSItems {
	String url;

	public RSSItems(String url) {
		this.url = url;
	}

	public void serverConect(String yourURL) {//インターネット上の特定のサーバーに接続
		try {
			URL url = new URL (yourURL);
			URLConnection connection = url.openConnection();
			connection.connect();
			InputStream inputStream = connection.getInputStream();
			Document document = this.buildDocument(inputStream, "utf-8");
			this.showTree(document.getDocumentElement());
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

	public void showTree(Node node) {//RSSをコンソールに表示するやつ。
		for (Node current = node.getFirstChild();
				current != null;
				current = current.getNextSibling()) {
			if (current.getNodeType() == Node.ELEMENT_NODE) {//ノードが要素なら
				String nodeName = current.getNodeName();
				System.out.println(nodeName);
				showTree(current);
				System.out.println();
			}else if (current.getNodeType() == Node.TEXT_NODE
					&& current.getNodeValue().trim().length() != 0){//ノードはテキスト？
		System.out.println(current.getNodeValue());

}	else if (current.getNodeType() == Node.CDATA_SECTION_NODE) {// ノードはCDATA?
	System.out.println(current.getNodeValue());
}//HTMLタグなどを含む
;//上記以外のノードでは何もしない。

		}

	}
}