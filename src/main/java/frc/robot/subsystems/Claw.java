// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.ClawConstants.*;

import com.revrobotics.CANSparkBase;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Claw extends SubsystemBase {
  
  CANSparkBase m_clawWheel;

  /** Creates a new Claw. */
  public Claw() {
    m_clawWheel = new CANSparkMax(kRollerClawID, MotorType.kBrushed);

    m_clawWheel.setSmartCurrentLimit(kClawCurrentLimit);

    m_clawWheel.setInverted(true);

  }

  /**
   * This method is an example of the 'subsystem factory' style of command creation. A method inside
   * the subsytem is created to return an instance of a command. This works for commands that
   * operate on only that subsystem, a similar approach can be done in RobotContainer for commands
   * that need to span subsystems. The Subsystem class has helper methods, such as the startEnd
   * method used here, to create these commands.
   */
  public Command clawIn() {
    // The startEnd helper method takes a method to call when the command is initialized and one to
    // call when it ends
    return this.startEnd(
        // When the command is initialized, set the wheels to the intake speed values
        () -> {
          m_clawWheel.set(-kClawPower);
        },
        // When the command stops, stop the wheels
        () -> {
          m_clawWheel.set(0.0);
        });
  }

  public Command clawOut() {
    // The startEnd helper method takes a method to call when the command is initialized and one to
    // call when it ends
    return this.startEnd(
        // When the command is initialized, set the wheels to the intake speed values
        () -> {
          m_clawWheel.set(kClawPower);
        },
        // When the command stops, stop the wheels
        () -> {
          m_clawWheel.set(0.0);
        });
  }


  //expansion idea: if your robot drops notes when traversing the field, impliment a way for the claw to be passivly rolling its
  //wheels inwards (parhaps using kClawRetainPower) to help hold onto the note


  // An accessor method to set the speed (technically the output percentage) of the launch wheel
  public void setClawWheel(double speed) {
    m_clawWheel.set(speed);
  }

}
