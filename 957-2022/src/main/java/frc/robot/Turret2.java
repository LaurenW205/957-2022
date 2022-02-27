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
    
    CANSparkMax turret = new CANSparkMax(10,MotorType.kBrushless);     //variables
    SparkMaxPIDController pid = turret.getPIDController();
    RelativeEncoder encoder = turret.getEncoder();
    boolean button2 = false;
    double time = 0;
    double speed = 0.25;
    int caseNumber = 0;
   
    double tx0 = -NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx0").getDouble(0); // three target points
    double tx1 = -NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx1").getDouble(0);
    double tx2 = -NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx2").getDouble(0);
    double ta0 = -NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta0").getDouble(0); // target area (used to determine if target is seen)
    double ta1 = -NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta1").getDouble(0);
    double ta2 = -NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta2").getDouble(0);
    double tl  = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tl").getDouble(0);  

    double slot1 = 0;
    double slot2 = 0;
    double slot3 = 0;
   

   
    double turn = 0;
    double oldTurn = 0;

    double tx = (tx0 + tx1 + tx2)/3.00001 * 27.5;    // finding average of three points

    public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM;

    public Turret2(){
            //PID constants for PID shooter
            kP = 0.1; 
            kI = 0;
            kD = 0; 
            kIz = 0; 
            kFF = 0.000015; 
            kMaxOutput = 1; 
            kMinOutput = -1;
            maxRPM = 5700;
    
        //Sets PID constants
            pid.setP(kP);
            pid.setI(kI);
            pid.setD(kD);
            pid.setIZone(kIz);
            pid.setFF(kFF);
            pid.setOutputRange(kMinOutput, kMaxOutput);
    }
    


    public void seeking()
    {
        if( time == 0.5){   //if target not seen for 0.5 secs
            if (slot1 > 0){ //spin in positive position
            speed = -0.5;   
            }else{          //spin in negative direction
            speed = 0.5;       
            }
        }
   
        if (slot1 > 115.5){   //if turret gets to 90 degrees, spin the other way
            speed = -0.5;
        }
        if(slot1 < -115.5){
            speed = 0.5;
        }
   
        turret.set(speed); 
    }
    


    public void tracking(){
        if (Math.abs(tx) > 2)
            turn = (slot1 * 0.77922078 + tx * 1) * 1.283333333;   //moving to desired targets

        if( turn > 115.5 ){   //shouldn't overshoot 180 degrees or 115.5 rotations
            turn = 115.5;
         }else if(turn < -115.5){
                turn = -115.5;
        }
  
        double z = oldTurn + 0.2 * (turn - oldTurn);    //smooth turning

        if( z > 115.5 ){   //shouldn't overshoot 180 degrees or 115.5 rotations
        z = 115.5;
     }else if(z < -115.5){
            z = -115.5;
    }
  
        
        if(ta0 == 0 ||ta1 == 0 ||ta2 == 0 ){    // if target is not seen, set turret to old turn
            pid.setReference(oldTurn, CANSparkMax.ControlType.kPosition);    
        }else{
            pid.setReference(z, CANSparkMax.ControlType.kPosition);   // if target is seen set to z value
            oldTurn = z;    //changes old turn value to z 
        }
    
    }
   
    public void run(boolean button){
        tx0 = -NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx0").getDouble(0);  // three target points, updated data
        tx1 = -NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx1").getDouble(0);
        tx2 = -NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx2").getDouble(0);
        ta0 = -NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta0").getDouble(0);
        ta1 = -NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta1").getDouble(0);
        ta2 = -NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta2").getDouble(0);
        tl  = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tl").getDouble(0);
        tx = (tx0 + tx1 + tx2)/3.00001 * 27.5;    // finding average of three points

        time = time + 0.02;  

        //checks if limelight is on
        if(tl==-9000){
            //pid.setReference(0, CANSparkMax.ControlType.kPosition);
            //NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setDouble(1);//turns off limelight
        }
        
        //if target seen, sets time to zero
        if(ta0 != 0 &&  ta1 != 0 && ta2 != 0 ) 
        time = 0;

        System.out.println(caseNumber);

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
                //NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setDouble(3);//on
            }else{
                //NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setDouble(1);//off
            }
            //NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setDouble(3);//on
            //checks if limelight is on
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setDouble(-9000);
            slot3 = slot2;
            slot2 = slot1;
            slot1 = turret.getEncoder().getPosition();

    }   



    public void manualOverride(double x_axis, double y_axis, double manualAngle){

        double angle = 0;

        if( x_axis == 0)
            x_axis = 0.01;

        if(x_axis >= 0 && y_axis >= 0){ // x pos, y pos
            angle = Math.toDegrees(Math.atan(y_axis/x_axis));
            angle = angle - 90;

        }else if(x_axis < 0 && y_axis >= 0){ // x neg, y pos
            angle = 90 + (Math.toDegrees(Math.atan(y_axis/x_axis)));

        }else if (x_axis >= 0 && y_axis < 0 ) { //y neg, x pos
            angle = -90;
         
        } else if(x_axis < 0 && y_axis < 0){
            angle = 90;
        }

        if(Math.pow(x_axis, 2) + (Math.pow(y_axis, 2)) < 0.25)
            angle = 0;
        System.out.println(angle);
        pid.setReference((manualAngle + angle) *(1/0.77922078), CANSparkMax.ControlType.kPosition);
    }
}
