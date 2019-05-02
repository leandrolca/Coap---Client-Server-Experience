package it.unipr.iotlab.iot2018.cf.client;

import java.util.Timer;
import java.util.TimerTask;


import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResource;
//import org.eclipse.californium.core.CoapResource;
//import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
//import org.eclipse.californium.core.Utils;
//import org.eclipse.californium.core.WebLink;
//import org.eclipse.californium.core.coap.LinkFormat;
//import org.eclipse.californium.core.coap.MediaTypeRegistry;
//import org.eclipse.californium.core.coap.Request;
//import org.eclipse.californium.core.network.EndpointManager;
//import org.eclipse.californium.core.network.config.NetworkConfig;
//import org.eclipse.californium.core.network.interceptors.MessageTracer;



public class TempHumiClient extends CoapClient {

	public static void main(String[] args) {
		// Classe principal
		CoapClient client = new CoapClient("coap://192.168.43.128:5683/DHT-resource");
		System.out.println("===============\nCO01+06");
		System.out.println("---------------\nGET /obs with Observe");
		
		CoapObserveRelation relation1 = client.observe(
				new CoapHandler() {
					@Override 
					public void onLoad(CoapResponse response) {
						String content = response.getResponseText();
						System.out.println("-CO01----------");
						System.out.println(content);
					}
					
					@Override public void onError() {
						System.err.println("-Failed--------");
					}
				}
		);
		
		
		
		//run();
		try { Thread.sleep(600*1000); } catch (InterruptedException e) { }
		System.out.println("---------------\nCancel Observe");
		relation1.reactiveCancel();
		try { Thread.sleep(6*1000); } catch (InterruptedException e) { }
	
	
	}

		
}