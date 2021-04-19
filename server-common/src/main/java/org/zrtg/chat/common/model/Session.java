package org.zrtg.chat.common.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import com.google.gson.JsonObject;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.kurento.client.*;
import org.kurento.jsonrpc.JsonUtils;
import org.zrtg.chat.common.constant.Constants;
import org.zrtg.chat.common.model.proto.MessageBodyProto;
import org.zrtg.chat.common.model.proto.MessageCandidateProto;
import org.zrtg.chat.common.model.proto.MessageProto;
import org.zrtg.chat.common.model.proto.MessageRoomProto;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
@Slf4j
public class Session   implements Serializable{

 
 
 
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 8269505210699191257L;
	private transient Channel session;
	private ScriptSession dwrsession;
	 
	private String nid;//session在本台服务器上的ID
	private int source;//来源 用于区分是websocket\socket\dwr
	private String deviceId;//客户端ID  (设备号码+应用包名),ios为devicetoken
	private String host;//session绑定的服务器IP
	private String account;//session绑定的账号
	private String platform;//终端类型  
	private String platformVersion;//终端版本号
	private String appKey;//客户端key
	private Long bindTime;//登录时间
	private Long updateTime;//更新时间
	private String sign;//签名
	private Double longitude;//经度
	private Double latitude;//维度
	private String location;//位置
	private int status;// 状态

	private  MediaPipeline pipeline;

	private  WebRtcEndpoint outgoingMedia;

	private  ConcurrentMap<String, WebRtcEndpoint> incomingMedia = new ConcurrentHashMap<>();

	public MediaPipeline getPipeline()
	{
		return pipeline;
	}

	public void setPipeline(MediaPipeline pipeline)
	{
		this.pipeline = pipeline;
		this.outgoingMedia =  new WebRtcEndpoint.Builder(pipeline).build();
		this.outgoingMedia.addIceCandidateFoundListener(new EventListener<IceCandidateFoundEvent>() {

			@Override
			public void onEvent(IceCandidateFoundEvent event) {
				log.info("通知页面iceCandidate:{}",account);

				MessageProto.Model.Builder builder = MessageProto.Model.newBuilder();
				builder.setCmd(Constants.CmdType.ONICECANDIDATE);
				builder.setSender(account);
				builder.setReceiver(account);
				builder.setMsgtype(Constants.ProtobufType.GROUP_CALL);
				builder.setToken(account);

				MessageCandidateProto.MessageCandidate.Builder candidate = MessageCandidateProto.MessageCandidate.newBuilder();
				candidate.setCandidate(event.getCandidate().getCandidate());
				candidate.setSdpMid(event.getCandidate().getSdpMid());
				candidate.setSdpMLineIndex(event.getCandidate().getSdpMLineIndex());

				builder.setContent(candidate.build().toByteString());
				synchronized (session) {
					session.write(builder);
				}
			}
		});
	}

	public WebRtcEndpoint getOutgoingMedia()
	{
		return outgoingMedia;
	}



	public ConcurrentMap<String, WebRtcEndpoint> getIncomingMedia()
	{
		return incomingMedia;
	}



	private Map<String,Session> sessions = new HashMap<String,Session>(); //用于dwr websocket存储多开页面创建的session

