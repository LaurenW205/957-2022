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

public class Turret2 {
    
    CANSparkMax turret = new CANSparkMax(7,MotorType.kBrushless);     //making variables
    SparkMaxPIDController pid;
    RelativeEncoder encoder;
    Joystick controller = new Joystick(0);
    double time = 0;
    double speed = 0.25;
    int caseNumber = 0;
   


    double tx0 = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx0").getDouble(0); // three target points
    double tx1 = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx1").getDouble(0);
    double tx2 = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx2").getDouble(0);
    double ta0 = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta0").getDouble(0); // target area (used to determine if target is seen)
    double ta1 = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta1").getDouble(0);
    double ta2 = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta2").getDouble(0);

   

    double slot1 = 0;
    double slot2 = 0;
    double slot3 = 0;
   

   
    double turn = 0;
    double oldTurn = 0;

    double tx = (tx0 + tx1 + tx2)/3.00001 * 27.5;    // finding average of three points
    


    public void seeking()
    {
        if( time == 0.5){   //if target not seen for 0.5 secs
            if (slot1 > 0){ //spin in positive position
            speed = -0.5;   
            }else{          //spin in negative direction
            speed = 0.5;       
            }
        }
   
        if (slot1 > 231){   //if turret gets to 180 degrees, spin the other way
            speed = -0.5;
        }
        if(slot1 < -231){
            speed = 0.5;
        }
   
        turret.set(speed); 
    }

    
    
    public void tracking(){
        if (Math.abs(tx) > 2)
            turn = (slot1 * 0.77922078 + tx * 1) * 1.283333333;   //moving to desired targets

        if( turn > 462 ){   //shouldn't overshoot 360 degrees or 462 rotations
            turn = 462;
         }else if(turn < -462){
                turn = -462;
        }
  
        double z = oldTurn + 0.1 * (turn - oldTurn);    //smooth turning
  
        if(controller.getRawButton(1)){
            if(ta0 == 0 ||ta1 == 0 ||ta2 == 0 ){  // if target is not seen, set turret to old turn
                pid.setReference(oldTurn, CANSparkMax.ControlType.kPosition);    
            }else{
                pid.setReference(z, CANSparkMax.ControlType.kPosition);   // if target is seen set to z value
                oldTurn = z;    //changes old turn value to z 
            }
        }
    }
   
    public void run(boolean button){
        tx0 = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx0").getDouble(0);  // three target points, updated data
        tx1 = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx1").getDouble(0);
        tx2 = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx2").getDouble(0);
        ta0 = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta0").getDouble(0);
        ta1 = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta1").getDouble(0);
        ta2 = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta2").getDouble(0);
        time = time + 0.02;  
        
        //if target seen, sets time to zero
        if(ta0 != 0 &&  ta1 != 0 && ta2 != 0 ) 
        time = 0;

        switch(caseNumber){
            case 0: //sets turret to zero
                pid.setReference(0, CANSparkMax.ControlType.kPosition);
                caseNumber ++;
            break;

            case 1: //checks if button is pressed
                if(button)
                    caseNumber++;
            break;

            case 2: //checks if button is released
                 time = 10;
                if(!button)
                    caseNumber++;
            break;
                
            case 3: //seeks and tracks target, stops if button pressed
                if (time < 0.5){
                    tracking();
                } else{
                    seeking();
                }
                if(button)
                    caseNumber++;
            break;

            case 4: //resets turret to zero
                pid.setReference(0, CANSparkMax.ControlType.kPosition);
                if(!button);
                    caseNumber = 0;
            break;
        }   
            //turns limelight on if seeking or tracking
            if(caseNumber == 3){
                NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setDouble(3);
            }else{
                NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setDouble(1);
            }
    }   
}
