package co.junwei.cpabe;
import co.junwei.cpabe.policy.LangPolicy;
import it.unisa.dia.gas.jpbc.Element;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import co.junwei.bswabe.Bswabe;
import co.junwei.bswabe.BswabeCph;
import co.junwei.bswabe.BswabeCphKey;
import co.junwei.bswabe.BswabeElementBoolean;
import co.junwei.bswabe.BswabeMsk;
import co.junwei.bswabe.BswabePrv;
import co.junwei.bswabe.BswabePub;
import co.junwei.bswabe.SerializeUtils;

public class Cpabe {

	/**
	 * 启动方法
	 * @param
	 * @author Junwei Wang(wakemecn@gmail.com)
	 */
	public static void setup(String pubfile, String mskfile) throws IOException,
			ClassNotFoundException {
		byte[] pub_byte, msk_byte;
		BswabePub pub = new BswabePub();
		BswabeMsk msk = new BswabeMsk();
		Bswabe.setup(pub, msk);

		/* store BswabePub into mskfile */
		pub_byte = SerializeUtils.serializeBswabePub(pub);
		Common.spitFile(pubfile, pub_byte);

		/* store BswabeMsk into mskfile */
		msk_byte = SerializeUtils.serializeBswabeMsk(msk);
		Common.spitFile(mskfile, msk_byte);
	}
	//生成私钥
	public static void keygen(String pubfile, String prvfile, String mskfile,
			String attr_str) throws NoSuchAlgorithmException, IOException {
		BswabePub pub;
		BswabeMsk msk;
		byte[] pub_byte, msk_byte, prv_byte;

		/* get BswabePub from pubfile */
		pub_byte = Common.suckFile(pubfile);
		pub = SerializeUtils.unserializeBswabePub(pub_byte);

		/* get BswabeMsk from mskfile */
		msk_byte = Common.suckFile(mskfile);
		msk = SerializeUtils.unserializeBswabeMsk(pub, msk_byte);

		String[] attr_arr = LangPolicy.parseAttribute(attr_str);
		BswabePrv prv = Bswabe.keygen(pub, msk, attr_arr);

		/* store BswabePrv into prvfile */
		prv_byte = SerializeUtils.serializeBswabePrv(prv);
		Common.spitFile(prvfile, prv_byte);
	}

	public static String keygen_nofile(String pubfile,String mskfile, String attr_str) throws NoSuchAlgorithmException, IOException{
		BswabePub pub;
		BswabeMsk msk;
		byte[] pub_byte, msk_byte, prv_byte;

		/* get BswabePub from pubfile */
		pub_byte = Common.suckFile(pubfile);
		pub = SerializeUtils.unserializeBswabePub(pub_byte);

		/* get BswabeMsk from mskfile */
		msk_byte = Common.suckFile(mskfile);
		msk = SerializeUtils.unserializeBswabeMsk(pub, msk_byte);

		String[] attr_arr = LangPolicy.parseAttribute(attr_str);
		BswabePrv prv = Bswabe.keygen(pub, msk, attr_arr);

		/* store BswabePrv into prvfile */
		prv_byte = SerializeUtils.serializeBswabePrv(prv);
		String prvkey = Base64.getEncoder().encodeToString(prv_byte);
		return prvkey;
	}
    //加密方法
	public static String enc(String pubfile, String policy,
			String encfile,String cert) throws Exception {
		BswabePub pub;
		BswabeCph cph;
		BswabeCphKey keyCph;
		byte[] plt;
		byte[] cphBuf;
		byte[] aesBuf;
		byte[] pub_byte;
		Element m;

		/* get BswabePub from pubfile */
		pub_byte = Common.suckFile(pubfile);
		pub = SerializeUtils.unserializeBswabePub(pub_byte);

		keyCph = Bswabe.enc(pub, policy);
		cph = keyCph.cph;
		m = keyCph.key;
		System.err.println("m = " + m.toString());

		if (cph == null) {
			System.out.println("Error happed in enc");
			System.exit(0);
		}

		cphBuf = SerializeUtils.bswabeCphSerialize(cph);

		/* read file to encrypted */
		//plt = Common.suckFile(inputfile);
		/**
		 * 自己加的测试
		 */
		String message = cert;
		System.out.println("待加密密文："+message);
		plt = message.getBytes();

		aesBuf = AESCoder.encrypt(m.toBytes(), plt);
		String aes = Base64.getEncoder().encodeToString(aesBuf);
		//aesBuf = Base64.getDecoder().decode(aes);
		String cphStr = Base64.getEncoder().encodeToString(cphBuf);

		String aesMessage = aes+"-"+cphStr;

		// PrintArr("element: ", m.toBytes());
		//Common.writeCpabeFile(encfile, cphBuf, aesBuf);
		return aesMessage;
	}

