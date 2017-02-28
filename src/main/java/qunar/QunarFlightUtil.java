package qunar;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yexianxun on 2017/2/28.
 */
public class QunarFlightUtil {

    private static List<QunarFlight> queryFromHZToSY() throws IOException {
        HttpClient client = HttpClients.createDefault();
        String url = "http://lp.flight.qunar.com/api/lp_calendar?dep=%E6%9D%AD%E5%B7%9E&arr=%E4%B8%89%E4%BA%9A&dep_date=2017-04-01&adultCount=1&month_lp=1&tax_incl=0&direct=0&callback=";
        HttpGet get = new HttpGet(url);
        HttpResponse response = client.execute(get);
        String result = EntityUtils.toString(response.getEntity());
        System.out.println(result);
        JSONObject json = JSON.parseObject(result);
        String banner = json.getJSONObject("data").getString("banner");
        return JSONObject.parseArray(banner, QunarFlight.class);
    }

    public static void main(String[] args) {
        try {
            int[] lastPrice = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE};
            boolean[] shouldSend = new boolean[]{false};
            int nextHour = 8;
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        List<QunarFlight> qunarFlights = queryFromHZToSY();
                        StringBuilder sb = new StringBuilder("查询日期:").append(String.valueOf(new Date())).append("<br>");
                        sb.append("日期\t最低价<br>");
                        qunarFlights.stream().forEach(qunar -> {
                            int price = Integer.valueOf(qunar.getPrice());
                            switch (qunar.getDepDate()) {
                                case "2017-04-12":
                                    if (price != lastPrice[0]) {
                                        sb.append(qunar.getDepDate() + "\t" + price).append(",前" + nextHour + "小时价格：").append(lastPrice[0]).append("<br>");
                                        lastPrice[0] = price;
                                        shouldSend[0] = true;
                                    } else {
                                        sb.append(qunar.getDepDate() + "\t" + price).append("<br>");
                                    }
                                    break;
                                case "2017-04-13":
                                    if (price != lastPrice[1]) {
                                        shouldSend[0] = true;
                                        sb.append(qunar.getDepDate() + "\t" + price).append(",前" + nextHour + "小时价格：").append(lastPrice[1]).append("<br>");
                                        lastPrice[1] = price;
                                    } else {
                                        sb.append(qunar.getDepDate() + "\t" + price).append("<br>");
                                    }
                                    break;
                                default:
                                    sb.append(qunar.getDepDate() + "\t" + price).append("<br>");
                                    break;
                            }

                        });
                        System.out.println(sb.toString());
                        if (shouldSend[0]) {
                            MailUtil.send("yxx_cmhd@163.com", "杭州-三亚", sb.toString());
                            shouldSend[0] = false;
                        } else {
                            System.out.println("价格未变，不发送邮件");
                        }
                        System.out.println("send success");
                    } catch (Exception e) {
                        e.printStackTrace();
                        try {
                            MailUtil.send("yxx_cmhd@163.com", "杭州-三亚", e.getMessage());
                        } catch (MessagingException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }, 0, 1000 * 60 * 60 * nextHour);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                MailUtil.send("yxx_cmhd@163.com", "杭州-三亚", e.getMessage());
            } catch (MessagingException e1) {
                e1.printStackTrace();
            }
        }
    }
}
