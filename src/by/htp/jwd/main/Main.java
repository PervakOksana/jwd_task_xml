package by.htp.jwd.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

	public static void main(String[] args) {

		Map<String, Node> nodes = new HashMap<String, Node>();
		BufferedReader reader = null;
		String allLine = "";
		String line;

		try {
			reader = new BufferedReader(new FileReader("D://IT//EPAM//20210312//food.xml"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			while ((line = reader.readLine()) != null) {
				allLine = allLine + line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		//////////////////////////////////////////////////////

		String[] correctLine = allLine.split("<");
		String nameNode = "";
		String contentNode = "";
		Node node = null;
		Node nodeRemove = null;
		Attribute attr = null;new Attribute();

		for (int i = 1; correctLine.length > i; i++) {
			correctLine[i] = "<" + correctLine[i];

			Pattern pattern = Pattern.compile("<\\?(\\w*)");
			Pattern patternStart = Pattern.compile("<(\\w*)");
			Pattern patternEnd = Pattern.compile("<\\/(\\w*)");
			boolean matcher = pattern.matcher(correctLine[i]).find();
			boolean matcherStart = patternStart.matcher(correctLine[i]).find();
			boolean matcherEnd = patternEnd.matcher(correctLine[i]).find();

			//////////////////////////////////////////////////////

			if (matcherStart & !matcher & !matcherEnd) {

				Pattern patternu = Pattern.compile("\\<.*?\\>");
				Matcher m = patternu.matcher(correctLine[i]);
				while (m.find()) {
					nameNode = (String) (m.group().subSequence(1, m.group().length() - 1));

				}

				if (!"".equals(nameNode)) {

					String[] names = nameNode.split(" ");
					String[] contents = correctLine[i].trim().split(">");
					node = new Node();
					node.setName(nameNode);

					if (names.length > 1) {
						attr = new Attribute();
						attr.setId(names[1].split("=")[1].split("\"")[1]);
						nameNode = names[0];
						node.setName(nameNode);
						node.setAttrs(attr);
					}

					if (contents.length > 1) {
						node.setContent(contents[1]);
					}

				}
				nodes.put(nameNode, node);
			}

			//////////////////////////////////////////////////////

			if (matcherEnd) {
				nodeRemove = new Node();
				String str = correctLine[i];
				List<String> keys = new ArrayList<>(nodes.keySet());
				if (keys.size() > 2) {
					nodeRemove = nodes.get(keys.get(1));
					nodes.remove(keys.get(1));
					node = nodes.get(keys.get(2));
					node.setChilds(nodeRemove);
				}
				if (keys.size() > 1 && !(keys.size() > 2)) {
					nodeRemove = nodes.get(keys.get(1));
					nodes.remove(keys.get(1));
					node = nodes.get(keys.get(0));
					node.setChilds(nodeRemove);
				}
			}
			for (Map.Entry entry : nodes.entrySet()) {
				System.out.println(entry.getKey() + " : " + entry.getValue());
			}
			System.out.println("==================================================");
		}
		for (Map.Entry entry : nodes.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}

	}

}