	public static String enc_nofile(String pubfile, String policy, String cert) throws Exception {
		BswabePub pub;
		BswabeCph cph;
		BswabeCphKey keyCph;
		byte[] plt;
		byte[] cphBuf;
		byte[] aesBuf;
		byte[] pub_byte;
		Element m;

		/* get BswabePub from pubfile */
		pub_byte = Common.suckFile(pubfile);
		pub = SerializeUtils.unserializeBswabePub(pub_byte);

		keyCph = Bswabe.enc(pub, policy);
		cph = keyCph.cph;
		m = keyCph.key;
		System.err.println("m = " + m.toString());

		if (cph == null) {
			System.out.println("Error happed in enc");
			System.exit(0);
		}

		cphBuf = SerializeUtils.bswabeCphSerialize(cph);

		/* read file to encrypted */
		//plt = Common.suckFile(inputfile);
		/**
		 * 自己加的测试
		 */
		String message = cert;
		System.out.println("待加密密文："+message);
		plt = message.getBytes();

		aesBuf = AESCoder.encrypt(m.toBytes(), plt);
		String aes = Base64.getEncoder().encodeToString(aesBuf);
		//aesBuf = Base64.getDecoder().decode(aes);
		String cphStr = Base64.getEncoder().encodeToString(cphBuf);

		String aesMessage = aes+"-"+cphStr;

		// PrintArr("element: ", m.toBytes());
		//Common.writeCpabeFile(encfile, cphBuf, aesBuf);
		return aesMessage;
	}
	//解密方法
	public static String dec(String pubfile, String prvfile, String aesMessage) throws Exception {
		byte[] aesBuf, cphBuf;
		byte[] plt;
		byte[] prv_byte;
		byte[] pub_byte;
		byte[][] tmp;
		BswabeCph cph;
		BswabePrv prv;
		BswabePub pub;

		/* get BswabePub from pubfile */
		pub_byte = Common.suckFile(pubfile);
		pub = SerializeUtils.unserializeBswabePub(pub_byte);

		/* read ciphertext */
		//tmp = Common.readCpabeFile(encfile);
		//aesBuf = tmp[0];
		//cphBuf = tmp[1];
		String aes = aesMessage.split("-")[0];
		aesBuf = Base64.getDecoder().decode(aes);
		String cphStr = aesMessage.split("-")[1];
		cphBuf = Base64.getDecoder().decode(cphStr);


		cph = SerializeUtils.bswabeCphUnserialize(pub, cphBuf);

		/* get BswabePrv form prvfile */
		prv_byte = Common.suckFile(prvfile);
		prv = SerializeUtils.unserializeBswabePrv(pub, prv_byte);

		BswabeElementBoolean beb = Bswabe.dec(pub, prv, cph);
		System.err.println("e = " + beb.e.toString());
		if (beb.b) {
			plt = AESCoder.decrypt(beb.e.toBytes(), aesBuf);

			/**
			 *
			 */
			String message = new String(plt);
			System.out.println("解密出的："+message);
			return message;
			//Common.spitFile(decfile, plt);
		} else {
			System.exit(0);
			return "";
		}
	}

	public static String dec_nofile(String pubfile, String prvkey, String aesMessage) throws Exception {
		byte[] aesBuf, cphBuf;
		byte[] plt;
		byte[] prv_byte;
		byte[] pub_byte;
		byte[][] tmp;
		BswabeCph cph;
		BswabePrv prv;
		BswabePub pub;

		/* get BswabePub from pubfile */
		pub_byte = Common.suckFile(pubfile);
		pub = SerializeUtils.unserializeBswabePub(pub_byte);

		/* read ciphertext */
		//tmp = Common.readCpabeFile(encfile);
		//aesBuf = tmp[0];
		//cphBuf = tmp[1];
		String aes = aesMessage.split("-")[0];
		aesBuf = Base64.getDecoder().decode(aes);
		String cphStr = aesMessage.split("-")[1];
		cphBuf = Base64.getDecoder().decode(cphStr);


		cph = SerializeUtils.bswabeCphUnserialize(pub, cphBuf);

		/* get BswabePrv form prvfile */
		prv_byte = Base64.getDecoder().decode(prvkey);
		prv = SerializeUtils.unserializeBswabePrv(pub, prv_byte);

		BswabeElementBoolean beb = Bswabe.dec(pub, prv, cph);
		System.err.println("e = " + beb.e.toString());
		if (beb.b) {
			plt = AESCoder.decrypt(beb.e.toBytes(), aesBuf);

			/**
			 *
			 */
			String message = new String(plt);
			System.out.println("解密出的："+message);
			return message;
			//Common.spitFile(decfile, plt);
		} else {
			System.exit(0);
			return "";
		}
	}

}
