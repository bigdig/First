package com.hanvoncloud;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.imageio.ImageIO;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.codec.binary.Base64;

import com.hanvoncloud.common.LanguageEnum;

public class HwCloud {
	private String root = "https://api.hanvon.com";
	private String uid = "javasdk";
	private static HwCloud hwCloud = null;

	public static HwCloud getInstance() {
		if (hwCloud == null) {
			hwCloud = new HwCloud();
		}
		return hwCloud;
	}

	public String recgHandLine(String key, String language, String handLineData) {
		String data = null;
		String code = null;
		if (LanguageEnum.CHNS.getCode().equals(language)) {
			data = "{\"uid\":\"" + this.uid + "\",\"lang\":\"" + language + "\",\"data\":\"" + handLineData + "\"}";
			code = "d4b92957-78ed-4c52-a004-ac3928b054b5";
		} else if (LanguageEnum.CHNT.getCode().equals(language)) {
			data = "{\"uid\":\"" + this.uid + "\",\"lang\":\"" + language + "\",\"data\":\"" + handLineData + "\"}";
			code = "05a7d172-ad21-4749-be0f-bfa4166d4da0";
		} else if (LanguageEnum.EN.getCode().equals(language)) {
			data = "{\"uid\":\"" + this.uid + "\",\"lang\":\"" + language + "\",\"data\":\"" + handLineData + "\"}";
			code = "f01d64a2-bd96-4554-8bcc-81d221f314a4";
		} else {
			return "请选择正确的语言";
		}
		String url = this.root + "/rt/ws/v1/hand/line?key=" + key + "&code=" + code;
		String result = postData(url, data);
		byte[] byteData = Base64.decodeBase64(result);
		try {
			result = new String(byteData, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	public String recgHandSingle(String key, String type, String data) {
		data = "{\"uid\":\"" + this.uid + "\",\"type\":\"" + type + "\",\"data\":\"" + data + "\"}";

		String url = this.root + "/rt/ws/v1/hand/single?key=" + key + "&code=83b798e7-cd10-4ce3-bd56-7b9e66ace93d";
		String result = postData(url, data);
		byte[] byteData = Base64.decodeBase64(result);
		try {
			result = new String(byteData, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	public String recgHandFormula(String key, String data) {
		data = "{\"uid\":\"" + this.uid + "\",\"data\":\"" + data + "\"}";
		String url = this.root + "/rt/ws/v1/formula/hand/recg?key=" + key
				+ "&code=14b05be6-a9a5-45bc-980b-afc2e9847742";
		String result = postData(url, data);
		return result;
	}

	public String recgIdcard(String key, String path) {
		String imageStr = testOcrPicEncodingNoGray(path);
		String data = "{\"uid\":\"" + this.uid + "\",\"image\":\"" + imageStr + "\"}";

		String url = this.root + "/rt/ws/v1/ocr/idcard/recg?key=" + key + "&code=8d497db3-7341-4f1f-875a-2f5444884515";

		String result = postData(url, data);
		return result;
	}

	public String recgIdcardWithCroppedImage(String key, String path) {
		String imageStr = testOcrPicEncodingNoGray(path);
		String data = "{\"uid\":\"" + this.uid + "\",\"image\":\"" + imageStr + "\"}";

		String url = this.root + "/rt/ws/v1/ocr/idcard/cropped/recg?key=" + key
				+ "&code=56bc30ea-2229-4891-9c92-8a022f10889f";
		String result = postData(url, data);
		return result;
	}

	public String recgText(String key, String lang, String path) {
		String imageStr = testOcrPicEncodingNoGray(path);

		String data = null;
		String code = null;
		if (LanguageEnum.CHNS.getCode().equals(lang)) {
			data = "{\"uid\":\"" + this.uid + "\",\"lang\":\"chns\",\"color\":\"color\",\"image\":\"" + imageStr
					+ "\"}";
			code = "74e51a88-41ec-413e-b162-bd031fe0407e";
		} else if (LanguageEnum.CHNT.getCode().equals(lang)) {
			data = "{\"uid\":\"" + this.uid + "\",\"lang\":\"all\",\"color\":\"color\",\"image\":\"" + imageStr + "\"}";
			code = "4060d49a-acf8-4897-9f61-5540bda01ed9";
		} else if (LanguageEnum.EN.getCode().equals(lang)) {
			data = "{\"uid\":\"" + this.uid + "\",\"lang\":\"en\",\"color\":\"color\",\"image\":\"" + imageStr + "\"}";
			code = "fe8b94d8-f34a-4804-abf0-4d1d072797cf";
		} else {
			return "请选择正确的语言";
		}
		String url = this.root + "/rt/ws/v1/ocr/text/recg?key=" + key + "&code=" + code;

		String result = postData(url, data);
		return result;
	}

	public String recgNumber(String key, String path) {
		String imageStr = testOcrPicEncoding(path);
		String data = null;
		String code = null;
		data = "{\"uid\":\"" + this.uid + "\",\"color\":\"gray\",\"image\":\"" + imageStr + "\"}";
		code = "3d901313-5b21-4c7c-9fc2-5dbcddcf0d42";

		String url = this.root + "/rt/ws/v1/ocr/number/recg?key=" + key + "&code=" + code;

		String result = postData(url, data);
		return result;
	}

	public String recgCertificate(String key, String path) {
		String imageStr = testOcrPicEncodingNoGray(path);
		String data = null;
		String code = null;
		data = "{\"uid\":\"" + this.uid
				+ "\",\"color\":\"gray\",\"reqtype\":\"1\",\"doctype\":\"1\",\"outpath\":\"e:/test\",\"image\":\""
				+ imageStr + "\"}";
		code = "3d901313-5b21-4c7c-9fc2-5dbcddcf0d42";

		String url = this.root + "/rt/ws/v1/ocr/certificate/recg?key=" + key + "&code=" + code;

		String result = postData(url, data);
		return result;
	}

	public String recgTable(String key, String path, String type) {
		if ((key == null) || ("".equals(key))) {
			return "key is null";
		}
		if ((path == null) || ("".equals(path))) {
			return "path is null";
		}
		if ((type == null) || ("".equals(type))) {
			return "key is null";
		}
		String imageStr = testOcrPicEncoding(path);

		String data = null;
		data = "{\"uid\":\"" + this.uid
				+ "\",\"color\":\"gray\",\"reqtype\":\"1\",\"doctype\":\"1\",\"outpath\":\"e:/test\",\"image\":\""
				+ imageStr + "\"}";

		String uri = "/rt/ws/v1/ocr/table/text/recg?key=" + key + "&code=0d3b7d23-915a-4c6f-9886-6312440aba51";
		if ("1".equals(type)) {
			uri = "/rt/ws/v1/ocr/table/text/recg?key=" + key + "&code=0d3b7d23-915a-4c6f-9886-6312440aba51";
		} else if ("2".equals(type)) {
			uri = "/rt/ws/v1/ocr/table/json/recg?key=" + key + "&code=95738911-8f66-49af-813d-75e9fe4b1ec2";
		} else if ("3".equals(type)) {
			uri = "/rt/ws/v1/ocr/table/enhancedjson/recg?key=" + key + "&code=540ab4a3-fa83-4fe2-b188-3edbac684d3f";
		} else if ("4".equals(type)) {
			uri = "/rt/ws/v1/ocr/table/doc/recg";
		}
		String url = this.root + uri;

		String result = postData(url, data);
		return result;
	}

	public String recgTableForYdbs(String path, String doctype) {
		String imageStr = testOcrPicEncoding(path);
		String data = "{\"doctype\":\"" + doctype + "\", \"image\":\"" + imageStr + "\"}";
		String url = this.root + "/rt/ws/v1/ocr/table/doc/recg";
		String result = postData(url, data);
		return result;
	}

	public String recgBankCard(String key, String path, String type) {
		if ((key == null) || ("".equals(key))) {
			return "key is null";
		}
		if ((path == null) || ("".equals(path))) {
			return "path is null";
		}
		if ((type == null) || ("".equals(type))) {
			return "type is null";
		}
		if ((!"0".equals(type)) && (!"1".equals(type))) {
			return "type is not exist";
		}
		String imageStr = testOcrPicEncodingNoGray(path);
		String data = null;
		data = "{\"uid\":\"" + this.uid + "\",\"image\":\"" + imageStr + "\"}";
		String uri = null;
		if ("0".equals(type)) {
			uri = "/rt/ws/v1/ocr/bankcard/recg?key=" + key + "&code=d72b071d-f407-42c9-b50c-8122bc4d5fc8";
		} else {
			uri = "/rt/ws/v1/ocr/bankcard/cropped/recg?key=" + key + "&code=e954b425-6380-4946-83ae-9cc44f7d8331";
		}
		String url = this.root + uri;
		System.out.println(url);
		String result = postData(url, data);
		return result;
	}

	public String recgVehicleCard(String key, String path, String type) {
		if ((key == null) || ("".equals(key))) {
			return "key is null";
		}
		if ((path == null) || ("".equals(path))) {
			return "path is null";
		}
		if ((type == null) || ("".equals(type))) {
			return "type is null";
		}
		if ((!"0".equals(type)) && (!"1".equals(type))) {
			return "type is not exist";
		}
		String imageStr = testOcrPicEncodingNoGray(path);
		String data = null;
		data = "{\"uid\":\"" + this.uid + "\",\"image\":\"" + imageStr + "\"}";
		String uri = null;
		if ("0".equals(type)) {
			uri = "/rt/ws/v1/ocr/vehiclecard/recg?key=" + key + "&code=6e8d31e0-f55b-4c22-8bcb-1211fd42596d";
		} else {
			uri = "/rt/ws/v1/ocr/vehiclecard/cropped/recg?key=" + key + "&code=2bdab410-71b7-4f6d-9b91-8db27c9197e4";
		}
		String url = this.root + uri;
		System.out.println(url);
		String result = postData(url, data);
		return result;
	}

	public String recgDriverCard(String key, String path, String type) {
		if ((key == null) || ("".equals(key))) {
			return "key is null";
		}
		if ((path == null) || ("".equals(path))) {
			return "path is null";
		}
		if ((type == null) || ("".equals(type))) {
			return "type is null";
		}
		if ((!"0".equals(type)) && (!"1".equals(type))) {
			return "type is not exist";
		}
		String imageStr = testOcrPicEncodingNoGray(path);
		String data = null;
		data = "{\"uid\":\"" + this.uid + "\",\"image\":\"" + imageStr + "\"}";
		String uri = null;
		if ("0".equals(type)) {
			uri = "/rt/ws/v1/ocr/drivercard/recg?key=" + key + "&code=8d45c31f-59d2-4004-907e-7eff134017df";
		} else {
			uri = "/rt/ws/v1/ocr/drivercard/cropped/recg?key=" + key + "&code=ea22962a-47c9-4cb6-9566-286a926736bc";
		}
		String url = this.root + uri;
		System.out.println(url);
		String result = postData(url, data);
		return result;
	}

	public String recgOcrFormula(String key, String path) {
		String imageStr = testOcrPicEncoding(path);
		String data = "{\"uid\":\"" + this.uid + "\",\"color\":\"gray\",\"image\":\"" + imageStr + "\"}";
		String code = "fb177e48-fc6b-4d43-9a60-e4487db4cd20";

		String url = this.root + "/rt/ws/v1/ocr/formula/recg?key=" + key + "&code=" + code;

		String result = postData(url, data);
		return result;
	}

	public String recgBcard(String key, String lang, String path) {
		String imageStr = testOcrPicEncodingNoGray(path);

		String data = "{\"uid\":\"" + this.uid + "\",\"color\":\"color\",\"image\":\"" + imageStr + "\"}";

		String code = null;
		if (LanguageEnum.CHNS.getCode().equals(lang)) {
			code = "91f6a58d-e418-4e58-8ec2-61b583c55ba2";
		} else if (LanguageEnum.CHNT.getCode().equals(lang)) {
			code = "e6a41101-e7aa-4f7a-a8c7-719bad73a564";
		} else if (LanguageEnum.EN.getCode().equals(lang)) {
			code = "a8ce6925-d91b-461b-bf18-99ae3663b23b";
		} else if (LanguageEnum.JPN.getCode().equals(lang)) {
			code = "7acea125-99fd-4391-860f-d2b4b30b8ab4";
		} else if (LanguageEnum.AUTO.getCode().equals(lang)) {
			code = "cf22e3bb-d41c-47e0-aa44-a92984f5829d";
		} else {
			return "请选择正确的语言";
		}
		String url = this.root + "/rt/ws/v1/ocr/bcard/recg?key=" + key + "&code=" + code;

		String result = postData(url, data);
		return result;
	}

	public String recgFaceAgeSex(String key, String path) {
		String imageStr = testOcrPicEncodingNoGray(path);
		String data = "{\"uid\":\"" + this.uid + "\",\"img_id\":\"img_idtest\",\"psn_id\":\"psn_idtest\",\"data\":\""
				+ imageStr + "\"}";
		String uri = this.root + "/rt/ws/v1/face/age/recg?key=" + key + "&code=2f873139-c6d0-462c-81d9-3623f96ecaa8";
		String result = postData(uri, data);
		return result;
	}

	public String recgFaceKeyPoint(String key, String path) {
		String imageStr = testOcrPicEncodingNoGray(path);
		String data = "{\"uid\":\"" + this.uid + "\",\"img_id\":\"img_idtest\",\"psn_id\":\"psn_idtest\",\"data\":\""
				+ imageStr + "\"}";

		String uri = this.root + "/rt/ws/v1/face/kpl/recg?key=" + key + "&code=7815ff0e-aac8-4d74-8b7e-b190398cc649";

		String result = postData(uri, data);
		return result;
	}

	public String recgFacePose(String key, String path, String instruction) {
		String imageStr = testOcrPicEncodingNoGray(path);
		String data = "{\"uid\":\"" + this.uid + "\",\"instruction\":\"" + instruction + "\",\"data\":\"" + imageStr
				+ "\"}";

		String uri = this.root + "/rt/ws/v1/face/pose/recg?key=" + key + "&code=1bcc68ca-7b85-4eb1-bb79-37647a56003e";

		String result = postData(uri, data);
		return result;
	}

	public String recgFace121(String key, String templateImagePath, String targetImagePath) {
		String templateImageStr = testOcrPicEncodingNoGray(templateImagePath);
		String targetImageStr = testOcrPicEncodingNoGray(targetImagePath);
		String data = "{\"uid\":\"" + this.uid + "\",\"templateImage\":\"" + templateImageStr + "\",\"targetImage\":\""
				+ targetImageStr + "\"}";

		String uri = this.root + "/rt/ws/v1/face/121/recg?key=" + key + "&code=04fd72cb-a3bc-43c6-b244-a2b23a31fe8f";

		String result = postData(uri, data);
		return result;
	}

	public String recgFaceId(String key, String idcardImagePath, String faceImagePath) {
		String idcardImageStr = testOcrPicEncodingNoGray(idcardImagePath);
		String facetImageStr = testOcrPicEncodingNoGray(faceImagePath);
		String data = "{\"uid\":\"" + this.uid + "\",\"idcardImage\":\"" + idcardImageStr + "\",\"faceImage\":\""
				+ facetImageStr + "\"}";

		String uri = this.root + "/rt/ws/v1/face/faceid/recg?key=" + key + "&code=1b88a702-7a32-4443-a146-9919c9474cc9";

		String result = postData(uri, data);
		return result;
	}

	public String recgPassport(String key, String path) {
		String imageStr = testOcrPicEncoding(path);
		String data = null;
		data = "{\"uid\":\"" + this.uid + "\",\"image\":\"" + imageStr + "\"}";
		String uri = "/rt/ws/v1/ocr/passport/recg?key=" + key + "&code=48aa4d87-5e12-42a9-82cd-4abb75577039";
		String url = this.root + uri;

		String result = postData(url, data);
		return result;
	}

	public String recgValueadd(String key, String path, String imgtype) {
		String imageStr = testOcrPicEncodingNoGray(path);

		String data = null;
		data = "{\"uid\":\"" + this.uid + "\",\"image\":\"" + imageStr + "\", \"imgtype\":\"" + imgtype + "\"}";
		String uri = "/rt/ws/v1/ocr/valueadded/recg?key=" + key + "&code=03b110d2-06ac-4e1c-ba76-6a55204b45e6";
		String url = this.root + uri;

		String result = postData(url, data);
		return result;
	}

	public String recgExpressTelephone(String key, String path) {
		String imageStr = testOcrPicEncodingNoGray(path);
		String data = null;
		data = "{\"uid\":\"" + this.uid + "\",\"image\":\"" + imageStr + "\"}";
		String uri = "/rt/ws/v1/ocr/expresstelephone/recg?key=" + key + "&code=1708cad4-20fc-424b-92f4-fc8ae6728f3f";
		String url = this.root + uri;
		String result = postData(url, data);
		return result;
	}

	public String recgHousehold(String key, String path, int householdType) {
		String imageStr = testOcrPicEncodingNoGray(path);
		String data = null;
		data = "{\"uid\":\"" + this.uid + "\",\"image\":\"" + imageStr + "\" ,\"householdType\":\"" + householdType
				+ "\"}";

		String uri = "/rt/ws/v1/ocr/household/recg?key=" + key + "&code=506f9fab-4382-4b9f-8173-a18229cf166d";
		String url = this.root + uri;
		String result = postData(url, data);
		return result;
	}

	private String testOcrPicEncoding(String path) {
		File imFile = new File(path);
		byte[] oriArray = new byte[(int) imFile.length()];
		try {
			FileInputStream fis = new FileInputStream(imFile);
			fis.read(oriArray);
			fis.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		String imageData = Base64.encodeBase64String(oriArray);

		return convertJpg2GrayImageString(imFile);
	}

	private String convertJpg2GrayImageString(File imFile) {
		BufferedImage colorImage = null;
		try {
			colorImage = ImageIO.read(imFile);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		BufferedImage grayImage = new BufferedImage(colorImage.getWidth(), colorImage.getHeight(), 10);
		try {
			Graphics gray = grayImage.createGraphics();
			gray.drawImage(colorImage, 0, 0, null);
			gray.dispose();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		String imageFormat = imFile.getName().substring(imFile.getName().lastIndexOf(".") + 1);
		boolean isOKflag = true;
		try {
			isOKflag = ImageIO.write(grayImage, imageFormat, out);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		if (isOKflag) {
			byte[] grayImageData = out.toByteArray();
			String imageData = Base64.encodeBase64String(grayImageData);
			return imageData;
		}
		return null;
	}

	private String testOcrPicEncodingNoGray(String path) {
		File imFile = new File(path);
		byte[] oriArray = new byte[(int) imFile.length()];
		try {
			FileInputStream fis = new FileInputStream(imFile);
			fis.read(oriArray);
			fis.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		String imageData = Base64.encodeBase64String(oriArray);

		return imageData;
	}

	private static class TrustAnyTrustManager implements X509TrustManager {
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[0];
		}
	}

	private static class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

	private String postDataToURLForHttps(String urlStr, String data) {
		StringBuilder sb = new StringBuilder();

		InputStream in = null;
		OutputStream out = null;
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new SecureRandom());
			URL console = new URL(urlStr);
			HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
			conn.setSSLSocketFactory(sc.getSocketFactory());
			conn.setHostnameVerifier(new TrustAnyHostnameVerifier());

			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/octet-stream");
			conn.setUseCaches(false);

			conn.getOutputStream().write(data.getBytes());
			conn.getOutputStream().flush();
			conn.getOutputStream().close();
			BufferedReader bin = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));

			int i = 0;
			String line;
			while ((line = bin.readLine()) != null) {
				// String line;
				i++;
				sb.append(line);
			}
			conn.disconnect();
		} catch (ConnectException e) {
			System.out.println("ConnectException");
			System.out.println(e);
			try {
				in.close();
			} catch (Exception localException) {
			}
			try {
				out.close();
			} catch (Exception localException1) {
			}
		} catch (IOException e) {
			System.out.println("IOException");
			System.out.println(e);
			try {
				in.close();
			} catch (Exception localException2) {
			}
			try {
				out.close();
			} catch (Exception localException3) {
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			try {
				in.close();
			} catch (Exception localException4) {
			}
			try {
				out.close();
			} catch (Exception localException5) {
			}
		} catch (KeyManagementException e) {
			e.printStackTrace();
			try {
				in.close();
			} catch (Exception localException6) {
			}
			try {
				out.close();
			} catch (Exception localException7) {
			}
		} finally {
			try {
				in.close();
			} catch (Exception localException8) {
			}
			try {
				out.close();
			} catch (Exception localException9) {
			}
		}
		return sb.toString();
	}

	private String postData(String urlStr, String data) {
		if (urlStr.startsWith("https:")) {
			return postDataToURLForHttps(urlStr, data);
		}
		StringBuilder sb = new StringBuilder();
		URL url = null;
		HttpURLConnection httpurlconnection = null;
		try {
			url = new URL(urlStr);
			httpurlconnection = (HttpURLConnection) url.openConnection();
			httpurlconnection.setDoInput(true);
			httpurlconnection.setDoOutput(true);
			httpurlconnection.setRequestMethod("POST");

			httpurlconnection.setRequestProperty("Content-Type", "application/octet-stream");
			httpurlconnection.getOutputStream().write(data.getBytes());
			httpurlconnection.getOutputStream().flush();
			httpurlconnection.getOutputStream().close();
			InputStream in = httpurlconnection.getInputStream();

			BufferedReader r = new BufferedReader(new InputStreamReader(in, "utf-8"));
			String line;
			while ((line = r.readLine()) != null) {
				// String line;
				sb.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (httpurlconnection != null) {
				httpurlconnection.disconnect();
			}
		}
		return sb.toString();
	}

	private static String getLocalMac() {
		InetAddress ia = null;
		byte[] mac = null;
		try {
			ia = InetAddress.getLocalHost();
			mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < mac.length; i++) {
			if (i != 0) {
				sb.append("-");
			}
			int temp = mac[i] & 0xFF;
			String str = Integer.toHexString(temp);
			if (str.length() == 1) {
				sb.append("0" + str);
			} else {
				sb.append(str);
			}
		}
		return sb.toString();
	}

	private static String getMACAddress() {
		byte[] mac = null;
		InetAddress ia = null;
		try {
			ia = InetAddress.getLocalHost();
			mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		if (mac != null) {
			for (int i = 0; i < mac.length; i++) {
				if (i != 0) {
					sb.append("-");
				}
				String s = Integer.toHexString(mac[i] & 0xFF);
				sb.append(s.length() == 1 ? 0 + s : s);
			}
		}
		return sb.toString().toUpperCase();
	}
}