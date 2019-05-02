package it.unipr.iotlab.iot2018.cf.client;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.coap.CoAP.Code;

public class DHTSimpleClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CoapClient client = new CoapClient("coap://192.168.43.128:5683/DHT-resource");
		Request request = new Request(Code.GET);
		//Synchronously send the GET message (blocking call)
		CoapResponse coapResp = client.advanced(request);
		//The "CoapResponse" message contains the response.
		System.out.println(Utils.prettyPrint(coapResp));
		
		Request request1 = new Request(Code.GET);
		//Synchronously send the GET message (blocking call)
		CoapResponse coapResp1 = client.advanced(request1);
		//The "CoapResponse" message contains the response.
		System.out.println(Utils.prettyPrint(coapResp));
	}

}
