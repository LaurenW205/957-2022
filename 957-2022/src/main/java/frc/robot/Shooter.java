package frc.robot;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.motorcontrol.Spark;

public class Shooter {
    double minimumSpeed;

    AHRS ahrs = new AHRS(SerialPort.Port.kMXP); 
    CANSparkMax shooter = new CANSparkMax(6, MotorType.kBrushless);
    RelativeEncoder encoder = shooter.getEncoder();
    SparkMaxPIDController p = shooter.getPIDController();
    DigitalInput breakBeamSensor = new DigitalInput(0);
    public int caseNumber = 0;
    boolean oldSensor = false;
    public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM;

    public Shooter(){
    //PID constants for PID shooter
        kP = 6e-5; 
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

        switch(caseNumber){
        case 0: //checks if button is pressed
            if(button)
                caseNumber ++;
        break;

        case 1: //checks if button is released
            if(!button)
                caseNumber ++;
        break;

        case 2: //turns motor on until button is pressed or no cargo
            shooter.set(5700);

            if(shooter.getEncoder().getVelocity()> 4000){
                Passthrough.getInstance().feeder.set(.5);
            }else{
                Passthrough.getInstance().feeder.set(0);
            }

            if(button || cargo == 0)
                caseNumber++;
        break;

        case 3: //checks if button is not pressed
            if(!button)
                caseNumber = 0;
        }

        //checks if sensor beam is broken and decrease cargo amount
        if(breakBeamSensor.get() && !oldSensor)
            cargo = cargo - 1;

        oldSensor = breakBeamSensor.get();

        //updates cargo amount
        return cargo;
    }
}
