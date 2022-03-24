package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Turret2 {
    
    CANSparkMax turret = new CANSparkMax(10,MotorType.kBrushless);     //variables
    SparkMaxPIDController pid = turret.getPIDController();
    RelativeEncoder encoder = turret.getEncoder();
    boolean button2 = false;
    double time = 0;
    double speed = 0.25;
    int caseNumber = 0;
    NetworkTableEntry radius; 
    NetworkTableEntry tx;
    boolean right;
    boolean left; 

    
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

        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        NetworkTable table = inst.getTable("datatable");

        radius = table.getEntry("radius");
        tx = table.getEntry("tx"); 
    }
    
    public void seeking(boolean l, boolean r)
    {
        // setting which direction to turn in
        
        if(time >= 0.5){   //if target not seen for 0.5 secs
            if(l){
            speed = -0.5; 
            }
            if(r){
                speed = 0.5;
            }
        }
   
        if (turret.getEncoder().getPosition() > 115.5){   //if turret gets to 90 degrees, spin the other way
            speed = -0.5;
        }
        if(turret.getEncoder().getPosition() < -115.5){
            speed = 0.5;
        }
   
        turret.set(speed); 
    }
    


    public void tracking(){
        // It is already known that we have a valid target
        if(tx.getDouble(-9000) != -9000){
            turret.set(speed);
            return;
        }

        // Get tx
        double target = tx.getDouble(-9000);
        // determining which direction to turn in based on target position
        if(target > 0){
            speed = -0.5 * Math.abs(target);
        }else if(target < 0){
            speed = 0.5 * Math.abs(target);
        }

        // If our current encoder position exceeds our bounds (+90 -90), set speed to 0
        
        if(encoder.getPosition()>90 && speed > 0){
            speed = 0;
        }
        if(encoder.getPosition()<-90 && speed < 0){
            speed = 0;
        }

        // turning the turret based on the speed 
        turret.set(speed); 
    
    }
   
    public void run(boolean leftSearch, boolean rightSearch){

        time = time + 0.02;  

        //if target seen, sets time to zero
        if(tx.getDouble(-9000) != -9000) 
            time = 0;

        switch(caseNumber){
            case 0: //sets turret to zero
                pid.setReference(0, CANSparkMax.ControlType.kPosition);
                caseNumber ++;
            break;

            case 1: //checks if left search or right search button is pressed
                if(leftSearch || rightSearch)
                    caseNumber++;

                left = leftSearch;
                right = rightSearch;       
            break;

            case 2: //checks if both buttons are released
                time = 10;
                if(!leftSearch && !rightSearch)
                    caseNumber++;
            break;
                
            case 3: //seeks and tracks target, stops if button pressed
                if (time < 0.5){
                    tracking();
                } else{
                    seeking(leftSearch, rightSearch);
                }
                if(rightSearch || leftSearch)
                    caseNumber++;
            break;

            case 4: //resets turret to zero
                pid.setReference(0, CANSparkMax.ControlType.kPosition);
                if(!rightSearch && leftSearch);
                    caseNumber = 0;
            break;
        }   
    }   

    public void manualOverride(double x_axis, double y_axis, double manualAngle, double gyroAngle){

        double angle = 0;

        if( x_axis == 0)
            x_axis = 0.01;

        if(x_axis >= 0 && y_axis >= 0){ // x pos, y pos
            angle = Math.toDegrees(Math.atan(y_axis/x_axis));
            angle = angle - 90;

        }else if(x_axis < 0 && y_axis >= 0){ // x neg, y pos
            angle = 90 + (Math.toDegrees(Math.atan(y_axis/x_axis)));

        }else if (x_axis >= 0 && y_axis < 0 ) { //y neg, x pos
            angle = Math.toDegrees(Math.atan(y_axis/x_axis));
            angle = angle - 90;
         
        } else if(x_axis < 0 && y_axis < 0){ //y neg, x neg
            angle =  Math.toDegrees(Math.atan(y_axis/x_axis));
            angle = 90 + angle;
        }
        
        if(angle > 90){
            angle = 90;
        }else if(angle < -90){
            angle = -90;
        }
        if(Math.pow(x_axis, 2) + (Math.pow(y_axis, 2)) < 0.25)
            angle = 0;
        pid.setReference((manualAngle + angle) *(1/0.77922078), CANSparkMax.ControlType.kPosition);
    }
}
