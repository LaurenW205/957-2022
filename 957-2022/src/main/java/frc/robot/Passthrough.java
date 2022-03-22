package frc.robot;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;

public class Passthrough {
    private static Passthrough m_passthrough;
    public CANSparkMax pusher = new CANSparkMax(9, MotorType.kBrushless);
    boolean oldSensor = false;
    RelativeEncoder m_pushEncoder = pusher.getEncoder();
    public int intakeFlag = 0;
    public double target_pos = 0;
    double offset = 16;
    int maxTime = 30;
    int state = 0;
    public boolean intakeSensor = true;
    double timer = 0;

    public Passthrough(){
        pusher.restoreFactoryDefaults();
        m_pushEncoder.setPosition(0);
        pusher.setIdleMode(IdleMode.kBrake);
    }

    public void raiseFlag(int cargo){
        if (cargo != 2){
            intakeFlag = 1;
        }    
    }
    
    public void run(int cargo){

        pusher.setIdleMode(IdleMode.kBrake);

        if(!intakeSensor){
            timer = 0;
        }

        if(intakeFlag != 0){
            pusher.set(.3*intakeFlag);
            
            if (intakeFlag == 1 && (pusher.getEncoder().getPosition() > target_pos + offset || timer >0.04)){  
                target_pos = target_pos + offset;
                intakeFlag = 0;
                pusher.set(0);
            }
            if (intakeFlag == -1 && pusher.getEncoder().getPosition() < target_pos - offset){
                target_pos = target_pos - offset;
                intakeFlag = 0;
                pusher.set(0);
            }
        }
        timer = timer + 0.02;
    }

  

    public static synchronized Passthrough getInstance(){
        if (m_passthrough == null)
            m_passthrough = new Passthrough();

        return m_passthrough;
    }
}
