/**
 * 
 */
package com.hp.nccc.iso8583.parse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.hp.nccc.iso8583.common.CommonFunction;
import com.hp.nccc.iso8583.core.B24TlvParseInfos;
import com.hp.nccc.iso8583.core.ChipParseInfo;
import com.hp.nccc.iso8583.core.ISO8583Factory;
import com.hp.nccc.iso8583.core.ISO8583VO;
import com.hp.nccc.iso8583.core.ISOType;

/**
 * @author hsiehes
 * 
 */
public class ConfigParse {

	private static final Logger log = LogManager.getLogger(ConfigParse.class);

	/**
	 * Creates a message factory configured from the default file, which is
	 * ISO8583.xml located in the root of the classpath, using the specified
	 * ClassLoader.
	 */
	public static ISO8583Factory<ISO8583VO> createDefault(ClassLoader loader) throws IOException {
		if (loader.getResource("ISO8583.xml") == null) {
			log.warn("ISO8583 ConfigParser cannot find ISO8583.xml, returning empty message factory");
			return new ISO8583Factory<ISO8583VO>();
		} else {
			return createFromClasspathConfig(loader, "ISO8583.xml");
		}
	}

	/**
	 * Creates a message factory configured from the default file, which is
	 * ISO8583.xml located in the root of the classpath, using the ISO8583Factory's
	 * ClassLoader.
	 */
	public static ISO8583Factory<ISO8583VO> createDefault() throws IOException {
		return createDefault(ISO8583Factory.class.getClassLoader());
	}

	/**
	 * Creates a message factory from the specified path inside the classpath, using
	 * the specified ClassLoader.
	 */
	public static ISO8583Factory<ISO8583VO> createFromClasspathConfig(String path) throws IOException {
		return createFromClasspathConfig(ISO8583Factory.class.getClassLoader(), path);
	}

	/**
	 * Creates a message factory from the specified path inside the classpath, using
	 * ISO8583Factory's ClassLoader.
	 */
	public static ISO8583Factory<ISO8583VO> createFromClasspathConfig(ClassLoader loader, String path)
			throws IOException {
		InputStream ins = loader.getResourceAsStream(path);
		ISO8583Factory<ISO8583VO> ifact = new ISO8583Factory<ISO8583VO>();
		if (ins != null) {
			log.debug("ISO8583 Parsing config from classpath file {" + path + "}");
			try {
				parse(ifact, ins);
			} finally {
				ins.close();
			}
		} else {
			log.warn("ISO8583 File not found in classpath: {" + path + "}");
		}
		return ifact;
	}

	/** Creates a message factory from the file located at the specified URL. */
	public static ISO8583Factory<ISO8583VO> createFromUrl(URL url) throws IOException {
		ISO8583Factory<ISO8583VO> ifact = new ISO8583Factory<ISO8583VO>();
		InputStream stream = url.openStream();
		try {
			parse(ifact, stream);
		} finally {
			stream.close();
		}
		return ifact;
	}
	
	/*20210331_EDCISO use*/
    public static ISO8583Factory<ISO8583VO> createFromByteArray(byte[] byteAarray) throws IOException {
        ISO8583Factory<ISO8583VO> ifact = new ISO8583Factory<ISO8583VO>();
        InputStream stream = new ByteArrayInputStream(byteAarray);
        try {
            parse(ifact, stream);
        } finally {
            stream.close();
        }
        return ifact;
    }

