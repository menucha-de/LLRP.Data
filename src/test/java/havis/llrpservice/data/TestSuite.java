package havis.llrpservice.data;

import havis.llrpservice.data.message.parameter.serializer.ParameterByteBufferSerializerTest;
import havis.llrpservice.data.message.serializer.MessageByteBufferSerializerTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ DataTypeConverterTest.class,
		ParameterByteBufferSerializerTest.class,
		MessageByteBufferSerializerTest.class })
public class TestSuite {
}