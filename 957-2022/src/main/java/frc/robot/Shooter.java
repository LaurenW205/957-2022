package frc.robot;

import javax.lang.model.util.ElementScanner6;


import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.motorcontrol.Spark;

public class Shooter {
    double minimumSpeed;

   
    public CANSparkMax shooter = new CANSparkMax(6, MotorType.kBrushless);
    RelativeEncoder encoder = shooter.getEncoder();
    SparkMaxPIDController p = shooter.getPIDController();
    DigitalInput breakBeamSensor = new DigitalInput(2);
    public int caseNumber = 0;
    boolean oldSensor = false;
    public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM;
    double timer = 0;
    double timer2 = 0;

    public Shooter(){
    //PID constants for PID shooter
        kP = 3e-3; 
        kI = 0;
        kD = 0; 
        kIz = 0; 
        kFF = 0.000015; 
        kMaxOutput = 1; 
        kMinOutput = -1;
        maxRPM = 5700;

    //Sets PID constants
        p.setP(kP);
        p.setI(kI);
        p.setD(kD);
        p.setIZone(kIz);
        p.setFF(kFF);
        p.setOutputRange(kMinOutput, kMaxOutput);
    }

    public int run(int cargo, boolean button){

        timer = timer + 0.02;

        switch(caseNumber){
        case 0: //checks if button is pressed
            if(button)
                caseNumber ++;
        break;

        case 1: //checks if button is released
            oldSensor = breakBeamSensor.get();
            if(!button)
                caseNumber ++;
                timer2 = 0;
        break;

        case 2: //turns motor on until button is pressed or no cargo
        
            //checks if sensor beam is broken and decrease cargo amount
            if(breakBeamSensor.get() && !oldSensor)
                cargo = cargo - 1;
    
            oldSensor = breakBeamSensor.get();

            p.setReference(-2650, ControlType.kVelocity);

            if(shooter.getEncoder().getVelocity()< -2400){
                Passthrough.getInstance().pusher.set(.2);
            }else{
                Passthrough.getInstance().pusher.set(0);
            }

            if (cargo != 1)
                timer2 = 0;
            else
                timer2 = timer2 + 0.02;

            if (timer2 > 3){
                caseNumber++;
                cargo = 0;
            }

            if(cargo == 0 && timer > 0.5){
                caseNumber++;
            }else if (cargo != 0){
                timer = 0;
            }

            if(button)
                caseNumber++;
        break;

        case 3: //checks if button is not pressed
        p.setReference(0, ControlType.kVelocity);
        Passthrough.getInstance().target_pos = Passthrough.getInstance().pusher.getEncoder().getPosition();

            Passthrough.getInstance().pusher.set(0);
            if(!button)
                caseNumber = 0;
        break;
        }

        


        //updates cargo amount
        return cargo;

        
    }
}
