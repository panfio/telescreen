package ru.panfio.telescreen.handler.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

public class TestFiles {
    public static InputStream toInputStream(String content) {
        return new ByteArrayInputStream(content.getBytes(Charset.forName("UTF-8")));
    }

    public static final String TIMESHEET = "<timesheet>\n" +
            "   <tags class=\"java.util.ArrayList\" >\n" +
            "      <tag>\n" +
            "         <color>-256</color>\n" +
            "         <created>1573636367497</created>\n" +
            "         <deleted>false</deleted>\n" +
            "         <tagId>38e1cf43d1e34c65b5b28bf0664138e4</tagId>\n" +
            "         <lastUpdate>1574706459770</lastUpdate>\n" +
            "         <name>Coding</name>\n" +
            "         <teamId></teamId>\n" +
            "         <user></user>\n" +
            "      </tag>\n" +
            "      <tag>\n" +
            "         <color>-256</color>\n" +
            "         <deleted>false</deleted>\n" +
            "         <tagId>239cf751346f401092ee8cbe09c2fa4d</tagId>\n" +
            "         <lastUpdate>1574706459770</lastUpdate>\n" +
            "         <name>Coding</name>\n" +
            "      </tag>\n" +
            "   </tags>\n" +
            "   <taskTags class=\"java.util.ArrayList\">\n" +
            "      <taskTag>\n" +
            "         <created>1573638285408</created>\n" +
            "         <deleted>false</deleted>\n" +
            "         <id>8a731336ab8b4632b119891b967cd98a</id>\n" +
            "         <lastUpdate>1574706459786</lastUpdate>\n" +
            "         <tagId>239cf751346f401092ee8cbe09c2fa4d</tagId>\n" +
            "         <taskId>d15a023ff743452eac1966f4bc428847</taskId>\n" +
            "         <user></user>\n" +
            "      </taskTag>" +
            "   </taskTags>" +
            "   <tasks>" +
            "      <task>\n" +
            "         <billable>1</billable>\n" +
            "         <billed>0</billed>\n" +
            "         <created>1575736089044</created>\n" +
            "         <deleted>false</deleted>\n" +
            "         <description>Description</description>\n" +
            "         <distance>0.0</distance>\n" +
            "         <endDate>2019-12-07T23:30:00+03:00</endDate>\n" +
            "         <feeling>0</feeling>\n" +
            "         <taskId>d15a023ff743452eac1966f4bc428847</taskId>\n" +
            "         <lastUpdate>1575750662414</lastUpdate>\n" +
            "         <location>Russia, 345678</location>\n" +
            "         <locationEnd>Russia, 345678</locationEnd>\n" +
            "         <paid>0</paid>\n" +
            "         <projectId>29f336e9f8dd4526966023719c2e5ff2</projectId>\n" +
            "         <startDate>2019-12-07T19:28:00+03:00</startDate>\n" +
            "         <type>0</type>\n" +
            "         <user></user>\n" +
            "      </task>" +
            "   </tasks>" +
            "</timesheet>";

    public static final String ACTIVITIES = "{\n" +
            "    \"activities\": [\n" +
            "        {\n" +
            "            \"name\": \"Google Chrome -> Dreams by Ytho.\", \n" +
            "            \"time_entries\": [\n" +
            "                {\n" +
            "                    \"days\": 0, \n" +
            "                    \"end_time\": \"2019-11-26 23:52:10\", \n" +
            "                    \"hours\": 0, \n" +
            "                    \"minutes\": 0, \n" +
            "                    \"seconds\": 9, \n" +
            "                    \"start_time\": \"2019-11-26 23:52:01\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"days\": 0, \n" +
            "                    \"end_time\": \"2019-11-26 23:55:00\", \n" +
            "                    \"hours\": 0, \n" +
            "                    \"minutes\": 0, \n" +
            "                    \"seconds\": 9, \n" +
            "                    \"start_time\": \"2019-11-26 23:52:30\"\n" +
            "                }\n" +
            "            ]\n" +
            "        }\n" +
            "    ]\n" +
            "}";

    public static final String TELEGRAM = "<!DOCTYPE html>\n" +
            "<html>\n" +
            " <body onload=\"CheckLocation();\">\n" +
            "  <div class=\"page_wrap\">\n" +
            "   <div class=\"page_body chat_page\">\n" +
            "    <div class=\"history\">\n" +
            "     <div class=\"message default clearfix\" id=\"message1234\">\n" +
            "      <div class=\"pull_left userpic_wrap\">\n" +
            "       <div class=\"userpic userpic4\" style=\"width: 42px; height: 42px\">\n" +
            "        <div class=\"initials\" style=\"line-height: 42px\">\n" +
            "A\n" +
            "        </div>\n" +
            "       </div>\n" +
            "      </div>\n" +
            "      <div class=\"body\">\n" +
            "       <div class=\"pull_right date details\" title=\"30.09.2018 22:03:27\">\n" +
            "22:03\n" +
            "       </div>\n" +
            "       <div class=\"from_name\">\n" +
            "Alex \n" +
            "       </div>\n" +
            "       <div class=\"text\">\n" +
            "<a href=\"https://example.com\">https://example.com</a>\n" +
            "       </div>\n" +
            "      </div>\n" +
            "     </div>\n" +
            "     <div class=\"message default clearfix joined\" id=\"message4321\">\n" +
            "      <div class=\"body\">\n" +
            "       <div class=\"pull_right date details\" title=\"11.08.2019 22:54:49\">\n" +
            "22:54\n" +
            "       </div>\n" +
            "       <div class=\"text\">\n" +
            "TEST text1\n" +
            "       </div>\n" +
            "      </div>\n" +
            "     </div>\n" +
            "    </div>\n" +
            "   </div>\n" +
            "  </div>\n" +
            " </body>\n" +
            "</html>";
}
