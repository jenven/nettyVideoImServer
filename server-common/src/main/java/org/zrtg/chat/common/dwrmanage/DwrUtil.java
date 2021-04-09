package org.zrtg.chat.common.dwrmanage;

import com.alibaba.fastjson.JSONArray;
import org.directwebremoting.Browser;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessionFilter;
import org.zrtg.chat.common.constant.Constants;
import org.zrtg.chat.common.model.proto.MessageBodyProto;
import org.zrtg.chat.common.model.proto.MessageProto;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DwrUtil
{
    /**
     * 发送给全部
     */
    public static void sedMessageToAll(MessageProto.Model  msgModel){
      
    	 
        try{
			  MessageBodyProto.MessageBody  content = MessageBodyProto.MessageBody.parseFrom(msgModel.getContent());
			  Map<String,Object> map = new HashMap<String,Object>();
			  map.put("user", Constants.DWRConfig.JSONFORMAT.printToString(msgModel));
			  map.put("content", Constants.DWRConfig.JSONFORMAT.printToString(content));
			  final Object msg = JSONArray.toJSON(map);
			  Browser.withAllSessions(new Runnable(){ 
		            private ScriptBuffer script = new ScriptBuffer(); 
		            @Override
                    public void run(){
		            	script.appendCall(Constants.DWRConfig.DWR_SCRIPT_FUNCTIONNAME, msg);  
		                Collection<ScriptSession> sessions = Browser.getTargetSessions(); 
		                for (ScriptSession scriptSession : sessions){ 
		                    scriptSession.addScript(script); 
		                } 
		            } 
		       });
		  }catch(Exception e){
			  
		  } 
    } 
    /**
     * 发送给个人
     */
    public static void sendMessageToUser(String userid, String message) {  
        final String sessionid = userid;  
        final String msg = message;  
        final String attributeName = Constants.SessionConfig.SESSION_KEY;  
        Browser.withAllSessionsFiltered(new ScriptSessionFilter() {  
            @Override
            public boolean match(ScriptSession session) {
                if (session.getAttribute(attributeName) == null) {
                    return false;
                } else {
                    boolean f = session.getAttribute(attributeName).equals(sessionid);  
                    return f;  
                }  
            }  
        }, new Runnable() {  
            private ScriptBuffer script = new ScriptBuffer();  
            @Override
            public void run() {
                script.appendCall(Constants.DWRConfig.DWR_SCRIPT_FUNCTIONNAME, msg);  
                Collection<ScriptSession> sessions = Browser.getTargetSessions();  
                for (ScriptSession scriptSession : sessions) {  
                    scriptSession.addScript(script);  
                }   
            }   
        });  
  
    }  
    
}