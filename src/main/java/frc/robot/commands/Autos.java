// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Claw;
import frc.robot.subsystems.Launcher;

import frc.robot.Constants.AutonConstants;
import frc.robot.Constants.LauncherConstants;

public final class Autos {
  /** Example static factory for an autonomous command. */
  public static Command backupAuto(Drivetrain drivetrain, double startDelay) {
    return new RunCommand(() -> drivetrain.arcadeDrive(0, 0), drivetrain)
              .withTimeout(startDelay)
              .andThen(new RunCommand(() -> drivetrain.arcadeDrive(AutonConstants.kAutonBackSpeed, 0), drivetrain)
              .withTimeout(AutonConstants.kAutonBackTime)
              .andThen(new RunCommand(() -> drivetrain.arcadeDrive(0, 0), drivetrain)));
  }

  // launch only
  public static Command shootAuto(Launcher launcher, Drivetrain drivetrain, double startDelay) {
    //return new RunCommand(() -> drivetrain.arcadeDrive(0, 0), drivetrain);
    return new RunCommand(() -> drivetrain.arcadeDrive(0, 0), drivetrain)
              .withTimeout(startDelay)
              .andThen(new PrepareLaunch(launcher)
              .withTimeout(LauncherConstants.kLauncherDelay)
              .andThen(new LaunchNote(launcher)));
  }

  // launch and drive
  public static Command shootDriveAuto(Launcher launcher, Drivetrain drivetrain, double startDelay, double driveDelay) {
    //return new RunCommand(() -> drivetrain.arcadeDrive(0, 0), drivetrain);
    return new RunCommand(() -> drivetrain.arcadeDrive(0, 0), drivetrain)
              .withTimeout(startDelay)
              .andThen(new PrepareLaunch(launcher)
              .withTimeout(LauncherConstants.kLauncherDelay)
              .andThen(new LaunchNote(launcher)
              .withTimeout(driveDelay))
              .andThen(new RunCommand(() -> drivetrain.arcadeDrive(AutonConstants.kAutonBackSpeed, 0), drivetrain)
              .withTimeout(AutonConstants.kAutonBackTime)));
  }

  public static Command shootAmpAuto(Claw m_claw) {
    return new RunCommand(() -> m_claw.clawOut());
  }


  private Autos() {
    throw new UnsupportedOperationException("This is a utility class!");
  }
}
