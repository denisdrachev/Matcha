package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import matcha.properties.ConfigProperties;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;

@SpringBootTest
//@ContextConfiguration
//@RunWith(SpringRunner.class)
public class DemoApplicationTests {

//    @Autowired
//    ConfigProperties configProperties;

//	@Autowired
//	JdbcTemplate jdbcTemplate;
//
//	@Test
//	void insertTest() {
//		System.err.println(jdbcTemplate != null);
//	}

    @SneakyThrows
    @Test
    public void tesst() {
        String inputString = "Hello World!";
        byte[] byteArrray = inputString.getBytes();
        System.err.println(byteArrray);
        for (byte el : byteArrray) {
            System.err.print(el+" ");
        }
        System.err.println();

        String string = new String(byteArrray);
        System.err.println(string);
        Assert.assertEquals(inputString, string);
//        String charsetName = "IBM01140";
//        byte[] byteArrray2 = inputString.getBytes("IBM01140");
//        assertArrayEquals(
//                new byte[] { -56, -123, -109, -109, -106, 64, -26,
//                        -106, -103, -109, -124, 90 },
//                byteArrray2);
    }

    @SneakyThrows
    @Test
    public void tess2t() {
        String qq = "{\"aaa\":\"123\",\"bbb\":\"567\"}";
        final ObjectNode node = new ObjectMapper().readValue(qq, ObjectNode.class);
        System.err.println(node.has("aaa"));
        System.err.println(node.get("aaa"));
        JsonNode aaa = node.get("aaa");
        System.err.println(aaa.toPrettyString());
        System.err.println(aaa.toString());
        System.err.println(aaa.asText());
//        System.err.println(aaa.);

        String inputString = "pass";
//        System.err.println(Arrays. inputString));
        byte[] bytes = inputString.getBytes(StandardCharsets.US_ASCII);
        System.err.println("info: " + inputString.getBytes(StandardCharsets.UTF_8));
        for (byte el : bytes) {
            System.err.print(el+" ");
        }
        System.err.println();
        byte[] byteArrray = inputString.getBytes();
        System.err.println(byteArrray);
        for (byte el : byteArrray) {
            System.err.print(el+" ");
        }
        System.err.println();

        String string = new String(byteArrray);
        System.err.println(string);
        Assert.assertEquals(inputString, string);

//        String charsetName = "IBM01140";
//        byte[] byteArrray2 = inputString.getBytes("IBM01140");
//        assertArrayEquals(
//                new byte[] { -56, -123, -109, -109, -106, 64, -26,
//                        -106, -103, -109, -124, 90 },
//                byteArrray2);
    }

    @Test
    public void fff() {
        System.err.println(Calendar.getInstance().getTime());

        Date date = Calendar.getInstance().getTime();
    }

//    @Test
//    public void configurationPropertyTest() {
//        System.err.println(configurationProperty);
//    }

}
