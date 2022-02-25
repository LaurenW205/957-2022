package frc.robot;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Timer;

import edu.wpi.first.wpilibj.RobotController;

import edu.wpi.first.wpilibj.AnalogInput;

import edu.wpi.first.wpilibj.DigitalInput;

public class Bling {

  DigitalInput BlingSwitch = new DigitalInput(4);

  private SerialPort arduino1 = null;
  boolean a1 = false;
  private Timer timer;
  private Timer timer2;

  double volt = 0;

  String rangefinderVoltString = new String();
  String alliance_partners = "2," + "997" + ',' + "955" + ',';

  public void connect(){

    //Connect to arduino on kUSB (just the one USB. If that doesn't work then something much bigger is going wrong)
    try{
      if(!a1){
        arduino1 = new SerialPort(9600, SerialPort.Port.kUSB);
        arduino1.setTimeout(1);
        a1 = true;
        System.out.println("Connected on kUSB");
      }
    }catch(Exception e){
      System.out.println("Failed to connect on kUSB, trying kUSB 1");
    }

    //Timer for voltage sending
    timer = new Timer();
    timer.start();

    //Timer for alliance sending
    timer2 = new Timer();
    timer2.start();

  }

  //Just send the alliance
  public void sendAlliance(String ap1, String ap2){
    arduino1.writeString("2," + ap1 + ',' + ap2 + ',');
  }

  //Run everything, constantly run in RobotPeriodic
  public void tick(String ap1, String ap2){

    if((ap1.length() < 5) && (ap2.length() < 5)){
      //Send the 2 Ally #s every 10 seconds, as entered in SmartDashboard
      if(timer2.get() > 10){
        arduino1.writeString("2," + ap1 + ',' + ap2 + ',');
        timer2.reset();
      }
    }
  

    
      //Run if it has been at least 2 seconds since the last time voltage was sent (to avoid overfilling the buffer)
      if(timer.get() > 2){
        //Check if switch is pressed, and send battery voltage if yes
        if(BlingSwitch.get()){
          double volt = RobotController.getBatteryVoltage(); //Store raw voltage in a double
          arduino1.writeString(String.format("%.5g%n", volt)); //Limit voltage to 5 characters and send
          timer.reset();
          System.out.println("volts sent"); //Make sure it works
        }
      }
    
    

    //Clear the timer every minute, slightly faster response time
    if(timer.get() > 60){
      timer.reset();
    }
      
  }
  
}
