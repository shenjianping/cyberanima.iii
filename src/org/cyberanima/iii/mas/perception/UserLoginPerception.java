package org.cyberanima.iii.mas.perception;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import org.cyberanima.iii.common.dao.DataRow;
import org.cyberanima.iii.common.dao.TypeProperties;
import org.cyberanima.iii.common.jms.MessageFactory;
import org.cyberanima.iii.mas.perception.jms.PerceptionMessageProducer;
import org.cyberanima.iii.mas.perception.dao.DataSearch;
import org.cyberanima.iii.mas.perception.dao.DataWrite;

@Service(value = "userLoginPerception")
public class UserLoginPerception
{

    static Logger logger = Logger.getLogger(UserLoginPerception.class);

    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate2;

    @Resource
    private PerceptionMessageProducer perceptionMessageProducer;

    // public static void main(String[] arg) throws Exception {
    // doQuertz();
    // }

    public void doQuertz() throws Exception
    {
        logger.info("Start User Login Perceiving");

        // JdbcTemplate jdbcTemplate2=(JdbcTemplate)
        // SpringContextUtil.getBean("jdbcTemplate");
        // System.out.println("jdbcTemplate2: "+jdbcTemplate2);
        DataRow ub_userlogin = DataSearch.getUnperceivedUB();
        // System.out.println("matchmakingTask.getDataColumn(): " +
        // matchmakingTask);
        if (ub_userlogin.getDataColumn() == null)
        {
            return;
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSS", Locale.ENGLISH);
        int id = Integer.parseInt(ub_userlogin.getColumnString("id"));
        int type = Integer.parseInt(ub_userlogin.getColumnString("type"));
        Date time = dateFormat.parse(ub_userlogin.getColumnString("time").toString());
        int userid = Integer.parseInt(ub_userlogin.getColumnString("userid"));
        int pstatus = Integer.parseInt(ub_userlogin.getColumnString("pstatus"));
        

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("id", ub_userlogin.getColumnString("id"));
        paramMap.put("type", ub_userlogin.getColumnString("type"));
        paramMap.put("time", ub_userlogin.getColumnString("time"));
        paramMap.put("userid", ub_userlogin.getColumnString("userid"));
        paramMap.put("pstatus", ub_userlogin.getColumnString("pstatus"));

        // DataWrite.matchmakingTaskUpateByIDW(sID,
        // TypeProperties.MatchmakingTaskStatus_Handling, "", null);
        DataWrite.UBUpateByID(id, TypeProperties.UB_Perceived);
        perceptionMessageProducer.sendMsg(MessageFactory.createMessage(MessageFactory.MessageType.userLogin, ub_userlogin, paramMap), "iii_perception_ub_userlogin");
        System.out.println("HERE");

    }
}
