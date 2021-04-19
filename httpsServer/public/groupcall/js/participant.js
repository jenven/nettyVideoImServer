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

const PARTICIPANT_MAIN_CLASS = 'participant main';
const PARTICIPANT_CLASS = 'participant';

/**
 * Creates a video element for a new participant
 *
 * @param {String} name - the name of the new participant, to be used as tag
 *                        name of the video element.
 *                        The tag of the new element will be 'video<name>'
 * @return
 */
function Participant(name) {
	this.name = name;
	var container = document.createElement('div');
	container.className = isPresentMainParticipant() ? PARTICIPANT_CLASS : PARTICIPANT_MAIN_CLASS;
	container.id = name;
	var span = document.createElement('span');
	var video = document.createElement('video');
	var rtcPeer;

	container.appendChild(video);
	container.appendChild(span);
	container.onclick = switchContainerClass;
	document.getElementById('participants').appendChild(container);

	span.appendChild(document.createTextNode(name));

	video.id = 'video-' + name;
	video.autoplay = true;
	video.controls = false;


	this.getElement = function() {
		return container;
	}

	this.getVideoElement = function() {
		return video;
	}

	function switchContainerClass() {
		if (container.className === PARTICIPANT_CLASS) {
			var elements = Array.prototype.slice.call(document.getElementsByClassName(PARTICIPANT_MAIN_CLASS));
			elements.forEach(function(item) {
					item.className = PARTICIPANT_CLASS;
				});

				container.className = PARTICIPANT_MAIN_CLASS;
			} else {
			container.className = PARTICIPANT_CLASS;
		}
	}

	function isPresentMainParticipant() {
		return ((document.getElementsByClassName(PARTICIPANT_MAIN_CLASS)).length != 0);
	}

	this.offerToReceiveVideo = function(error, offerSdp, wp){
		if (error) return console.error ("sdp offer error")
		console.log('Invoking SDP offer callback function');
		var message = new proto.Model();
		var messageRoom = new proto.MessageRoom();

		message.setMsgtype(5);//房间消息
		message.setCmd(8);//RECEIVEVIDEOFROM 接收视频信息
		message.setGroupid("0");//系统用户组
		message.setToken(name);
		message.setSender(name);
		messageRoom.setExtend(offerSdp);
		message.setContent(messageRoom.serializeBinary());

		sendMessage(message);
	}


	this.onIceCandidate = function (candidate, wp) {
		console.log("Local candidate:" + JSON.stringify(candidate));

		var message = new proto.Model();
		var messageCandidate = new proto.MessageCandidate();

		message.setMsgtype(5);//房间消息
		message.setCmd(10);//ONICECANDIDATE 媒体协商
		message.setGroupid("0");//系统用户组
		message.setToken(name);
		message.setSender(name);
		messageCandidate.setCandidate(candidate.candidate);
		messageCandidate.setSdpmid(candidate.sdpMid);
		messageCandidate.setSdpmlineindex(candidate.sdpMLineIndex);

		message.setContent(messageCandidate.serializeBinary());


		sendMessage(message);
	}

	Object.defineProperty(this, 'rtcPeer', { writable: true});

	this.dispose = function() {
		console.log('Disposing participant ' + this.name);
		this.rtcPeer.dispose();
		container.parentNode.removeChild(container);
	};
}
