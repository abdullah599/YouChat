package com.example.YouChat

class Message {
	 var message:String="None"
	 var sender_id:String="null"

	constructor(){}
	constructor( msg:String, sid:String){
		message=msg
		sender_id=sid
	}

}