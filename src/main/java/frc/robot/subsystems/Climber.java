// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.ClimberConstants.*;

import com.revrobotics.CANSparkBase;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {
  
  CANSparkBase m_climber;

  /** Creates a new Climber. */
  public Climber() {
    m_climber = new CANSparkMax(kClimberID, MotorType.kBrushed);

    m_climber.setIdleMode(IdleMode.kBrake);

    m_climber.setSmartCurrentLimit(kClimberStallCurrentLimit, kClimberCurrentLimit);

    m_climber.setInverted(false);

  }

  /**
   * This method is an example of the 'subsystem factory' style of command creation. A method inside
   * the subsytem is created to return an instance of a command. This works for commands that
   * operate on only that subsystem, a similar approach can be done in RobotContainer for commands
   * that need to span subsystems. The Subsystem class has helper methods, such as the startEnd
   * method used here, to create these commands.
   */
  public Command climberUp() {
    // The startEnd helper method takes a method to call when the command is initialized and one to
    // call when it ends
    return this.startEnd(
        // When the command is initialized, set the wheels to the intake speed values
        () -> {
          m_climber.set(kClimberPower);
        },
        // When the command stops, stop the wheels
        () -> {
          m_climber.set(0.0);
        });
  }

  public Command climberDown() {
    // The startEnd helper method takes a method to call when the command is initialized and one to
    // call when it ends
    return this.startEnd(
        // When the command is initialized, set the wheels to the intake speed values
        () -> {
          m_climber.set(-kClimberPower);
        },
        // When the command stops, stop the wheels
        () -> {
          m_climber.set(0.0);
        });
  }

  // An accessor method to set the speed (technically the output percentage) of the launch wheel
  public void setClimberWheel(double speed) {
    m_climber.set(speed);
  }

}
