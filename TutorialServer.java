package it.unipr.iotlab.iot2018.cf.server;

import org.eclipse.californium.core.CoapServer;

import it.unipr.iotlab.iot2018.cf.server.resources.DHTResource;
import it.unipr.iotlab.iot2018.cf.server.resources.HelloWorldResource;
import it.unipr.iotlab.iot2018.cf.server.resources.ObservableResource;
import it.unipr.iotlab.iot2018.cf.server.resources.TemperatureHumidityResource;

public class TutorialServer extends CoapServer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		TutorialServer tutorialServer = new TutorialServer();
		
		HelloWorldResource hello = new HelloWorldResource("hello-world");
		tutorialServer.add(hello);
		
		ObservableResource obsRes = new ObservableResource("observable-resource");
		obsRes.setObservable(true);
		obsRes.getAttributes().setObservable();
		tutorialServer.add(obsRes);
		
	//	TemperatureHumidityResource temphumiRes = new TemperatureHumidityResource("temperature-humidity-resource");
	//	temphumiRes.setObservable(true);
	//	temphumiRes.getAttributes().setObservable();
	//	tutorialServer.add(temphumiRes);
		
		DHTResource DHTRes = new DHTResource("DHT-resource");
		DHTRes.setObservable(true);
		DHTRes.getAttributes().setObservable();
		tutorialServer.add(DHTRes);
		
		tutorialServer.start();
	}

}
