package org.zrtg.chat.common.rebot.proxy.impl;
/**
 * 图灵机器人回复
 */

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.time.DateFormatUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.zrtg.chat.common.constant.Constants;
import org.zrtg.chat.common.model.MessageWrapper;
import org.zrtg.chat.common.model.proto.MessageBodyProto;
import org.zrtg.chat.common.model.proto.MessageProto;
import org.zrtg.chat.common.rebot.model.RobotMessage;
import org.zrtg.chat.common.rebot.model.RobotMessageArticle;
import org.zrtg.chat.common.rebot.proxy.RebotProxy;

import java.util.Date;
import java.util.List;

//@Service
public class TLRebotProxy implements RebotProxy
{
	  private final static Logger log = LoggerFactory.getLogger(TLRebotProxy.class);
	private String apiUrl = "http://www.tuling123.com/openapi/api";//图灵机器人apiurl
	private String key;//秘钥    请自行申请图灵机器人KEY

	@Override
	public MessageWrapper botMessageReply(String user, String content) {
		log.info("TLRebot reply user -->"+user +"--mes:"+content);
		String message = "";
		 try{
				Document doc = Jsoup.connect(apiUrl).timeout(62000).data("key",key).data("userid",user).data("info",content).post();
				String msgStr = doc.select("body").html().replace("&quot;", "'");
					   msgStr = msgStr.replace("\r\n", "<br>");
					   msgStr = msgStr.replace("<br />", "") ;
				RobotMessage msg=	JSON.parseObject(msgStr,RobotMessage.class);
				switch (msg.getCode()) {
				case 100000:
					message = msg.getText();
					break; 
				case 200000:
					message = msg.getText() +"<a href='"+msg.getUrl()+"' target='_blank;'>"+msg.getUrl()+"</a></br>";
					break;
				case 302000:
					List<RobotMessageArticle>  sublist = JSON.parseArray(msg.getList(), RobotMessageArticle.class);
					
					for(RobotMessageArticle a :sublist){
						 //message+="a("+a.getDetailurl()+")["+a.getArticle()+"] <br><br>"; //layim聊天窗口回复内容
						message+= "<a href='"+a.getDetailurl()+"' target='_blank;'>"+a.getArticle()+"</a></br>";
					}
					break;
				case 308000:
					List<RobotMessageArticle>  sublistOne = JSON.parseArray(msg.getList(), RobotMessageArticle.class);
					for(RobotMessageArticle a :sublistOne){
						//message+="菜名："+a.getName()+"<br>用料："+a.getInfo()+"  <br>详情地址:a("+a.getDetailurl()+")["+a.getDetailurl()+"]      <br><br>";//layim聊天窗口回复内容
						message+="菜名："+a.getName()+"<br>用料："+a.getInfo()+"  <br>详情地址:<a href='"+a.getDetailurl()+"' target='_blank;'>"+a.getDetailurl()+"</a>      </br>";
					}
				break;
				default:
					break;
				} 
				 
				MessageProto.Model.Builder  result = MessageProto.Model.newBuilder();
				 result.setCmd(Constants.CmdType.MESSAGE);
				 result.setMsgtype(Constants.ProtobufType.REPLY);
				 result.setSender(Constants.ImserverConfig.REBOT_SESSIONID);//机器人ID
				 result.setReceiver(user);//回复人
				 result.setTimeStamp(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
				 MessageBodyProto.MessageBody.Builder  msgbody =  MessageBodyProto.MessageBody.newBuilder();
				 msgbody.setContent(message); 
			     result.setContent(msgbody.build().toByteString());
			     return new MessageWrapper(MessageWrapper.MessageProtocol.REPLY, Constants.ImserverConfig.REBOT_SESSIONID, user,result.build());
			}catch(Exception e){
				e.printStackTrace();
			}
		   return new MessageWrapper(MessageWrapper.MessageProtocol.REPLY, Constants.ImserverConfig.REBOT_SESSIONID, user,null);
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public void setKey(String key) {
		this.key = key;
	}
	  
}
