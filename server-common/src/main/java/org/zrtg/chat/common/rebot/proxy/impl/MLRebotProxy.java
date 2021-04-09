package org.zrtg.chat.common.rebot.proxy.impl;
/**
 * 茉莉机器人回复
 */


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
import org.zrtg.chat.common.rebot.proxy.RebotProxy;

import java.util.Date;

@Service
public class MLRebotProxy implements RebotProxy
{
	private final static Logger log = LoggerFactory.getLogger(MLRebotProxy.class);
	private String apiUrl = "http://i.itpk.cn/api.php";//机器人apiurl
	private String key="";//秘钥    请自行申请机器人KEY
	private String secret="";

	@Override
	public MessageWrapper botMessageReply(String user, String content) {
		log.info("MLRebot reply user -->"+user +"--mes:"+content);
		String message = "";
		 try{
			    //只实现了基础的  具体的请自行修改
			    Document doc = Jsoup.connect(apiUrl).timeout(62000).data("api_key",key).data("api_secret",secret).data("limit","5").data("question",content).post();
				message = doc.select("body").html(); 
			}catch(Exception e){
				e.printStackTrace();
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
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}
	  
}
