package oga.binary.bind.test;

import java.nio.ByteBuffer;
import java.util.Date;

import oga.binary.bind.JABBConversionProvider;
import oga.binary.bind.JABBException;
import oga.binary.bind.JABBMarshallException;
import oga.binary.bind.JABBMarshaller;
import oga.binary.bind.JABBUnmarshaller;
import oga.binary.bind.JABBUnsupportedDataTypeException;
import oga.binary.bind.support.HEX;

import org.junit.Test;

public class JABBSimpleTest {

	private JABBConversionProvider jabbConversionProvider = null;

	public JABBSimpleTest() {
		super();
		jabbConversionProvider = new JABBConversionProvider();
	}

	@Test
	public void mashaller() {

		SimpleMessage msg = new SimpleMessage();
		msg.setId(0x12L);
		msg.setName("osm515");
		msg.setType('a');
		msg.setCreatedOn(new Date());
		SimpleChildMessage smsg = new SimpleChildMessage();
		smsg.setId(0x34L);
		smsg.setName("test");
		smsg.setType('b');
		smsg.setCreatedOn(new Date());
		msg.setChildMessage(smsg);
		/*
		 * marshalling
		 */
		try {

			JABBMarshaller marshaller = new JABBMarshaller(
					jabbConversionProvider);
			byte[] bytes = marshaller.marshall(msg);
			//
			String hexString = HEX.getFormatedString(bytes);
			System.out.println(hexString);

			System.out.println(msg);
			JABBUnmarshaller unmarshaller = new JABBUnmarshaller(
					jabbConversionProvider);
			SimpleMessage unmsg = unmarshaller.unmarshall(bytes,
					SimpleMessage.class);
			System.out.println(unmsg);

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (JABBMarshallException e) {
			e.printStackTrace();
		} catch (JABBUnsupportedDataTypeException e) {
			e.printStackTrace();
		} catch (JABBException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void unmashaller() {

		byte[] src = new byte[] { 0x00, 0x00, 0x00, 0x12, 0x6F, 0x73, 0x6D, 0x35,
				0x31, 0x35, 0x20, 0x20, 0x20, 0x20, 0x61, 0x20, 0x12, 0x09,
				0x18, 0x01, 0x42, 0x51, 0x00, 0x00, 0x00, 0x34, 0x74, 0x65,
				0x73, 0x74, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x62, 0x20,
				0x12, 0x09, 0x18, 0x01, 0x42, 0x51 };

		JABBUnmarshaller unmarshaller = new JABBUnmarshaller(
				jabbConversionProvider);
		try {
			SimpleMessage obj = (SimpleMessage) unmarshaller.unmarshall(src,
					SimpleMessage.class);
			System.out.println(obj);
		} catch (JABBException e) {
			e.printStackTrace();
		}
	}

	private void getBytesFromObject(ByteBuffer buffer, Long value, int length) {
		Long longVal = value;
		int bitLength = length * 8;
		byte bytePad = 0x00;
		for (int i = 0; i < bitLength; i += 8) {
			if (i < Long.SIZE) {
				longVal = value >> i;
				System.out.println(Long.toHexString(longVal));
				buffer.put(longVal.byteValue());
			} else {
				buffer.put(bytePad);
			}
		}
	}

	private Long getObjectFromBytes(ByteBuffer buffer, int length) {
		int bitLength = length * 8;
		Long result = 0L;
		for (int i = 0; i < bitLength; i += 8) {
			if (i < Long.SIZE) {
				byte byteVal = buffer.get();
				result += (long) (byteVal & 0xFF) << i;
				System.out.println("byte:" + Integer.toHexString(byteVal)
						+ ", Long:" + Long.toHexString(result));
			} else {
				buffer.get();
			}
		}
		return result;
	}

	@Test
	public void testNumber() {
		ByteBuffer buffer = ByteBuffer.allocate(24);
		System.out.println("allocate:" + buffer.remaining());
		Long value = 0x0123456789ABCDEFL;
		System.out.println("value:" + Long.toHexString(value));
		getBytesFromObject(buffer, value, 10);
		System.out.println("before flip buffer : "
				+ HEX.getFormatedString(buffer.array()));
		System.out.println("before flip:" + buffer.remaining());
		buffer.flip();
		System.out.println("after flip buffer : "
				+ HEX.getFormatedString(buffer.array()));
		System.out.println("after flip:" + buffer.remaining());
		Long convertedVal = getObjectFromBytes(buffer, 10);
		System.out.println(Long.toHexString(convertedVal));
		System.out.println("after reading:" + buffer.remaining());

		ByteBuffer newbuffer = ByteBuffer.allocate(24);
		System.out.println("before wrap buffer : "
				+ HEX.getFormatedString(newbuffer.array()));
		newbuffer = ByteBuffer.wrap(newbuffer.array(), 0, 14);
		System.out.println("after wrap buffer : "
				+ HEX.getFormatedString(newbuffer.array()));
	}

	@Test
	public void dateTest() {
		System.out.println(Long.TYPE);
	}
}
