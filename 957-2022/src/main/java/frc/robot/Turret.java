package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;


 

public class Turret {
    
   CANSparkMax turret = new CANSparkMax(10,MotorType.kBrushless);
   SparkMaxPIDController pid;
   RelativeEncoder encoder;
   Joystick controller = new Joystick(0);
   double time = 0;
   double speed = 0.25;


   double tx0 = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx0").getDouble(0);  // three target points
   double tx1 = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx1").getDouble(0);
   double tx2 = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx2").getDouble(0);
   double ta0 = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta0").getDouble(0);
   double ta1 = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta1").getDouble(0);
   double ta2 = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta2").getDouble(0);

   

   double slot1 = 0;
   double slot2 = 0;
   double slot3 = 0;
   

   
  double turn = 0;
  double oldTurn = 0;

  double tx = (tx0 + tx1 + tx2)/3.00001 * 27.5;    // find average of the three points on vision target
  
  
  /*if(tx0 != 1000 &&  tx1 != 1000 && tx2 != 1000 ){
     time = 0;
  }
  */
  
  
  public void seeking()
   {
     

         if( time == 0.5){
           if (slot1 > 0){
             speed = -0.5;
           }else{
             speed = 0.5;
           }
         }
   
         if (slot1 > 231){
           speed = -0.5;
         }
        if(slot1 < -231){
          speed = 0.5;
        }
   
        turret.set(speed);
      }
        
   
   
  
  
  
  
   public void tracking()
   {
      if (Math.abs(tx) > 2){
         turn = (slot1 * 0.77922078 + tx * 1) * 1.283333333;  // moving to desired targets
       }else{
         //turn = (encoder.getPosition()*0.77922078);
       }
        if( turn > 462 ){             // shouldn't overshoot 360 degrees or 462 rotations
          turn = 462;
        }else if(turn < -462){
          turn = -462;
        }
     
  
      double z = oldTurn + 0.1 * (turn - oldTurn);    // smooth turning
  
      if(controller.getRawButton(1)){
        if(ta0 == 0 ||ta1 == 0 ||ta2 == 0 ){
          pid.setReference(oldTurn, CANSparkMax.ControlType.kPosition);    
        }else{
          pid.setReference(z, CANSparkMax.ControlType.kPosition);
          oldTurn = z;  // chnages old turn value to z 
 
        
        
      }
     }
   }

  
   
  
  
  
   public void run(boolean button)
   {
      
       tx0 = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx0").getDouble(0);  // three target points
       tx1 = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx1").getDouble(0);
       tx2 = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx2").getDouble(0);
       ta0 = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta0").getDouble(0);
       ta1 = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta1").getDouble(0);
       ta2 = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta2").getDouble(0);
       time = time + 0.02;  

      if(tx0 != 1000 &&  tx1 != 1000 && tx2 != 1000 ){
     time = 0;
  }
   if( time >= 0.5 && controller.getRawButton(1)) {
    seeking();

   } else{
      tracking();
   }
  

}
}
