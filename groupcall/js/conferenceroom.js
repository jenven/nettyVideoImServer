/*
 * (C) Copyright 2014 Kurento (http://kurento.org/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

var ws = new WebSocket('ws://192.168.10.10:1314/ws');
ws.binaryType = "arraybuffer";
var participants = {};
var name;
var room;
var currentsession = "";

window.onbeforeunload = function() {
	ws.close();
};

ws.onopen = function(e) {
	console.log("连接打开");
	var message = new proto.Model();
	var browser=BrowserUtil.info();
	message.setVersion("1.0");
	message.setDeviceid("");
	message.setCmd(1);
	message.setSender(currentsession);
	message.setMsgtype(1);
	message.setFlag(1);
	message.setPlatform(browser.name);
	message.setPlatformversion(browser.version);
	message.setToken(currentsession);
	var bytes = message.serializeBinary();
	//CHAT.socket.send("[LOGIN][" + new Date().getTime() +"][" + nickname + "][WebSocket]");
	ws.send(bytes);
};

ws.onmessage = function(e) {

	if (e.data instanceof ArrayBuffer){
		var msg =  proto.Model.deserializeBinary(e.data);
		console.log(msg);
		//心跳消息
		if(msg.getCmd()==2){
			console.log("响应心跳");
			//发送心跳回应
			var message1 = new proto.Model();
			message1.setCmd(2);
			message1.setMsgtype(4);
			ws.send(message1.serializeBinary());
		}else if(msg.getCmd()==3){
			if (msg.getSender() == msg.getReceiver()){
				currentsession = msg.getSender();

			}
			if(msg.getSender()!=currentsession){

			}
		}else if(msg.getCmd()==4){
			if(msg.getSender()!=currentsession){

			}
		}else if(msg.getCmd()==5){
			var msgCon =  proto.MessageBody.deserializeBinary(msg.getContent());
			if(msgCon.getType() === 0){//文本消息
				console.log(msgCon);
				var isme = (msg.getSender() == currentsession) ? true : false;
				var name = msgCon.getNickname();
				var label = (isme ? '' : ('<span class="label">' + name +'</span>'));
				var content = msgCon.getContent();
				var contentDiv = '<div>' + label + '<span class="content">' + content + '</span></div>';
				var photoUrl = msgCon.getAvatar();
				var photoDiv = '<span><img class="photo" src="' + photoUrl + '"/></span>';
				var section = document.createElement('section');
				if (isme) {
					section.className = 'user';
					section.innerHTML = contentDiv + photoDiv;
				} else {
					section.className = 'service';
					section.innerHTML = photoDiv + contentDiv;
				}
				$("#onlinemsg").append(section);

			}else if(msgCon.getType() === 1){//文件

			}else if(msgCon.getType() === 2){//纯表情

			}else if(msgCon.getType() === 3){//鲜花

				if (msgCon.getNickname() == CHAT.nickname){
					addSystemTip(msgCon.getContent().replace(CHAT.nickname,"你"));
				}else{
					addSystemTip(msgCon.getContent());
				}
				//鲜花特效
				$(document).snowfall('clear');
				$(document).snowfall({
					image:"images/face/50.gif",
					flakeCount:60,
					minSize:20,
					maxSize:40
				});
				window.flowerTimer = window.setTimeout(function(){
					$(document).snowfall('clear');
					window.clearTimeout(flowerTimer);
				},5000);
			}
		}


	}else{
		console.log(e.data);
	}
	return;
	var parsedMessage = JSON.parse(message.data);
	console.info('Received message: ' + message.data);

	switch (parsedMessage.id) {
	case 'existingParticipants':
		onExistingParticipants(parsedMessage);
		break;
	case 'newParticipantArrived':
		onNewParticipant(parsedMessage);
		break;
	case 'participantLeft':
		onParticipantLeft(parsedMessage);
		break;
	case 'receiveVideoAnswer':
		receiveVideoResponse(parsedMessage);
		break;
	case 'iceCandidate':
		participants[parsedMessage.name].rtcPeer.addIceCandidate(parsedMessage.candidate, function (error) {
	        if (error) {
		      console.error("Error adding candidate: " + error);
		      return;
	        }
	    });
	    break;
	default:
		console.error('Unrecognized message', parsedMessage);
	}
}

function register() {
	name = document.getElementById('name').value;
	room = document.getElementById('roomName').value;

	document.getElementById('room-header').innerText = 'ROOM ' + room;
	document.getElementById('join').style.display = 'none';
	document.getElementById('room').style.display = 'block';
	if (currentsession === ""){
		console.log("缺少session id");
		return;
	}

	var message = new proto.Model();
	var roomComent = new proto.MessageRoom();

	message.setMsgtype(5);//房间消息
	message.setCmd(7);//加入房间消息
	message.setGroupid("0");//系统用户组
	message.setToken(currentsession);
	message.setSender(currentsession);
	roomComent.setRoomid(room);
	roomComent.setExtend(name+"加入房间!");
	roomComent.setType(0);
	roomComent.setUsername(name);
	roomComent.setNickname(name);
	message.setContent(roomComent.serializeBinary());

	sendMessage(message);
}

function onNewParticipant(request) {
	receiveVideo(request.name);
}

function receiveVideoResponse(result) {
	participants[result.name].rtcPeer.processAnswer (result.sdpAnswer, function (error) {
		if (error) return console.error (error);
	});
}

function callResponse(message) {
	if (message.response != 'accepted') {
		console.info('Call not accepted by peer. Closing call');
		stop();
	} else {
		webRtcPeer.processAnswer(message.sdpAnswer, function (error) {
			if (error) return console.error (error);
		});
	}
}

function onExistingParticipants(msg) {
	var constraints = {
		audio : true,
		video : {
			mandatory : {
				maxWidth : 320,
				maxFrameRate : 15,
				minFrameRate : 15
			}
		}
	};
	console.log(name + " registered in room " + room);
	var participant = new Participant(name);
	participants[name] = participant;
	var video = participant.getVideoElement();

	var options = {
	      localVideo: video,
	      mediaConstraints: constraints,
	      onicecandidate: participant.onIceCandidate.bind(participant)
	    }
	participant.rtcPeer = new kurentoUtils.WebRtcPeer.WebRtcPeerSendonly(options,
		function (error) {
		  if(error) {
			  return console.error(error);
		  }
		  this.generateOffer (participant.offerToReceiveVideo.bind(participant));
	});

	msg.data.forEach(receiveVideo);
}

function leaveRoom() {
	sendMessage({
		id : 'leaveRoom'
	});

	for ( var key in participants) {
		participants[key].dispose();
	}

	document.getElementById('join').style.display = 'block';
	document.getElementById('room').style.display = 'none';

	ws.close();
}

function receiveVideo(sender) {
	var participant = new Participant(sender);
	participants[sender] = participant;
	var video = participant.getVideoElement();

	var options = {
      remoteVideo: video,
      onicecandidate: participant.onIceCandidate.bind(participant)
    }

	participant.rtcPeer = new kurentoUtils.WebRtcPeer.WebRtcPeerRecvonly(options,
			function (error) {
			  if(error) {
				  return console.error(error);
			  }
			  this.generateOffer (participant.offerToReceiveVideo.bind(participant));
	});;
}

function onParticipantLeft(request) {
	console.log('Participant ' + request.name + ' left');
	var participant = participants[request.name];
	participant.dispose();
	delete participants[request.name];
}

function sendMessage(message) {
	console.log(currentsession);
	ws.send(message.serializeBinary());
}
