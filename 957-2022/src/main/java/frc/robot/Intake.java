package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticsModuleType;

public class Intake {
    DoubleSolenoid doubleSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 1, 0);
    CANSparkMax intakeMotor = new CANSparkMax(0, MotorType.kBrushless);
    DigitalInput sensor = new DigitalInput(0);
    int var = 5;
    boolean lastCycle = false;


    public void ExtendCyl() {
        doubleSolenoid.set(Value.kForward);
        intakeMotor.set(1);
    }

    public void RetractCyl() {
        doubleSolenoid.set(Value.kReverse);
        intakeMotor.set(0);
    }

    public int run(int cargoNum, boolean button) {
    
        switch (var) {
            case 0:
                if (button)
                    var++;
            break;

            case 1:
                if (!button)
                    var = 2;
            break;

            case 2:
                ExtendCyl();
                var = 3;
            break;

            case 3:
                if (button)
                    var = 4;
            break;

            case 4:
                if (!button)
                    var = 5;
            break;

            case 5:
                RetractCyl();
                var = 0;
            break;

        }

        boolean nowCycle = sensor.get();

        if (nowCycle == false) {
            if (lastCycle == true)
                cargoNum++;
        }

        lastCycle = nowCycle;

        return cargoNum;
    }
}