	public void addSessions(Session session){
		sessions.put(session.getNid(), session);
	}
	 
	
	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
		setAttribute("updateTime", updateTime);
	}


	public Session(ScriptSession session) {
		this.dwrsession = session;
		this.nid = session.getId();
	}
	 
	public Session(Channel session) {
		this.session = session;
		this.nid = session.id().asShortText();
	}
 
	public Session()
	{
		
	}
	
	 
	public ScriptSession getDwrsession() {
		return dwrsession;
	}


	public void setDwrsession(ScriptSession dwrsession) {
		this.dwrsession = dwrsession;
	}


	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
		setAttribute(Constants.SessionConfig.SESSION_KEY, account);
	}
 

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		setAttribute("longitude", longitude);
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		setAttribute("latitude", latitude);
		this.latitude = latitude;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		setAttribute("location", location);
		this.location = location;
	}

 

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	public String getDeviceId() {
		return deviceId;
	}
 
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
		
		setAttribute("deviceId", deviceId);
	}


   

	public String getHost() {
		return host;
	}



	public Long getBindTime() {
		return bindTime;
	}

	public void setBindTime(Long bindTime) {
		this.bindTime = bindTime;
	    setAttribute("bindTime", bindTime);
	}
 

	public void setHost(String host) {
		this.host = host;
		 
		setAttribute("host", host);
	}

	public void setChannel(Channel session) {
		this.session = session;
	}
 
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
		setAttribute("status", status);
	}


	
	
	public Channel getSession() {
		return session;
	}
	
	public List<Channel> getSessionAll(){
		  List<Channel>  list= new ArrayList<Channel>();
		  list.add(getSession());
		  for (String key : sessions.keySet()) {
				Session  session = sessions.get(key);
				if(session!=null){
					list.add(session.getSession()); 
				}
		  }
		  return list;
	}

	public void setSession(Channel session) {
		this.session = session;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
		setAttribute("platform", platform);
	}

	public String getPlatformVersion() {
		return platformVersion;
	}

	public void setPlatformVersion(String platformVersion) {
		this.platformVersion = platformVersion;
		setAttribute("platformVersion", platformVersion);
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
		setAttribute("appKey", appKey);
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
		setAttribute("sign", sign);
	}
	
	

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
		setAttribute("source", source);
	}

	public void setAttribute(String key, Object value) {
		if(session!=null){
			session.attr(AttributeKey.valueOf(key)).set(value);
		}
	}


	public boolean containsAttribute(String key) {
		if(session!=null){
			return session.hasAttr(AttributeKey.valueOf(key));
		}
		return false;
	}
	
	public Object getAttribute(String key) {
		if(session!=null)
		{
			return session.attr(AttributeKey.valueOf(key)).get();
		}
		return null;
	}

	public void removeAttribute(String key) {
		if(session!=null)
		{
			session.attr(AttributeKey.valueOf(key)).set(null);
		}
	}

	public SocketAddress getRemoteAddress() {
		if(session!=null)
		{
			return session.remoteAddress();
		}
		return null;
	}

	public  boolean write(Object msg) {
		if(sessions.size()>0){
			for (String key : sessions.keySet()) {
				Session  session = sessions.get(key);
				if(session!=null){
					session.write(msg);
				}
			}
		} 
		if(session!=null&&isConnected())
		{
			return session.writeAndFlush(msg).awaitUninterruptibly(5000);
		}else if(dwrsession!=null&&isConnected()){
			try{
				MessageProto.Model  msgModel = (MessageProto.Model)msg;
				MessageBodyProto.MessageBody  content = MessageBodyProto.MessageBody.parseFrom(msgModel.getContent());
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("user", Constants.DWRConfig.JSONFORMAT.printToString(msgModel));
				map.put("content", Constants.DWRConfig.JSONFORMAT.printToString(content));
				ScriptBuffer script = new ScriptBuffer();  
				script.appendCall(Constants.DWRConfig.DWR_SCRIPT_FUNCTIONNAME, JSONArray.toJSON(map));  
				dwrsession.addScript(script);
				//protobuf 不支持低版本IE 没法使用protobuf  支持可以使用以下方法
				/*MessageProto.Model  msgModel = (MessageProto.Model)msg;
				ScriptBuffer script = new ScriptBuffer();  
				Object json = JSONArray.toJSON(msgModel.toByteArray());
				script.appendCall(Constants.DWRConfig.DWR_SCRIPT_FUNCTIONNAME, json);  
				dwrsession.addScript(script); */
				return  true; 
			}catch(Exception e){
				
			}
		} 
		return false;
	}

	
	public boolean isConnected() {
		//判断是否有多个session
		if(sessions!=null&&sessions.size()>0){
			for (String key : sessions.keySet()) {
				Session  session = sessions.get(key);
				if(session!=null && session.isConnected()){
					return true;
				}
			}
		}else if(session != null){
			return session.isActive();
		}else if(dwrsession!=null){
			return !dwrsession.isInvalidated();
		}
		return false;
	}

	public boolean  isLocalhost()
	{
		
		try {
			String ip = InetAddress.getLocalHost().getHostAddress();
			return ip.equals(host);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return false;
		 
	}
	
	
	public int  otherSessionSize()
	{
		return sessions.size();
	}
	
 
	public void close() {

		log.info("PARTICIPANT {}: Releasing resources", this.account);
		for (final String remoteParticipantName : incomingMedia.keySet()) {

			log.info("PARTICIPANT {}: Released incoming EP for {}", this.account, remoteParticipantName);

			final WebRtcEndpoint ep = this.incomingMedia.get(remoteParticipantName);

			ep.release(new Continuation<Void>() {

				@Override
				public void onSuccess(Void result) throws Exception {
					log.info("PARTICIPANT Released successfully incoming EP for {}",
							remoteParticipantName);
				}

				@Override
				public void onError(Throwable cause) throws Exception {
					log.info("PARTICIPANT  Could not release incoming EP for {}",
							remoteParticipantName);
				}
			});
		}

		outgoingMedia.release(new Continuation<Void>() {

			@Override
			public void onSuccess(Void result) throws Exception {
				log.info("PARTICIPANT Released outgoing EP");
			}

			@Override
			public void onError(Throwable cause) throws Exception {
				log.info("USER  Could not release outgoing EP");
			}
		});

		if(session!=null){
			session.close();
		}else if(dwrsession!=null){
			dwrsession.invalidate();
		}
	}
	
	public void closeAll() {
		close();
		for (String key : sessions.keySet()) {
			Session  session = sessions.get(key);
			if(session!=null){
				session.close();
				sessions.remove(key);
			}
		}
	}

	public void close(String nid) {
		if(getNid().equals(nid)){
			close();
		}else{
			Session  session = sessions.get(nid);
			sessions.remove(nid);
			session.close();
		} 
	}
	
	
	@Override
	public int hashCode(){
		
		return (deviceId + nid + host).hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
        
		if(o instanceof Session){
			return hashCode() == o.hashCode();
		}
		return false;
	}

    public boolean fromOtherDevice(Object o) {
        
		if (o instanceof Session) {
			
			Session t = (Session) o;
			if(t.deviceId!=null && deviceId!=null)
			{
				return !t.deviceId.equals(deviceId);
			} 
		}  
		return false;
	}

    public boolean fromCurrentDevice(Object o) {
        
		return !fromOtherDevice(o);
	}

	public void receiveVideoFrom(Session sender, String sdpOffer) throws IOException {

		log.info("USER {}: connecting with {} ", account, sender.getAccount());

		log.info("USER {}: SdpOffer for {} is {}", account,sender.getAccount(), sdpOffer);

		final String ipSdpAnswer = this.getEndpointForUser(sender).processOffer(sdpOffer);

		MessageProto.Model.Builder builder = MessageProto.Model.newBuilder();
		builder.setCmd(Constants.CmdType.RECEIVEVIDEOANSWER);
		builder.setSender(sender.getAccount());
		builder.setReceiver(account);
		builder.setMsgtype(Constants.ProtobufType.GROUP_CALL);
		builder.setToken(account);
		MessageRoomProto.MessageRoom.Builder messageBody = MessageRoomProto.MessageRoom.newBuilder();
		messageBody.setExtend(ipSdpAnswer);
		builder.setContent(messageBody.build().toByteString());


		log.info("USER {}: SdpAnswerfor {} is {}", account, sender.getAccount(),ipSdpAnswer);
		this.write(builder);
		log.info("gather candidates");
		this.getEndpointForUser(sender).gatherCandidates();
	}

	public WebRtcEndpoint getOutgoingWebRtcPeer() {
		return outgoingMedia;
	}

	private WebRtcEndpoint getEndpointForUser(final Session sender) {
		if (sender.getAccount().equals(this.account)) {
			log.info("PARTICIPANT {}: configuring loopback", this.account);
			return outgoingMedia;
		}

		log.info("PARTICIPANT {}: receiving video from {}", this.account, sender.getAccount());

		WebRtcEndpoint incoming = incomingMedia.get(sender.getAccount());
		if (incoming == null) {
			log.info("PARTICIPANT {}: creating new endpoint for {}", this.account, sender.getAccount());
			incoming = new WebRtcEndpoint.Builder(pipeline).build();

			incoming.addIceCandidateFoundListener(new EventListener<IceCandidateFoundEvent>() {

				@Override
				public void onEvent(IceCandidateFoundEvent event) {

					MessageProto.Model.Builder builder = MessageProto.Model.newBuilder();
					builder.setCmd(Constants.CmdType.ONICECANDIDATE);
					builder.setSender(sender.getAccount());
					builder.setReceiver(account);
					builder.setMsgtype(Constants.ProtobufType.GROUP_CALL);
					builder.setToken(account);

					MessageCandidateProto.MessageCandidate.Builder candidate = MessageCandidateProto.MessageCandidate.newBuilder();
					candidate.setCandidate(event.getCandidate().getCandidate());
					candidate.setSdpMid(event.getCandidate().getSdpMid());
					candidate.setSdpMLineIndex(event.getCandidate().getSdpMLineIndex());

					builder.setContent(candidate.build().toByteString());

					synchronized (session) {
						session.write(builder);
					}
				}
			});

			incomingMedia.put(sender.getAccount(), incoming);
		}
		log.info("PARTICIPANT {}: obtained endpoint for {}", this.account, sender.getAccount());
		sender.getOutgoingWebRtcPeer().connect(incoming);

		return incoming;
	}

	public void cancelVideoFrom(final Session sender) {
		this.cancelVideoFrom(sender.getAccount());
	}

	public void cancelVideoFrom(final String sessionid) {

		log.info("PARTICIPANT {}: canceling video reception from {}", this.account, sessionid);
		final WebRtcEndpoint incoming = incomingMedia.remove(sessionid);

		log.info("PARTICIPANT {}: removing endpoint endpoint for {}",  this.account, sessionid);
		incoming.release(new Continuation<Void>() {
			@Override
			public void onSuccess(Void result) throws Exception {
				log.info("PARTICIPANT Released successfully incoming EP for {}", sessionid);
			}

			@Override
			public void onError(Throwable cause) throws Exception {
				log.info("PARTICIPANT Could not release incoming  EP for {}",
						sessionid);
			}
		});
	}


	public void addCandidate(IceCandidate candidate, String sessionid) {

		if (this.account.compareTo(sessionid) == 0) {
			log.info("outgoingMedia:{}",sessionid);
			outgoingMedia.addIceCandidate(candidate);
		} else {
			log.info("incomingMedia:{}",sessionid);
			WebRtcEndpoint webRtc = incomingMedia.get(sessionid);
			if (webRtc != null) {
				webRtc.addIceCandidate(candidate);
			}
		}
	}

	@Override
	public String  toString(){
		return  JSON.toJSONString(Session.this);
	}


	
}