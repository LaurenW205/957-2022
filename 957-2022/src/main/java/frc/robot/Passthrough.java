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
    public boolean cargoDown = true;
    RelativeEncoder m_pushEncoder = pusher.getEncoder();
    int intakeFlag = 0;
    public double target_pos = 0;
    double offset = 16;
    int maxTime = 30;
    int state = 0;

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
    
    public void run(int cargo, boolean button, boolean reverse){
        if(reverse){
            intakeFlag = 0;
            return;
        }
        pusher.setIdleMode(IdleMode.kBrake);
        if (cargo == 1)  // If we have 1 cargo, we may allow the driver to move the cargo
        {   
            switch(state)
            {
            case 0:         // Check for button press
                if (button && intakeFlag == 0)
                state++;
            break;

            case 1:         // Check for button to be unpressed, if so raise flag to move cargo    
                target_pos = pusher.getEncoder().getPosition();
                if (!button)
                {
                   
                    if (cargoDown)   // If cargo is down, push up and set cargo to being up
                    {
                      intakeFlag = 1;
                      cargoDown = false;
                    }
                    else        // If cargo is up, push down and set cargo to being down
                    {
                      intakeFlag = -1;
                      cargoDown = true;
                    }
                }
                state++;
            break;

            case 2:         // Ignore any button presses until flag lowers
                if (intakeFlag == 0)
                  state++;
            break;

            }
        }
        else            // If we have 2 cargo, we reset any process that was ongoing in the case statements
          state = 0;
            
        if(intakeFlag != 0){
            pusher.set(.3*intakeFlag);
            
            if (intakeFlag == 1 && pusher.getEncoder().getPosition() > target_pos + offset ){  
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
    }

  

    public static synchronized Passthrough getInstance(){
        if (m_passthrough == null)
            m_passthrough = new Passthrough();

        return m_passthrough;
    }
}
