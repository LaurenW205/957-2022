package frc.robot.automodes;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;

import frc.robot.DriveTrain;
import frc.robot.Intake;
import frc.robot.Shooter;
import frc.robot.Turret2;

public class testauto {
    int autoStep = 0;
    double timer = 0;

    public void reset(){
        autoStep = 0;
        timer = 0;
    }


    public void run(DriveTrain d, Shooter s, Intake i, Turret2 t, int cargoNum){
        timer = timer + 0.02;
        s.speed = 2250;

        switch(autoStep){
            case 0: // drive forward slow
                if(timer <= 2){
                    d.arcadeDrive(.25, 0);
                
                    
                }else{
                    d.arcadeDrive(0, 0);
                    timer = 0;
                    d.resetEncoders();
                    autoStep ++;
                }
            break;
            case 1:
                if(timer <= 2){ // drive backward slow
                    d.arcadeDrive(-.25, 0);
                    
                    
                }else{
                    d.arcadeDrive(0, 0);
                    timer = 0;
                    d.resetEncoders();
                    autoStep ++;

                }
                break;
            case 2:
                if(timer <= 2){ // drive forward fast
                    d.arcadeDrive(.5, 0);
                    
                
                }else{
                    d.arcadeDrive(0, 0);
                    timer = 0;
                    d.resetEncoders();
                    autoStep ++;
                }
            break;

            case 3:
                if(timer <= 2){ // drive backward fast
                    d.arcadeDrive(-0.5, 0);
                    

                }else{
                    d.arcadeDrive(0, 0);
                    timer = 0;
                    d.resetEncoders();
                    autoStep ++;
                    
                }
            break;

            case 4:
                    i.var = 2;// intake goes down
                    timer = 0;
                    d.resetEncoders();
                    autoStep ++;
            break;

            case 5:
                if (cargoNum == 2 && timer >= 3){ // waits three seconds and checks if 2 cargo, then reverse
                    timer = 0;
                        if (timer <= 5)
                            i.reverse(cargoNum);
                
                }

            break;

        }
    }
}
