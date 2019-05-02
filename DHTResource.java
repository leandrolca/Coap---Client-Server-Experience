/**
 * 
 */
package it.unipr.iotlab.iot2018.cf.server.resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;


/**
 * @author Leandro C. de Andrade
 *
 */
public class DHTResource extends CoapResource {
	private static String line = null;
	private static String[] data;
	static String humi = null;
	static String temp = null;
	static float humidity = 0;
	static float temperature = 0;
	private int mValue = 0;

	// create gpio controller
 	final GpioController gpio1 = GpioFactory.getInstance();

    // provision gpio pin #03 as an output pin and turn on
    final GpioPinDigitalOutput pin1 = gpio1.provisionDigitalOutputPin(RaspiPin.GPIO_03, "MyGreenLED", PinState.HIGH);
	
    // set shutdown state for this pin
   // pin1.setShutdownOptions(true, PinState.LOW);

    // create gpio controller
  	final GpioController gpio2 = GpioFactory.getInstance();

	
    // provision gpio pin #04 as an output pin and turn on
    final GpioPinDigitalOutput pin2 = gpio2.provisionDigitalOutputPin(RaspiPin.GPIO_04, "MyRedLED", PinState.HIGH);


	
	private class UpdateTask extends TimerTask {
		
		private CoapResource mCoapRes;
		
		public UpdateTask(CoapResource coapRes) {
			mCoapRes = coapRes;
		}
		
		@Override 
		public void run() { 
			mValue = new Random().nextInt(20);
			mCoapRes.changed();
			line = getTHSensorData();
			data=line.split("=");
		    temperature=Float.parseFloat(data[1]);
			humidity=Float.parseFloat(data[2]);
			System.out.println("Temperature is : "+temperature+" 'C Humidity is :"+ humidity+" %RH");
			try {
			temp = TemperatureControl();
			humi = HumidityControl();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		// private method to read sensor data using a python script
		private String getTHSensorData() {
			String sensorData = null;
			try {
				Runtime rt = Runtime.getRuntime();
				Process p = rt.exec("python /home/pi/Adafruit_Python_DHT/examples/DHT.py 11 4");
				final BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
				sensorData = bri.readLine();
				bri.close();
				p.waitFor();
			} catch(Exception e) {
				e.printStackTrace();
			}
			// This method returns NULL if no data is available (Fail reading data from DHT sensor)
		return sensorData;	
		}
		
		
		private String TemperatureControl() throws InterruptedException {
			
			String TemperatureControlState = null;
			System.out.println("<--Pi4J--> GPIO Temperature Control ... started.");
	        
			// set shutdown state for this pin
		    pin1.setShutdownOptions(true, PinState.LOW);
		    
	        if (20.0 < temperature && temperature < 40.0){
	        		pin1.high();
	        		TemperatureControlState = "Normal";
	        		System.out.println("Temperature State is: "+ TemperatureControlState);
	        	} else {
	        		if ((temperature < 20.0)||(temperature > 40.0)) {
	        			pin1.pulse(1000, true);
	        			//Thread.sleep(1000);
	        			if (temperature < 20) {
	        				TemperatureControlState = "Cold";
	        				System.out.println("Temperature State is: "+ TemperatureControlState);
	        			}
	        			if (temperature > 40) {
	        				TemperatureControlState = "Hot";
	        				System.out.println("Temperature State is: "+ TemperatureControlState);
	        			}
	        	    }
	        	}
		return TemperatureControlState;
		}
		
		private String HumidityControl()throws InterruptedException {
			String HumidityControlState = null;
			System.out.println("<--Pi4J--> GPIO Humidity Control ... started.");

			// set shutdown state for this pin
		    pin2.setShutdownOptions(true, PinState.LOW);
			
	        if (50.0 < humidity && humidity < 75.0){
	           		pin2.high();
	        		HumidityControlState = "Normal";
	        		System.out.println("Humidity State is: "+ HumidityControlState);
	         	} else {
	        		if ((temperature < 50.0)||(temperature > 75.0)) {
	        			pin2.pulse(1000, true);
	        			//Thread.sleep(1000);
	        			if (humidity < 50) {
	        				HumidityControlState = "dry";
	        				System.out.println("Humidity State is: "+ HumidityControlState);
	        			}
	        			if (humidity > 75) {
	        				HumidityControlState = "Humid";
	        				System.out.println("Humidity State is: "+ HumidityControlState);
	        			}
	        	    }
	        	}
		
		return HumidityControlState;	
		}
		
		
	}	
	//Constructor
	public DHTResource(String name) {
		super(name);
		Timer timer = new Timer();
		timer.schedule(new UpdateTask(this), 0, 10000);
	}
	@Override
	public void handleGET(CoapExchange exchange) {
		exchange.respond(ResponseCode.CONTENT,
				" The sensor reading is: "+ line + " ",
				MediaTypeRegistry.TEXT_PLAIN);
		exchange.respond(ResponseCode.CONTENT,
				"Temperature = " + temperature + " " + "Humidity = " + humidity + "",
				MediaTypeRegistry.TEXT_PLAIN);
		exchange.respond(ResponseCode.CONTENT,
				"Temperature state is = "+temp + " " + "Humidity State is: " + humi+"",
				MediaTypeRegistry.TEXT_PLAIN);
		
	}
}