	/**
	 * Reads the XML from the stream and configures the message factory with its
	 * values.
	 * 
	 * @param ifact
	 *            The message factory to be configured with the values read from the
	 *            XML.
	 * @param stream
	 *            The InputStream containing the XML configuration.
	 */
	protected static <T extends ISO8583VO> void parse(ISO8583Factory<T> ifact, InputStream stream) throws IOException {
		DocumentBuilderFactory docfact = DocumentBuilderFactory.newInstance();
		DocumentBuilder docb = null;
		Document doc = null;
		try {
			docb = docfact.newDocumentBuilder();
			docb.setEntityResolver(new EntityResolver() {
				@Override
				public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
					if (systemId.contains("iso8583.dtd")) {
						return new InputSource(CommonFunction.getResourceAsStream("iso8583.dtd"));
					} else {
						return null;
					}
				}
			});

			doc = docb.parse(stream);

			doc.normalizeDocument();
			final Element root = doc.getDocumentElement();
			String encoding = root.getAttribute("encoding");
			if (encoding != null) {
				ifact.setCharacterEncoding(encoding);
			}
			String type = root.getAttribute("type");
			if (type == null || type.isEmpty()) {
				ifact.setType(ISO8583Factory.ISO8583_TYPE_AUTO);
			} else {
				ifact.setType(type);
			}
			String dataOnly = root.getAttribute("dataOnly");
			if (dataOnly == null || dataOnly.isEmpty()) {
				ifact.setDataOnly(false);
			} else {
				ifact.setDataOnly(Boolean.parseBoolean(dataOnly));
			}
			// Read the parsing guides
			ParseMapProvider tokenParseInfo = parseToken(ifact, root);
			ParseMapProvider tableParseInfo = parseTable(ifact, root);
			ChipParseInfo chipParseInfo = parseChip(ifact, root);
			B24TlvParseInfos b24TlvParseFieldInfo = parseB24Tlv(ifact, root);
			parseGuides(ifact, root, tokenParseInfo, chipParseInfo, tableParseInfo, b24TlvParseFieldInfo);
			parseHeaders(ifact, root);
		} catch (ParserConfigurationException ex) {
			log.error("ISO8583 Cannot parse XML configuration", ex);
			return;
		} catch (SAXException ex) {
			log.error("ISO8583 Parsing XML configuration", ex);
			return;
		}
	}

	protected static <T extends ISO8583VO> void parseHeaders(ISO8583Factory<T> ifact, final Element root)
			throws IOException {
		if (ifact.isDataOnly()) {
			ifact.setIsoHeader("default", "");
		} else {
			NodeList nodes = root.getElementsByTagName("header");
			if (nodes != null) {
				for (int i = 0; i < nodes.getLength(); i++) {
					Element elem = (Element) nodes.item(i);
					if (elem.getChildNodes() == null || elem.getChildNodes().getLength() == 0) {
						String header = "";
						String id = elem.getAttribute("ref");
						log.debug(String.format("Adding ISO8583 header for id{%s} : {%s}", id, header));
						ifact.setIsoHeader(id, header);
					} else {
						String header = elem.getChildNodes().item(0).getNodeValue();
						String id = elem.getAttribute("ref");
						log.debug(String.format("Adding ISO8583 header for id{%s} : {%s}", id, header));
						ifact.setIsoHeader(id, header);
					}
				}
			}
		}
	}

	private static <T extends ISO8583VO> ParseMapProvider parseToken(ISO8583Factory<T> ifact, final Element root)
			throws ParserConfigurationException, IOException {
		ParseMapProvider mapProvider = new ParseMapProvider();
		NodeList nodes = root.getElementsByTagName("token-parse");
		for (int i = 0; i < nodes.getLength(); i++) {
			Element elem = (Element) nodes.item(i);
			String id = elem.getAttribute("id");
			String parseDesc = elem.getAttribute("desc");
			String mti = elem.getAttribute("mti");
			String fillIn = elem.getAttribute("fill-in");
			mapProvider.setDesc(parseDesc, id, mti);
			// JTV - 20171012 - Seth
			if (!"".equals(fillIn)) {
				mapProvider.setDesc(fillIn, id, "fill-in");
			}
			NodeList fields = elem.getElementsByTagName("token-field");
			HashMap<Integer, FieldParseInfo> tempMap = new HashMap<Integer, FieldParseInfo>();
			for (int j = 0; j < fields.getLength(); j++) {
				Element f = (Element) fields.item(j);
				int num = Integer.parseInt(f.getAttribute("num"));
				ISOType itype = ISOType.valueOf(f.getAttribute("type"));
				int length = 0;
				if (f.getAttribute("length").length() > 0) {
					length = Integer.parseInt(f.getAttribute("length"));
				}
				String alt = "";
				if (f.getAttribute("alt") != null && f.getAttribute("alt").length() > 0) {
					alt = f.getAttribute("alt");
				}
				String desc = "";
				if (f.getAttribute("desc") != null && f.getAttribute("desc").length() > 0) {
					desc = f.getAttribute("desc");
				}
				String ref = "";
				if (f.getAttribute("ref") != null && f.getAttribute("ref").length() > 0) {
					ref = f.getAttribute("ref");
				}

				NodeList redefineFields = f.getElementsByTagName("token-redefine");
				Map<String, String> routerMap = null;
				if (redefineFields.getLength() > 0) {
					routerMap = new HashMap<String, String>();
					Element e = (Element) redefineFields.item(0);
					String defaultRef = e.getAttribute("default-ref");
					if (defaultRef == null || defaultRef.isEmpty()) {

					} else {
						routerMap.put(defaultRef, "default");
					}
					NodeList defineFields = e.getElementsByTagName("token-define");
					if (defineFields.getLength() > 0) {
						for (int k = 0; k < defineFields.getLength(); k++) {
							Element defineEle = (Element) defineFields.item(k);
							String defineRef = defineEle.getAttribute("ref");
							String defineExp = defineEle.getAttribute("selector-expression");
							routerMap.put(defineRef, defineExp);
						}
					}
				}

				FieldParseInfo fpi = FieldParseInfo.getInstance(itype, length, ifact.getCharacterEncoding(), alt, desc,
						ref);
				if (itype == ISOType.REDEF) {
					((RedefineParseInfo) fpi).setRouter(routerMap);
					StringBuffer sb = new StringBuffer();
					for (String refId : routerMap.keySet()) {
						sb.append(refId).append(",");
					}
					sb.deleteCharAt(sb.length() - 1);
					fpi.setReference(sb.toString());
				}
				if (itype == ISOType.B24TLV) {
					((B24TlvParseInfo) fpi).setB24TlvParseInfo(parseB24Tlv(ifact, root));
				}
				tempMap.put(num, fpi);
			}
			mapProvider.setFieldsParseInfo(tempMap, id, mti);
		}

		// process reference
		for (String id : mapProvider.getMap().keySet()) {
			Map<Integer, FieldParseInfo> tempMap = mapProvider.getMap().get(id);
			for (FieldParseInfo fpi : tempMap.values()) {
				if ((fpi.getReference() != null && !fpi.getReference().isEmpty()) || fpi.getType() == ISOType.REDEF) {
					String[] ids = fpi.getReference().split(",");
					for (String refId : ids) {
						if (!mapProvider.containsKey(refId)) {
							throw new ParserConfigurationException(
									String.format("can't find ref id [%s] in xml", refId));
						} else {
							if (fpi.getMapProvider() == null) {
								fpi.setMapProvider(mapProvider.getSubParseMapProvider(refId));
							} else {
								ParseMapProvider tempMapP = mapProvider.getSubParseMapProvider(refId);
								fpi.getMapProvider().setFieldsParseInfo(tempMapP.getFieldsParseInfo(refId, ""), refId,
										"");
								fpi.getMapProvider().setDesc(tempMapP.getDesc(refId, ""), refId, "");
							}
						}
					}
				}
			}
		}
		return mapProvider;
	}

	private static <T extends ISO8583VO> ParseMapProvider parseTable(ISO8583Factory<T> ifact, final Element root)
			throws ParserConfigurationException {
		ParseMapProvider mapProvider = new ParseMapProvider();
		NodeList nodes = root.getElementsByTagName("table-parse");
		for (int i = 0; i < nodes.getLength(); i++) {
			Element elem = (Element) nodes.item(i);
			String id = elem.getAttribute("id");
			String parseDesc = elem.getAttribute("desc");
			String mti = elem.getAttribute("mti");
			String lengthType = elem.getAttribute("length-type");
			mapProvider.setDesc(parseDesc, id, mti);
			mapProvider.setLengthType(lengthType, id);
			NodeList fields = elem.getElementsByTagName("table-field");
			HashMap<Integer, FieldParseInfo> tempMap = new HashMap<Integer, FieldParseInfo>();
			for (int j = 0; j < fields.getLength(); j++) {
				Element f = (Element) fields.item(j);
				int num = Integer.parseInt(f.getAttribute("num"));
				ISOType itype = ISOType.valueOf(f.getAttribute("type"));
				int length = 0;
				if (f.getAttribute("length").length() > 0) {
					length = Integer.parseInt(f.getAttribute("length"));
				}
				String alt = "";
				if (f.getAttribute("alt") != null && f.getAttribute("alt").length() > 0) {
					alt = f.getAttribute("alt");
				}
				String desc = "";
				if (f.getAttribute("desc") != null && f.getAttribute("desc").length() > 0) {
					desc = f.getAttribute("desc");
				}
				String ref = "";
				if (f.getAttribute("ref") != null && f.getAttribute("ref").length() > 0) {
					ref = f.getAttribute("ref");
				}
				tempMap.put(num,
						FieldParseInfo.getInstance(itype, length, ifact.getCharacterEncoding(), alt, desc, ref));
			}
			mapProvider.setFieldsParseInfo(tempMap, id, mti);
		}

		// process reference
		for (String id : mapProvider.getMap().keySet()) {
			Map<Integer, FieldParseInfo> tempMap = mapProvider.getMap().get(id);
			for (FieldParseInfo fpi : tempMap.values()) {
				if (fpi.getReference() != null && !fpi.getReference().isEmpty()) {
					String[] ids = fpi.getReference().split(",");
					for (String refId : ids) {
						if (!mapProvider.containsKey(refId)) {
							throw new ParserConfigurationException(
									String.format("can't find ref id [%s] in xml", refId));
						} else {
							fpi.setMapProvider(mapProvider.getSubParseMapProvider(refId));
						}
					}
				}

			}
		}
		return mapProvider;
	}

	protected static <T extends ISO8583VO> ChipParseInfo parseChip(ISO8583Factory<T> ifact, final Element root)
			throws ParserConfigurationException {
		ChipParseInfo chipParseInfo = null;
		NodeList nodes = root.getElementsByTagName("chip");
		if (nodes != null) {
			for (int i = 0; i < nodes.getLength(); i++) {
				Element elem = (Element) nodes.item(i);
				if (elem.getChildNodes() == null || elem.getChildNodes().getLength() == 0) {
					throw new ParserConfigurationException("Invalid ISO8583 chip element");
				} else {
					chipParseInfo = new ChipParseInfo();
					String twoByteTag = elem.getAttribute("dublebytestag");
					if (twoByteTag == null || twoByteTag.isEmpty()) {

					} else {
						String[] twoBytes = twoByteTag.split(",");
						for (String s : twoBytes) {
							chipParseInfo.getDoubleBytesTags().add(CommonFunction.hexDecode(s)[0]);
						}
					}
					String triByteTag = elem.getAttribute("triplebytestag");
					if (triByteTag == null || triByteTag.isEmpty()) {

					} else {
						String[] triBytes = triByteTag.split(",");
						for (String s : triBytes) {
							chipParseInfo.getTripleBytesTags().add(s);
						}
					}
					NodeList fields = elem.getElementsByTagName("chip-field");
					for (int j = 0; j < fields.getLength(); j++) {
						Element f = (Element) fields.item(j);
						ChipParseInfo.ChipFieldInfo fieldInfo = chipParseInfo.new ChipFieldInfo();

						byte[] tag = CommonFunction.hexDecode(f.getAttribute("tag"));
						fieldInfo.setTag(tag);
						String id = CommonFunction.bytesToHex(tag);

						int length = 0;
						if (f.getAttribute("length").length() > 0) {
							length = Integer.parseInt(f.getAttribute("length"));
							fieldInfo.setLength(length);
						}

						String desc = "";
						if (f.getAttribute("desc") != null && f.getAttribute("desc").length() > 0) {
							desc = f.getAttribute("desc");
							fieldInfo.setDesc(desc);
						}
						chipParseInfo.getChipFieldInfos().put(id, fieldInfo);
					}
				}
			}
		}
		return chipParseInfo;
	}

	protected static <T extends ISO8583VO> B24TlvParseInfos parseB24Tlv(ISO8583Factory<T> ifact, final Element root)
			throws ParserConfigurationException {
		B24TlvParseInfos b24TlvParseInfo = null;
		NodeList nodes = root.getElementsByTagName("b24-tlv");
		if (nodes != null) {
			b24TlvParseInfo = new B24TlvParseInfos();
			for (int i = 0; i < nodes.getLength(); i++) {
				Element elem = (Element) nodes.item(i);
				String id = elem.getAttribute("id");
				if (elem.getChildNodes() == null || elem.getChildNodes().getLength() == 0) {
					throw new ParserConfigurationException("Invalid ISO8583 b24-tlv element");
				} else {
					NodeList fields = elem.getElementsByTagName("b24-tlv-field");
					for (int j = 0; j < fields.getLength(); j++) {
						Element f = (Element) fields.item(j);
						B24TlvParseInfos.B24TlvFieldInfo fieldInfo = b24TlvParseInfo.new B24TlvFieldInfo();

						String tag = f.getAttribute("tag");
						fieldInfo.setTag(tag);

						String desc = "";
						if (f.getAttribute("desc") != null && f.getAttribute("desc").length() > 0) {
							desc = f.getAttribute("desc");
							fieldInfo.setDesc(desc);
						}
						b24TlvParseInfo.getB24TlvFieldInfo().put(id + "_" + tag, fieldInfo);
					}
				}
			}
		}
		return b24TlvParseInfo;
	}

	private static <T extends ISO8583VO> void parseGuides(ISO8583Factory<T> ifact, final Element root,
			ParseMapProvider tokenParseInfo, ChipParseInfo chipParseInfo, ParseMapProvider tableParseInfo,
			B24TlvParseInfos b24TlvParseFieldInfo) throws ParserConfigurationException {
		NodeList nodes = root.getElementsByTagName("parse");
		ParseMapProvider mapProvider = new ParseMapProvider();
		for (int i = 0; i < nodes.getLength(); i++) {
			Element elem = (Element) nodes.item(i);
			String id = elem.getAttribute("id");
			String parseDesc = elem.getAttribute("desc");
			String mti = elem.getAttribute("mti");
			mapProvider.setDesc(parseDesc, id, mti);
			NodeList fields = elem.getElementsByTagName("field");
			parseField(ifact, mapProvider, id, fields, mti);
		}

		// process reference
		for (String id : mapProvider.getMap().keySet()) {
			Map<Integer, FieldParseInfo> tempMap = mapProvider.getMap().get(id);
			for (FieldParseInfo fpi : tempMap.values()) {
				if ((fpi.getReference() != null && !fpi.getReference().isEmpty()) || fpi.getType() == ISOType.REDEF) {
					String[] ids = fpi.getReference().split(",");
					for (String refId : ids) {
						if (!mapProvider.containsKey(refId)) {
							throw new ParserConfigurationException(
									String.format("can't find ref id [%s] in xml", refId));
						} else {
							if (fpi.getMapProvider() == null) {
								fpi.setMapProvider(mapProvider.getSubParseMapProvider(refId));
							} else {
								ParseMapProvider tempMapP = mapProvider.getSubParseMapProvider(refId);
								fpi.getMapProvider().setFieldsParseInfo(tempMapP.getFieldsParseInfo(refId, ""), refId,
										"");
								fpi.getMapProvider().setDesc(tempMapP.getDesc(refId, ""), refId, "");
							}
						}
					}
				}
				if (fpi.getType() == ISOType.LLLCHIP) {
					((LllChipParseInfo) fpi).setChipParseInfo(chipParseInfo);
				} else if (fpi.getType() == ISOType.LLLTABLES) {
					fpi.setMapProvider(tableParseInfo);
					fpi.setSbuMapProvider(b24TlvParseFieldInfo);
				} else if (fpi.getType() == ISOType.LLLTOKENS || fpi.getType() == ISOType.LLLLTOKENS) {
					fpi.setMapProvider(tokenParseInfo);
					fpi.setSbuMapProvider(b24TlvParseFieldInfo);
				} else if (fpi.getType() == ISOType.POSKITZTAG) {
					((PoskitzTagParseInfo) fpi).setChipParseInfo(chipParseInfo);
				} else if (fpi.getType() == ISOType.SMARTPAYTAG) {
					((SmartPayTagParseInfo) fpi).setChipParseInfo(chipParseInfo);
				} else if (fpi.getType() == ISOType.B24TLV) {
					((B24TlvParseInfo) fpi).setB24TlvParseInfo(b24TlvParseFieldInfo);
				}

			}
			ifact.setParseMap(tempMap, id);
		}
	}

	private static <T extends ISO8583VO> void parseField(ISO8583Factory<T> ifact, ParseMapProvider mapProvider,
			String id, NodeList fields, String mti) {
		HashMap<Integer, FieldParseInfo> tempMap = new HashMap<Integer, FieldParseInfo>();
		for (int j = 0; j < fields.getLength(); j++) {
			Element f = (Element) fields.item(j);
			int num = Integer.parseInt(f.getAttribute("num"));
			ISOType itype = ISOType.valueOf(f.getAttribute("type"));
			int length = 0;
			if (f.getAttribute("length").length() > 0) {
				length = Integer.parseInt(f.getAttribute("length"));
			}
			String alt = "";
			if (f.getAttribute("alt") != null && f.getAttribute("alt").length() > 0) {
				alt = f.getAttribute("alt");
			}
			String desc = "";
			if (f.getAttribute("desc") != null && f.getAttribute("desc").length() > 0) {
				desc = f.getAttribute("desc");
			}
			String ref = "";
			if (f.getAttribute("ref") != null && f.getAttribute("ref").length() > 0) {
				ref = f.getAttribute("ref");
			}

			NodeList subFields = f.getElementsByTagName("field");
			if (subFields.getLength() > 0) {
				String subId = id + "_sub";
				ref = subId;
				parseField(ifact, mapProvider, subId, subFields, mti);
			}

			NodeList redefineFields = f.getElementsByTagName("redefine");
			Map<String, String> routerMap = null;
			if (redefineFields.getLength() > 0) {
				routerMap = new HashMap<String, String>();
				Element e = (Element) redefineFields.item(0);
				String defaultRef = e.getAttribute("default-ref");
				if (defaultRef == null || defaultRef.isEmpty()) {

				} else {
					routerMap.put(defaultRef, "default");
				}
				NodeList defineFields = e.getElementsByTagName("define");
				if (defineFields.getLength() > 0) {
					for (int i = 0; i < defineFields.getLength(); i++) {
						Element defineEle = (Element) defineFields.item(i);
						String defineRef = defineEle.getAttribute("ref");
						String defineExp = defineEle.getAttribute("selector-expression");
						routerMap.put(defineRef, defineExp);
					}
				}
			}
			FieldParseInfo fpi = FieldParseInfo.getInstance(itype, length, ifact.getCharacterEncoding(), alt, desc,
					ref);
			if (itype == ISOType.REDEF) {
				((RedefineParseInfo) fpi).setRouter(routerMap);
				StringBuffer sb = new StringBuffer();
				for (String refId : routerMap.keySet()) {
					sb.append(refId).append(",");
				}
				sb.deleteCharAt(sb.length() - 1);
				fpi.setReference(sb.toString());
			}
			tempMap.put(num, fpi);
		}
		mapProvider.setFieldsParseInfo(tempMap, id, mti);
	}

	/**
	 * Configures a ISO8583Factory using the default configuration file ISO8583.xml.
	 * This is useful if you have a ISO8583Factory created using Spring for example.
	 */
	public static <T extends ISO8583VO> void configureFromDefault(ISO8583Factory<T> ifact) throws IOException {
		if (ifact.getClass().getClassLoader().getResource("ISO8583.xml") == null) {
			log.warn("ISO8583 config file ISO8583.xml not found!");
		} else {
			configureFromClasspathConfig(ifact, "ISO8583.xml");
		}
	}

	/**
	 * This method attempts to open a stream from the XML configuration in the
	 * specified URL and configure the message factory from that config.
	 */
	public static <T extends ISO8583VO> void configureFromUrl(ISO8583Factory<T> ifact, URL url) throws IOException {
		InputStream stream = url.openStream();
		try {
			parse(ifact, stream);
		} finally {
			stream.close();
		}
	}

	/**
	 * Configures a ISO8583Factory using the configuration file at the path
	 * specified (will be searched within the classpath using the ISO8583Factory's
	 * ClassLoader). This is useful for configuring Spring-bound instances of
	 * ISO8583Factory for example.
	 */
	public static <T extends ISO8583VO> void configureFromClasspathConfig(ISO8583Factory<T> ifact, String path)
			throws IOException {
		InputStream ins = ifact.getClass().getClassLoader().getResourceAsStream(path);
		if (ins != null) {
			log.debug("ISO8583 Parsing config from classpath file {" + path + "}");
			try {
				parse(ifact, ins);
			} finally {
				ins.close();
			}
		} else {
			log.warn("ISO8583 File not found in classpath: {" + path + "}");
		}
	}

}
