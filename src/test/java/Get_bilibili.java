import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Get_bilibili implements Runnable{
    @Override
    public void run() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        RequestConfig requestConfig = RequestConfig.custom()
                .build();
        //2.创建get请求，相当于在浏览器地址栏输入 网址
        HttpGet request = new HttpGet("https://www.bilibili.com/v/popular/rank/all");
        requestConfig = RequestConfig.custom()
                .setConnectTimeout(1000)//设置创建连接的最长时间
                .setConnectionRequestTimeout(500)//设置获取连接的最长时间
                .setSocketTimeout(10 * 1000)//设置数据传输的最长时间
                .setCookieSpec(CookieSpecs.STANDARD)
                .build();
        request.setConfig(requestConfig);
        request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36");
        request.setHeader("cookie","_uuid=1034CEFCC-A7A5-46D6-952E-7C5D9789D410712463infoc; b_nut=1642770212; buvid3=20BED23E-EDE5-4540-9453-6B5F47BC3099167631infoc; LIVE_BUVID=AUTO3816427702137797; buvid_fp_plain=undefined; i-wanna-go-back=-1; buvid_fp=acdac8c7907f1b99f05b71a1b773e700; hit-dyn-v2=1; CURRENT_BLACKGAP=0; blackside_state=0; nostalgia_conf=-1; buvid4=E60CB0B7-513E-D471-6962-E73CEC7E0ACE83627-022032917-551X6fL2lCmQIHshmlT9%2Fg%3D%3D; Hm_lvt_8a6e55dbd2870f0f5bc9194cddf32a02=1662114379,1662202313,1662203614,1662301134; fingerprint3=da9d28c5ffacb4b1e5c1efd45aa85575; DedeUserID=308823578; DedeUserID__ckMd5=5742ec96f34bc2e5; b_ut=5; CURRENT_QUALITY=80; hit-new-style-dyn=0; CURRENT_FNVAL=4048; bili_jct=f8d0693c436924202167c2f7fb6d20c2; fingerprint=d9b56d472c2acf8e0130852eb1b1c7c4; innersign=0; bp_video_offset_308823578=727552359014596600; PVID=1; b_lsid=E9BDB2F10_1846B98C221");
        try {
            //3.执行get请求，相当于在输入地址栏后敲回车键
            response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity httpEntity = response.getEntity();
                String html = EntityUtils.toString(httpEntity, "utf-8");
                Document document = Jsoup.parse(html);
                Elements elements = document.select("body > [id=app] > [class=popular-container] > [class=rank-container] > [class=rank-list-wrap] > [class=rank-list] > [class=rank-item] > [class=content] > [class=info] > a");
                int sum=1;
                for (Element element : elements) {
                    String httpFrom=element.attr("href");
                    String[] httpFrom_ = httpFrom.split("//");
                    httpFrom = httpFrom_[1];
                    String title = element.ownText();
                    System.out.println("排名：" + sum + " ，网址：https://" + httpFrom + " ,名字：" + title);
                    ++sum;

                }


            } else {
                //如果返回状态不是200，比如404（页面不存在）等，根据情况做处理，这里略
                System.out.println("返回状态不是200");
                System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //6.关闭
            HttpClientUtils.closeQuietly(response);
            HttpClientUtils.closeQuietly(httpClient);

        }
    }
    }


